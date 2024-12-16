package jp.go.aist.rtm.cuirtcbuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.Generator;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.IDLParamConverter;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;
import jp.go.aist.rtm.rtcbuilder.java.manager.JavaCMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.java.manager.JavaGenerateManager;
import jp.go.aist.rtm.rtcbuilder.python.manager.PythonCMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.python.manager.PythonGenerateManager;
import jp.go.aist.rtm.rtcbuilder.util.FileUtil;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CuiRtcBuilder {

	public static void main(String[] args) {
		
//		File currentDirectory = new File(".");  
//		System.out.println(currentDirectory.getAbsolutePath());  
		
		Option opt_file = OptionBuilder.isRequired(true).hasArg(true).create("f");
		Option opt_dir = OptionBuilder.isRequired(true).hasArg(true).create("d");
		Option opt_idl = OptionBuilder.isRequired(false).hasArgs().create("i");
		Options opts = new Options();
		opts.addOption(opt_file);
		opts.addOption(opt_dir);
		opts.addOption(opt_idl);
		
		CommandLineParser parser = new PosixParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(opts, args);
		} catch (ParseException ex) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("CuiRTCBuilder", opts, true);
			return;
		}
		//
		String targetFile = null;
		String targetDir = null;
		String[] idlDir = null;
		if(cmd.hasOption("f")) {
			targetFile = cmd.getOptionValue("f");
			cmd.getOptions();
		}
		if(cmd.hasOption("d")) {
			targetDir = cmd.getOptionValue("d");
		}
		if(cmd.hasOption("i")) {
			idlDir = cmd.getOptionValues("i");
		}
		List<IdlPathParam> idlDirs = null;
		if(idlDir!=null) {
			List<String> idlDirList = Arrays.asList(idlDir);
			for(String each : idlDirList) {
				idlDirs.add(new IdlPathParam(each, false));
			}
		}
		//
		JavaCMakeGenerateManager JavaCmanager = new JavaCMakeGenerateManager();
		JavaGenerateManager Javamanager = new JavaGenerateManager();
		PythonCMakeGenerateManager PyCmanager = new PythonCMakeGenerateManager();
		PythonGenerateManager Pymanager = new PythonGenerateManager();
		ProfileHandler handler = new ProfileHandler(true);
		handler.addManager(JavaCmanager);
		handler.addManager(Javamanager);
		handler.addManager(PyCmanager);
		handler.addManager(Pymanager);
		GeneratorParam generatorParam = null;
		try {
			generatorParam = handler.restorefromXMLFile(targetFile, true);
			if(idlDir!=null) {
				extractDataTypes(idlDirs, generatorParam);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Generator generator = new Generator();
		generator.addGenerateManager(JavaCmanager);
		generator.addGenerateManager(Javamanager);
		generator.addGenerateManager(PyCmanager);
		generator.addGenerateManager(Pymanager);
		try {
			generator.validate(generatorParam.getRtcParam());
			List<GeneratedResult> results = generator.generateTemplateCode(generatorParam, idlDirs);
			for(GeneratedResult target : results) {
				String fileName = targetDir + File.separator + target.getName().replace("/", File.separator);
				writeFile(fileName, target.getCode(), "UTF-8");
			}
			//
			RtcParam rtcParam = generatorParam.getRtcParam();
			for( IdlFileParam idlFile : rtcParam.getProviderIdlPathes() ) {
				String idlTarget = targetDir + File.separator + "idl" + File.separator + idlFile.getIdlFile();
				FileChannel src = new FileInputStream(idlFile.getIdlPath()).getChannel();
				FileChannel trg = new FileOutputStream(idlTarget).getChannel();
				try {
					src.transferTo(0, src.size(), trg);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					src.close();
					trg.close();
				}
			}
			for( IdlFileParam idlFile : rtcParam.getConsumerIdlPathes() ) {
				String idlTarget = targetDir + File.separator + "idl" + File.separator + idlFile.getIdlFile();
				FileChannel src = new FileInputStream(idlFile.getIdlPath()).getChannel();
				FileChannel trg = new FileOutputStream(idlTarget).getChannel();
				try {
					src.transferTo(0, src.size(), trg);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					src.close();
					trg.close();
				}
			}
			//
			for( String includedIdlFile : rtcParam.getIncludedIdls() ) {
				File target = new File(includedIdlFile);
				String idlTarget = targetDir + File.separator + "idl" + File.separator + target.getName();
				FileChannel src = new FileInputStream(includedIdlFile).getChannel();
				FileChannel trg = new FileOutputStream(idlTarget).getChannel();
				try {
					src.transferTo(0, src.size(), trg);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					src.close();
					trg.close();
				}
			}
		} catch (Exception e) {
//			e.printStackTrace();
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	private static String[] extractDataTypes(List<IdlPathParam> target, GeneratorParam generatorParam) {
		String FS = System.getProperty("file.separator");
		List<IdlPathParam> sources = new ArrayList<IdlPathParam>();
		sources.addAll(target);
		String defaultPath = System.getenv("RTM_ROOT");
		if (defaultPath != null) {
			if(!defaultPath.endsWith(FS)) {
				defaultPath += FS;
			}
			sources.add(0, new IdlPathParam(defaultPath + "rtm" + FS + "idl", false));
		}
		List<DataTypeParam> sourceContents = new ArrayList<DataTypeParam>();
		for (int intidx = 0; intidx < sources.size(); intidx++) {
			IdlPathParam source = sources.get(intidx);
			try {
				File idlDir = new File(source.getPath());
				String[] list = idlDir.list();
				if (list == null) {
					continue;
				}
				List<String> idlNames = new ArrayList<String>();
				for (String name : list) {
					if (name.toLowerCase().endsWith(".idl")) {
						idlNames.add(name);
					}
				}
				Collections.sort(idlNames, new Comparator<String>() {
					public int compare(String a, String b) {
						return a.compareTo(b);
					}
				});
				for (String idlName : idlNames) {
					String idlContent = FileUtil
							.readFile(source + FS + idlName);
					DataTypeParam param = new DataTypeParam();
					param.setContent(idlContent);
					param.setFullPath(source + FS + idlName);
					sourceContents.add(param);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (java.lang.SecurityException e) {
				e.printStackTrace();
			}
		}
		String[] defaultTypeList = new String[0];
		List<String> dataTypes = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		IDLParamConverter.extractTypeDef(sourceContents, dataTypes, builder);
		defaultTypeList = new String[dataTypes.size()];
		defaultTypeList = dataTypes.toArray(defaultTypeList);
		//
		generatorParam.getDataTypeParams().clear();
		generatorParam.getDataTypeParams().addAll(sourceContents);

		return defaultTypeList;
	}
	
	private static void writeFile(String file, String contents, String encode) throws IOException {
		try{
			File target = new File(file);
			File dir = new File(target.getParent());
			if(dir.exists()==false) {
				dir.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos, encode);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(contents);
			bw.close();
			osw.close();
			fos.close();
		} catch (IOException e) {
			throw new IOException("Error! : File Write path:" + file);  //$NON-NLS-1$
		}
	}

}
