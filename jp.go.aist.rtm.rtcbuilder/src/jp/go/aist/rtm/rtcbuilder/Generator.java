package jp.go.aist.rtm.rtcbuilder;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.corba.idl.parser.IDLParser;
import jp.go.aist.rtm.rtcbuilder.corba.idl.parser.ParseException;
import jp.go.aist.rtm.rtcbuilder.corba.idl.parser.syntaxtree.specification;
import jp.go.aist.rtm.rtcbuilder.fsm.StateParam;
import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import jp.go.aist.rtm.rtcbuilder.generator.HeaderException;
import jp.go.aist.rtm.rtcbuilder.generator.IDLParamConverter;
import jp.go.aist.rtm.rtcbuilder.generator.PreProcessor;
import jp.go.aist.rtm.rtcbuilder.generator.param.ConfigSetParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.GeneratorParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.PropertyParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortInterfaceParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.ServicePortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlFileParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceArgumentParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceClassParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.ServiceMethodParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.TypeDefParam;
import jp.go.aist.rtm.rtcbuilder.generator.parser.MergeBlockParser;
import jp.go.aist.rtm.rtcbuilder.manager.CMakeGenerateManager;
import jp.go.aist.rtm.rtcbuilder.manager.CXXGenerateManager;
import jp.go.aist.rtm.rtcbuilder.manager.CommonGenerateManager;
import jp.go.aist.rtm.rtcbuilder.manager.GenerateManager;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.compare.GeneratedCautionDialog;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ComponentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.util.FileUtil;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;
import jp.go.aist.rtm.rtcbuilder.util.ValidationUtil;

/**
 * ジェネレータクラス
 */
