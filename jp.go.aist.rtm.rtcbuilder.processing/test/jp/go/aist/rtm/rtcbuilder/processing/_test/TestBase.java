package jp.go.aist.rtm.rtcbuilder.processing._test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import junit.framework.TestCase;

public class TestBase extends TestCase {
	protected String rootPath;
	protected String expPath;
	protected String expContent;
	protected int index;
	protected String[] ignore_row_phrases = {"--service-idl=", "--consumer-idl"};
	protected final int default_file_num = 37;

	public TestBase () {
		File fileCurrent = new File(".");
		rootPath = fileCurrent.getAbsolutePath();
		rootPath = rootPath.substring(0,rootPath.length()-1);
	}
	protected String readFile(String fileName) {
		return readFile(fileName, "\r\n");
	}
	
	protected String readFile(String fileName, String separator) {
		StringBuffer stbRet = new StringBuffer();
		try (FileReader fr = new FileReader(fileName); BufferedReader br = new BufferedReader(fr) ) {
			String str = new String();
			while( (str = br.readLine()) != null ){
				boolean isIgnore = false;
				for(int index=0;index<ignore_row_phrases.length;index++) {
					if(str.length()==0 || str.contains(ignore_row_phrases[index])) {
						isIgnore = true;
						break;
					}
				}
				if(!isIgnore) stbRet.append(str + separator);
			}
		} catch (IOException e){
			e.printStackTrace();
		}
		return stbRet.toString();
	}

	protected int getFileIndex(String targetName, List<GeneratedResult> targetList) {
		int resultindex = -1;

		for( int intIdx=0; intIdx<targetList.size(); intIdx++ ) {
			if( targetList.get(intIdx).getName().equals(targetName) ) {
				return intIdx;
			}
		}
		return resultindex;
	}

	protected String getGeneratedString(String source) {
		return getGeneratedString(source, System.getProperty( "line.separator" ));
	}
	protected String getGeneratedString(String source, String sep) {
		String[] target = source.split(sep);
		StringBuffer stbRet = new StringBuffer();

		for( int index=0; index<target.length; index++ ) {
			boolean isIgnore = false;
			for(int indexi=0;indexi<ignore_row_phrases.length;indexi++) {
				if(target[index].length()==0 || target[index].contains(ignore_row_phrases[indexi])) {
					isIgnore = true;
					break;
				}
			}
			if(!isIgnore) stbRet.append(target[index] + sep);
		}
		return stbRet.toString();
	}

	protected String replaceRootPath(String content) {
		String result = content.replace("__ROOT_PATH__", rootPath);
		String origPath = "C:\\Tech-Arts\\EclipseRTM\\jp.go.aist.rtm.rtcbuilder.java\\";
		result = result.replace(origPath, rootPath);
		return result;
	}

	protected void checkCode(List<GeneratedResult> result, String resourceDir, String fileName) {
		index = getFileIndex(fileName, result);
		expPath = resourceDir + fileName;
		expContent = readFile(expPath);
		expContent = replaceRootPath(expContent);
		assertEquals(expContent,
				getGeneratedString(result.get(index).getCode(), "\r\n"));
	}
	protected void checkCode(List<GeneratedResult> result, String resourceDir, String fileName, String separator) {
		index = getFileIndex(fileName, result);
		expPath = resourceDir + fileName;
		expContent = readFile(expPath, separator);
		expContent = replaceRootPath(expContent);
		assertEquals(expContent,
				getGeneratedString(result.get(index).getCode(), separator));
	}

}