public class Generator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(Generator.class);
	private String warningMessage = "";

	Map<String, GenerateManager> generateManagerList = new HashMap<String, GenerateManager>();

	public Generator() {
		this.addGenerateManager(new CommonGenerateManager());
		this.addGenerateManager(new CXXGenerateManager());
		this.addGenerateManager(new CMakeGenerateManager());
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	/**
	 * ジェネレート・マネージャを追加する
	 *
	 * @param genManager
	 *            　生成対象のジェネレート・マネージャ
	 */
	public void addGenerateManager(GenerateManager genManager) {
		String key = genManager.getClass().getName();
		generateManagerList.put(key, genManager);
	}

	/**
	 * ジェネレート・マネージャをクリアする
	 */
	public void clearGenerateManager() {
		generateManagerList.clear();
	}

	public List<GeneratedResult> generateTemplateCode(GeneratorParam generatorParam) throws Exception {
		validate(generatorParam.getRtcParam());
		return generateTemplateCode(generatorParam, null);
	}

	public void validateIDLDef(GeneratorParam generatorParam, List<IdlPathParam> idlDir) throws Exception {
		RtcParam rtcParam  = generatorParam.getRtcParam();

		List<String> checkedIDL = new ArrayList<String>();
		List<String> dummy = new ArrayList<String>();

		for( ServicePortParam serviceport : rtcParam.getServicePorts() ) {
			for( ServicePortInterfaceParam serviceInterfaces : serviceport.getServicePortInterfaces() ) {
				String targetIDL = serviceInterfaces.getIdlFullPath();
				if(checkedIDL.contains(targetIDL)) continue;
				checkedIDL.add(targetIDL);
				//
				File file = new File(targetIDL);
				if(file.exists()==false) {
					throw new FileNotFoundException("Target IDL File [" + targetIDL + "] NOT EXists.");
				}
				String idlContent = FileUtil.readFile(targetIDL);
				if (idlContent == null) continue;
				List<String> idlSearchDirs = new ArrayList<String>();
				for(IdlPathParam path : rtcParam.getIdlSearchPathList()) {
					idlSearchDirs.add(path.getPath());
				}
				if(idlDir!=null){
					for(IdlPathParam each : idlDir) {
						idlSearchDirs.add(each.getPath());
					}
				}
				PreProcessor.parse(idlContent, idlSearchDirs, dummy, true);
			}
		}
	}

	/**
	 * ジェネレートする
	 *
	 * @param generatorParam
	 *            パラメータ
	 * @return GeneratedResultのリスト
	 * @throws ParseException
	 *             IDLのパースに失敗した場合など
	 */
	public List<GeneratedResult> generateTemplateCode(
			GeneratorParam generatorParam, List<IdlPathParam> idlDir) throws Exception {

		List<ServiceClassParam> rtcServiceClasses = new ArrayList<ServiceClassParam>();
		//IDL重複チェック用
		List<String> IDLPathes = new ArrayList<String>();
		//IDL読み込み用
		List<ServiceClassParam> IDLPathParams = new ArrayList<ServiceClassParam>();
		List<GeneratedResult> result = new ArrayList<GeneratedResult>();

		RtcParam rtcParam =  generatorParam.getRtcParam();
		//onImplementedフラグの修正
		for(int index=IRtcBuilderConstants.ACTIVITY_INITIALIZE;index<IRtcBuilderConstants.ACTIVITY_DUMMY;index++) {
			if(rtcParam.getDetailContent(index)!=null && 0<rtcParam.getDetailContent(index).length()) {
				rtcParam.setActionImplemented(index, true);
			}
		}
		rtcParam.checkAndSetParameter();
		rtcParam.getIdlPathes().clear();
		rtcParam.getIdlPathes().addAll(rtcParam.getIdlSearchPathList());
		//
		for( DataPortParam outport : rtcParam.getOutports() ) {
			if(0<outport.getIdlFile().length()) {
				IDLPathes.add(outport.getIdlFile());
                IDLPathParams.add(new ServiceClassParam(outport.getIdlFile(), outport.getIdlFile(), ""));
			}
		}
		for( DataPortParam inport : rtcParam.getInports() ) {
			if(0<inport.getIdlFile().length()) {
				IDLPathes.add(inport.getIdlFile());
                IDLPathParams.add(new ServiceClassParam(inport.getIdlFile(), inport.getIdlFile(), ""));
			}
		}
		for( ConfigSetParam config : rtcParam.getConfigParams() ) {
			if(0<config.getIdlFile().length()) {
				IDLPathes.add(config.getIdlFile());
                IDLPathParams.add(new ServiceClassParam(config.getIdlFile(), config.getIdlFile(), ""));
			}
		}
		//
		for( ServicePortParam serviceport : rtcParam.getServicePorts() ) {
			for( ServicePortInterfaceParam serviceInterfaces : serviceport.getServicePortInterfaces() ) {
				if( !IDLPathes.contains(serviceInterfaces.getIdlFullPath()) ) {
					IDLPathes.add(serviceInterfaces.getIdlFullPath());
                    IDLPathParams.add(
                            new ServiceClassParam(serviceInterfaces.getIdlFullPath(), serviceInterfaces.getIdlFullPath(), ""));
				}
			}
		}
		if(idlDir!=null) {
			for(IdlPathParam param : idlDir) {
				if( 0< param.getPath().length() ) {
					boolean existed = false;
					for(IdlPathParam exist : rtcParam.getIdlPathes()) {
						if(exist.getPath().equals(param.getPath())) {
							existed = true;
							break;
						}
					}
					if(existed==false) {
						rtcParam.getIdlPathes().add(new IdlPathParam(param.getPath(), false));
					}
				}
			}
		}

		rtcServiceClasses.addAll(getRtcServiceClass(rtcParam, IDLPathParams, idlDir, generatorParam.getDataTypeParams()));
		checkReferencedServiceParam(rtcServiceClasses, rtcParam);

		List<ServiceClassParam> serviceClassParamList = new ArrayList<ServiceClassParam>();
		List<String> serviceClassNameList = new ArrayList<String>();
		for( ServiceClassParam serviceClassParam : rtcServiceClasses ) {
			String checkKey = serviceClassParam.getName() + "::" + serviceClassParam.getIdlPath();
			if( !serviceClassNameList.contains(checkKey) ) {
				serviceClassNameList.add(checkKey);
				serviceClassParamList.add(serviceClassParam);
			}
		}
		rtcParam.getServiceClassParams().clear();

		for( ServiceClassParam param : serviceClassParamList ) {
			param.setParent(rtcParam);
			rtcParam.getServiceClassParams().add(param);
		}
		for (String key : generateManagerList.keySet()) {
			GenerateManager manager = generateManagerList.get(key);
			if (!"Common".equals(manager.getManagerKey())
					&& !rtcParam.getLangList().contains(
							manager.getManagerKey())) {
				continue;
			}
			result.addAll(manager.generateTemplateCode(rtcParam));
		}

		return result;
	}

	/**
	 * バリデートを行う
	 *
	 * @param generatorParam
	 */
	public void validate(RtcParam rtcParam) {

		if( rtcParam.getOutputProject() == null ) {
			throw new RuntimeException(IRTCBMessageConstants.VALIDATE_ERROR_OUTPUTPROJECT);
		}
		/////Module
		//Name
		if( rtcParam.getName() == null || rtcParam.getName().length()==0 ) {
			throw new RuntimeException(IRTCBMessageConstants.VALIDATE_ERROR_COMPONENTNAME);
		}
		if( !StringUtil.checkDigitAlphabet(rtcParam.getName()) ) {
			throw new RuntimeException(IMessageConstants.BASIC_VALIDATE_NAME2);
		}
		//Category
		if( rtcParam.getCategory()==null || rtcParam.getCategory().length() == 0) {
			throw new RuntimeException(IMessageConstants.BASIC_VALIDATE_CATEGORY);
		}
		/////DataPort
		List<String> portNames = new ArrayList<String>();
		for( DataPortParam inport : rtcParam.getInports() ) {
			String result = ValidationUtil.validateDataPort(inport);
			if( result!=null ) 	throw new RuntimeException(result + " : " + rtcParam.getName());
			if (portNames.contains(inport.getName()))
				throw new RuntimeException(IRTCBMessageConstants.VALIDATE_ERROR_PORTSAMENAME + rtcParam.getName());
			portNames.add(inport.getName());
		}
		for (DataPortParam outport : rtcParam.getOutports()) {
			String result = ValidationUtil.validateDataPort(outport);
			if( result!=null ) 	throw new RuntimeException(result + " : " + rtcParam.getName());
			if( portNames.contains(outport.getName()) )
				throw new RuntimeException(IRTCBMessageConstants.VALIDATE_ERROR_PORTSAMENAME + rtcParam.getName());
			portNames.add(outport.getName());
		}
		/////Service Port
		List<String> servicePortNames = new ArrayList<String>();
		for( ServicePortParam servicePort : rtcParam.getServicePorts() ) {
			String result = ValidationUtil.validateServicePort(servicePort);
			if( result!=null ) 	throw new RuntimeException(result + " : " + rtcParam.getName());
			if( servicePortNames.contains(servicePort.getName()) )
				throw new RuntimeException(IRTCBMessageConstants.VALIDATE_ERROR_INTERFACESAMENAME + rtcParam.getName());
			servicePortNames.add(servicePort.getName());
		}
		/////Service Interface
		List<String> serviceInterfaceNames = new ArrayList<String>();
		for( ServicePortParam servicePort : rtcParam.getServicePorts() ) {
			for( ServicePortInterfaceParam serviceInterface : servicePort.getServicePortInterfaces() ) {
				String result = ValidationUtil.validateServiceInterface(serviceInterface, rtcParam.getOutputProject());
				if( result!=null ) 	throw new RuntimeException(result + " : " + rtcParam.getName());
				if (serviceInterfaceNames.contains(serviceInterface.getTmplVarName()))
					throw new RuntimeException(IRTCBMessageConstants.VALIDATE_ERROR_INTERFACESAMENAME + rtcParam.getName());
				serviceInterfaceNames.add(serviceInterface.getTmplVarName());
			}
		}
		/////ConfigurationSet
		List<String> configNames = new ArrayList<String>();
		for( ConfigSetParam config : rtcParam.getConfigParams() ) {
			String result = ValidationUtil.validateConfigurationSet(config);
			if( result!=null ) 	throw new RuntimeException(result + " : " + rtcParam.getName());
			if (configNames.contains(config.getName()))
				throw new RuntimeException(IMessageConstants.CONFIGURATION_VALIDATE_DUPLICATE + rtcParam.getName());
			configNames.add(config.getName());
		}
		/////FSM
		PropertyParam fsm = rtcParam.getProperty(IRtcBuilderConstants.PROP_TYPE_FSM);
		if(fsm!=null) {
			if(Boolean.valueOf(fsm.getValue())) {
				PropertyParam fsmType = rtcParam.getProperty(IRtcBuilderConstants.PROP_TYPE_FSMTYTPE);
				if(fsmType==null) {
					throw new RuntimeException(Messages.getString("IMC.FSM_NOT_SELECTED") + rtcParam.getName());
				} else {
					String strType = fsmType.getValue();
					if(!(strType.equals(IRtcBuilderConstants.FSMTYTPE_STATIC) || strType.equals(IRtcBuilderConstants.FSMTYTPE_DYNAMIC))) {
						throw new RuntimeException(Messages.getString("IMC.FSM_TYPE_INVALID") + rtcParam.getName());
					}
				}
				
				StateParam fsmParam = rtcParam.getFsmParam();
				if(fsmParam==null) {
					throw new RuntimeException(Messages.getString("IMC.FSM_NO_SM") + rtcParam.getName());
				} else {
					List<String> stateList = new ArrayList<String>();
					stateList.add(fsmParam.getName());
					for(StateParam param : fsmParam.getAllStateList() ) {
						if(stateList.contains(param.getName())) {
							throw new RuntimeException(Messages.getString("IMC.STATE_DUPL1") + param.getName() + Messages.getString("IMC.STATE_DUPL2") + rtcParam.getName());
						} else {
							stateList.add(param.getName());
						}
					}
				}
			}
		}
	}

	/**
	 * 参照されているServiceが存在するか確認する
	 *
	 * @param rtcServiceClasses
	 * @param generatorParam
	 * @return
	 */
	private void checkReferencedServiceParam(List<ServiceClassParam> rtcServiceClasses, RtcParam param) {

		List<String> serviceTypes = new ArrayList<String>();
		for( ServicePortParam serviceport : param.getServicePorts() ) {
			for( ServicePortInterfaceParam serviceInterfaces : serviceport.getServicePortInterfaces() ) {
				if( !serviceTypes.contains(serviceInterfaces.getInterfaceType()) )
					serviceTypes.add(serviceInterfaces.getInterfaceType());
			}
		}

		for( String serviceType : serviceTypes ) {
			ServiceClassParam find = null;
			for( ServiceClassParam serviceClassParam : rtcServiceClasses ) {
				if( serviceType.equals(serviceClassParam.getName()) ) {
					find = serviceClassParam;
					break;
				}
			}
			if( find == null )
				throw new RuntimeException("'" + serviceType + "' is not found in IDL");
		}
	}

	/**
	 * サービスクラス,型定義を取得する
	 *
	 * @param generatorParam
	 * @param IDLPathes
	 * @return
	 * @throws ParseException
	 */
	private List<ServiceClassParam> getRtcServiceClass(RtcParam rtcParam,
			List<ServiceClassParam> IDLPathes, List<IdlPathParam> idlDir, List<DataTypeParam> dataList) throws ParseException, HeaderException {
		List<ServiceClassParam> result = new ArrayList<ServiceClassParam>();
		List<String> includeFiles = new ArrayList<String>();

		String lineSeparator = System.getProperty("line.separator");
		for (int intIdx = 0; intIdx < IDLPathes.size(); intIdx++) {
			ServiceClassParam sv = IDLPathes.get(intIdx);
			if (sv == null) continue;
			List<String> incs = new ArrayList<String>();
			String idl = null;
			try {
				String idlContent = FileUtil.readFile(sv.getName());
				if (idlContent == null) continue;
				
				String[] eachLines = idlContent.split(lineSeparator);
				StringBuffer tmpBuf= new StringBuffer();
				for(String eachLine : eachLines) {
					tmpBuf.append(eachLine.trim()  + lineSeparator);
			    }
				idlContent = tmpBuf.toString();
				
				List<String> pathList = new ArrayList<String>();
				for(IdlPathParam path : rtcParam.getIdlSearchPathList()) {
					pathList.add(path.getPath());
				}
				if(idlDir!=null) {
					for(IdlPathParam each : idlDir) {
						pathList.add(each.getPath());
					}
				}
				idl = PreProcessor.parse(idlContent, pathList, incs, false);
			} catch (IOException e) {
				continue;
			}
			IDLParser parser = new IDLParser(new StringReader(idl));

			specification spec = parser.specification();

			List<ServiceClassParam> serviceClassParams = IDLParamConverter.convert(spec, sv.getName());
			if(IDLParamConverter.checkSuperInterface(serviceClassParams, false)==false) {
				warningMessage = "No parent interface definition found. Please check the IDL and included IDL files. ";
			}
			List<TypeDefParam> typedefParams = IDLParamConverter.convert_typedef(spec, sv.getName());
			for(TypeDefParam param : typedefParams) {
				String defFull = "";
				if( 0<param.getModuleName().length() ) {
					defFull = param.getModuleName() + "::" + param.getTargetDef();
				} else {
					defFull = param.getTargetDef();
				}
				for(DataTypeParam dataParam : dataList) {
					if(dataParam.isDefault()==false) continue;
					if(dataParam.getDefinedTypes().contains(defFull)) {
						param.setDefault(true);
						break;
					}
				}
			}
			if (typedefParams.size() > 0) {
				serviceClassParams = convertType(serviceClassParams, typedefParams);
			}
			for (ServiceClassParam scp : serviceClassParams) {
				scp.setTypeDef(typedefParams);
			}
			result.addAll(serviceClassParams);
			//
			for (IdlFileParam p : rtcParam.getProviderIdlPathes()) {
				if (sv.getName().trim().equals(p.getIdlPath().trim())) {
					for (String s : incs) {
						if (!p.getIncludeIdlPathes().contains(s)) {
							p.getIncludeIdlPathes().add(s);
						}
					}
				}
			}
			for (IdlFileParam p : rtcParam.getConsumerIdlPathes()) {
				if (sv.getName().trim().equals(p.getIdlPath().trim())) {
					for (String s : incs) {
						if (!p.getIncludeIdlPathes().contains(s)) {
							p.getIncludeIdlPathes().add(s);
						}
					}
				}
			}
			//
			for (String s : incs) {
				if (!includeFiles.contains(s)) {
					includeFiles.add(s);
				}
			}
		}
		//
		for(String target : includeFiles) {
			if( !rtcParam.getIncludedIdls().contains(target) ) {
				rtcParam.getIncludedIdls().add(target);
			}
		}

		return result;
	}

	private List<ServiceClassParam> convertType(List<ServiceClassParam> source, List<TypeDefParam> types) {

		for(int idxParent=0;idxParent<types.size();idxParent++) {
			TypeDefParam cur = types.get(idxParent);
			String target = types.get(idxParent).getOriginalDef();
			//// for struct...
			if (cur.isStruct()){
			   for(String chld : cur.getChildType()){
				    TypeDefParam tdp2 = findTypeDefParam(chld, types);
				    if(tdp2 != null){

							if(!cur.isSequence()){ cur.setSequence(tdp2.isSequence()); }
							if(!cur.isUnbounded()) {
						 	 	cur.setUnbounded( isUnboundedTypeDef(chld, types));

							}
					  }
				 }
			}
			/// for typedef
			if (cur.isAlias()){
				TypeDefParam tdp = findTypeDefParam(target , types) ;
				if(tdp != null){
					////  Copy Attributs
					if(!cur.isSequence()){ cur.setSequence(tdp.isSequence()); }
					if(!cur.isUnbounded()) {
						 cur.setUnbounded( isUnboundedTypeDef(cur.getTargetDef(), types));
					}
					cur.setStruct(tdp.isStruct());
					cur.setInterface(tdp.isInterface());
					cur.setOriginalDef(tdp.getTargetDef());
				}

			}
		}

		for( ServiceClassParam target : source) {
			for( ServiceMethodParam method : target.getMethods() ) {
				checkMethodType(method, types);
				for( ServiceArgumentParam param : method.getArguments() ) {
					checkArgumentType(param, types);
				}
			}
		}
		return source;
	}

	private TypeDefParam findTypeDefParam(String name, List<TypeDefParam> types) {
		for(int index=0;index<types.size();index++) {
			TypeDefParam tdp = types.get(index);
			if( name.equals(tdp.getTargetDef()) ) {
					return tdp;
			}
		}
		return null;
	}

	private boolean isUnboundedTypeDef(String name, List<TypeDefParam> types) {
		  TypeDefParam cur = findTypeDefParam(name, types);

		  if(cur != null){
				if(cur.isUnbounded() || cur.isSequence()) {
					return true;

				}else{
		  			if (cur.isAlias()){
						return  isUnboundedTypeDef(cur.getOriginalDef(), types);

					}
		  			if (cur.isStruct()){
			   			for(String chld : cur.getChildType()){
							if( isUnboundedTypeDef(chld, types) ){ return true; }
						}
				 	}
				}
			}
			return false;
	}

	private void checkMethodType(ServiceMethodParam target, List<TypeDefParam> types) {
		String targetFull = target.getModule() + target.getType();
		String targetType = target.getType();
		//
		for(TypeDefParam tdparam : types) {
			String defFull = "";
			if( 0<tdparam.getModuleName().length() ) {
				defFull = tdparam.getModuleName() + "::" + tdparam.getTargetDef();
			} else {
				defFull = tdparam.getTargetDef();
			}
			boolean isHit = false;
			if(tdparam.isDefault()) {
				if(targetType.equals(defFull) || targetFull.equals(defFull)) {
					isHit = true;
				}
			} else {
				if(targetFull.equals(defFull)) {
					isHit = true;
				}
			}
			if(isHit) {
				target.setSequence(tdparam.isSequence());
				target.setArray(tdparam.isArray());
				target.setArrayDim(tdparam.getArrayDim());
				target.setStruct(tdparam.isStruct());
				target.setOriginalType(tdparam.getOriginalDef());
				target.setUnbounded(tdparam.isUnbounded());
				target.setAlias(tdparam.isAlias());
				target.setInterface(tdparam.isInterface());
				target.setDefault(tdparam.isDefault());
				break;
			}
		}
		target.setType(checkType(target.getType(), types));
	}
	private void checkArgumentType(ServiceArgumentParam target, List<TypeDefParam> types) {
		String targetFull = target.getModule() + target.getType();
		for(TypeDefParam tdparam : types) {
			String defFull = "";
			if( 0<tdparam.getModuleName().length() ) {
				defFull = tdparam.getModuleName() + "::" + tdparam.getTargetDef();
			} else {
				defFull = tdparam.getTargetDef();
			}
			if(targetFull.equals(defFull) || target.getType().equals(defFull)) {
				if(target.getType().equals(defFull)) {
					target.setModule("");
				}
				target.setOriginalType(tdparam.getOriginalDef());
				target.setUnbounded(tdparam.isSequence() || tdparam.isUnbounded());
				target.setArray(tdparam.isArray());
				target.setArrayDim(tdparam.getArrayDim());
				target.setStruct(tdparam.isStruct());
				target.setInterface(tdparam.isInterface());
				target.setAlias(tdparam.isAlias());
				target.setSequence(tdparam.isSequence());
				target.setType(checkType(target.getType(), types));
				return;
			}
		}
	}
	private String checkType(String target, List<TypeDefParam> types) {
		for(TypeDefParam tdparam : types) {
			if(target.equals(tdparam.getTargetDef())) {
				if(!tdparam.getScopedName().equals("")) {
					if(tdparam.isString()) return tdparam.getScopedName() + "::" + tdparam.getOriginalDef();
					return tdparam.getScopedName() + "::" + target;
				}
				if( tdparam.isString() && !tdparam.isStruct() ) return tdparam.getOriginalDef();
			}
		}
		return target;
	}

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	private void writeFile(List<GeneratedResult> generatedResultList,
			RtcParam rtcParam, String genTime, MergeHandler handler) throws IOException, CoreException {

		IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceHandle.getProject(rtcParam.getOutputProject());
		if(!project.exists()) {
			return;
		}

		for (GeneratedResult generatedResult : generatedResultList) {
			if (generatedResult.getName().equals("") == false) {
				writeFile(generatedResult, project, handler, genTime);
			}
		}	
		for( IdlFileParam idlFile : rtcParam.getProviderIdlPathes() ) {
			if(RTCUtil.checkDefault(idlFile.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			IFile idlTarget = project.getFile("idl" + File.separator + idlFile.getIdlFile());
			if( !idlTarget.getLocation().toOSString().equals(idlFile.getIdlPath()) )  {
				idlTarget.delete(true, null);
				idlTarget.create(new FileInputStream(idlFile.getIdlPath()), true, null);
			}
		}
		for( IdlFileParam idlFile : rtcParam.getConsumerIdlPathes() ) {
			if(RTCUtil.checkDefault(idlFile.getIdlPath(), rtcParam.getParent().getDataTypeParams())) continue;
			IFile idlTarget = project.getFile("idl" + File.separator + idlFile.getIdlFile());
			if( !idlTarget.getLocation().toOSString().equals(idlFile.getIdlPath()) )  {
				idlTarget.delete(true, null);
				idlTarget.create(new FileInputStream(idlFile.getIdlPath()), true, null);
			}
		}
		//
		for( String includedIdlFile : rtcParam.getIncludedIdls() ) {
			if(RTCUtil.checkDefault(includedIdlFile, rtcParam.getParent().getDataTypeParams())) continue;
			File target = new File(includedIdlFile);
			IFile idlTarget = project.getFile("idl" + File.separator + target.getName());
			if( !idlTarget.getLocation().toOSString().equals(includedIdlFile) )  {
				idlTarget.delete(true, null);
				idlTarget.create(new FileInputStream(includedIdlFile), true, null);
			}
		}
		//
		//アイコン、ビットマップのコピー
		copyFigure("icons/rt_middleware_logo.ico", project, "cmake/rt_middleware_logo.ico");
		copyFigure("icons/rt_middleware_banner.bmp", project, "cmake/rt_middleware_banner.bmp");
		copyFigure("icons/rt_middleware_dlg.bmp", project, "cmake/rt_middleware_dlg.bmp");
	}

	private void copyFigure(String source, IProject outputProject, String dist) {
		try {
			URL bundleUrl = this.getClass().getClassLoader().getResource(source);
			URL jarUrl = org.eclipse.core.runtime.FileLocator.toFileURL(bundleUrl);
			URI uri = URI.create(jarUrl.toString().replace(" ", "%20").replace(File.separator, "/"));

			File targetFile = new File(outputProject.getLocation().toOSString(), dist);

			Files.copy(Paths.get(uri), Paths.get(targetFile.toURI()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(GeneratedResult generatedResult, IProject outputProject,
			MergeHandler handler, String genTime) throws IOException {

		File targetFile = new File(outputProject.getLocation().toOSString(), generatedResult.getName());

		boolean isOutput = false;
		if (targetFile.exists()) {
			String originalFileContents = FileUtil.readFile(targetFile.getAbsolutePath());
			if (StringUtil.removeNewLine(originalFileContents).equals(
					StringUtil.removeNewLine(generatedResult.getCode())) == false) {
				int selectedProcess = handler.getSelectedProcess(generatedResult, originalFileContents);
				if (selectedProcess == MergeHandler.PROCESS_GENERATE_ID || selectedProcess == MergeHandler.PROCESS_MERGE_ID) {
					if(ComponentPreferenceManager.getInstance().getNot_Generated_Caution() == false) {
						GeneratedCautionDialog confirmDialog = new GeneratedCautionDialog();
						confirmDialog.setTargetFile(generatedResult.getName() + genTime);
						int ret = confirmDialog.open();
						if(ret == MessageDialog.CANCEL) {	
							writeFile(generatedResult, outputProject, handler, genTime);
							return;
						}
						
						if(confirmDialog.getSelected()) {
							ComponentPreferenceManager.getInstance().setNot_Generated_Caution(true);
						}
					}
					
					IResource originalFile = outputProject.findMember(generatedResult.getName());
					IFile renameFile = outputProject.getFile(generatedResult.getName() + genTime);
					try {
						originalFile.move(renameFile.getFullPath(), true, null);

						if (selectedProcess == MergeHandler.PROCESS_MERGE_ID) {
							generatedResult.setCode(MergeBlockParser.merge(
									originalFileContents, MergeBlockParser
											.parse(generatedResult.getCode())));
						}
						isOutput = true;
					} catch (NullPointerException e) {
						LOGGER.error("Fail to move resource (null)", e);
					} catch (CoreException e) {
						LOGGER.error("Fail to move resource (move)", e);
					}
					//バックアップファイルの整理
					FileUtil.removeBackupFiles(outputProject.getLocation().toOSString(), generatedResult.getName());
				}
			}
		} else {
			isOutput = true;
		}

		if (isOutput) {
			IFile outputFile = outputProject.getFile(generatedResult.getName());
			IPath relPath = outputFile.getProjectRelativePath();
			if( relPath.segmentCount() > 1 ) {
				String[] segs = relPath.segments();
				StringBuilder builder = new StringBuilder();
				for(int index=0;index<relPath.segmentCount()-1;index++) {
					builder.append(segs[index] + File.separator);
					IFolder folder = outputProject.getFolder(builder.toString());
					if(!folder.exists()) {
						try {
							folder.create(false, true, null);
						} catch (CoreException e) {
							LOGGER.error("Fail to create folder", e);
						}
					}
				}
			}
			try {
				if(generatedResult.getEncode().length()==0) {
					String strFullPath = outputFile.getLocation().toOSString();
					try( FileOutputStream fos = new FileOutputStream(strFullPath) ) {
						if(generatedResult.isNotBom()==false) {
							fos.write(0xef); fos.write(0xbb); fos.write(0xbf);
						}
						OutputStreamWriter osw = new OutputStreamWriter( fos , "UTF-8");
						BufferedWriter fp = new BufferedWriter( osw );
						fp.write ( generatedResult.getCode());
						fp.flush();
					}
				} else {
					outputFile.create(new ByteArrayInputStream(generatedResult.getCode().getBytes(generatedResult.getEncode())), false, null);
				}
			} catch (Exception e) {
				LOGGER.error("Fail to create file", e);
			}
		}
	}

	/**
	 * ジェネレートし、ファイル出力を行う
	 *
	 * @param generatorParam
	 *            パラメータ
	 * @param handler
	 *            MergeHandler
	 * @throws ParseException
	 * @throws IOException
	 */
	public void doGenerateWrite(GeneratorParam generatorParam, List<IdlPathParam> idlDirs, String genTime,
			MergeHandler handler) throws Exception {
		warningMessage = "";
		RtcParam rtcParam =  generatorParam.getRtcParam();
		validate(rtcParam);
		List<GeneratedResult> generatedResult = generateTemplateCode(generatorParam, idlDirs);
		writeFile(generatedResult, rtcParam, genTime, handler);
	}

	/**
	 * マージハンドラ
	 */
	public interface MergeHandler {
		/**
		 * プロセス：オリジナルを残す
		 */
		public static final int PROCESS_ORIGINAL_ID = 10;

		/**
		 * プロセス：新しく生成したものを利用する
		 */
		public static final int PROCESS_GENERATE_ID = 20;

		/**
		 * プロセス：マージを行う
		 */
		public static final int PROCESS_MERGE_ID = 30;

		/**
		 * プロセスを選択する
		 *
		 * @param generatedResult
		 *            生成結果
		 * @param originalFileContents
		 *            既存ファイルの内容
		 * @return
		 */
		public int getSelectedProcess(GeneratedResult generatedResult,
				String originalFileContents);
	}
}
