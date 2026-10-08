package jp.go.aist.rtm.rtcbuilder.ros.python._test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import jp.go.aist.rtm.rtcbuilder.generator.GeneratedResult;
import junit.framework.TestCase;

public class TestBase extends TestCase {
//	protected String rootPath = "C:\\Tech-Arts\\Eclipse\\jp.go.aist.rtm.rtcbuilder\\";
	protected String rootPath;
	protected String expPath;
	protected String expContent;
	protected int index;
	protected String[] ignore_row_phrases = {"--service-idl=", "--consumer-idl"};
	protected final int default_file_num = 27;
	protected final int service_file_num = 7;

	public TestBase () {
		File fileCurrent = new File(".");
		rootPath = fileCurrent.getAbsolutePath();
		rootPath = rootPath.substring(0,rootPath.length()-1);
	}
	protected String readFile(String fileName) {
	    StringBuilder stbRet = new StringBuilder();
	    
	    try (FileInputStream fis = new FileInputStream(fileName);
	         InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
	         BufferedReader br = new BufferedReader(isr)) {
	        
	        String str;
	        while ((str = br.readLine()) != null) {
	            stbRet.append(str).append("\r\n");
	        }
	    } catch (IOException e) {
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
		String sep = System.getProperty( "line.separator" );
		return getGeneratedString(source, sep);
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
			if(!isIgnore) stbRet.append(target[index] + System.getProperty( "line.separator" ));
		}
		return stbRet.toString();
	}

	protected String replaceRootPath(String content) {
		String result = content.replace("__ROOT_PATH__", rootPath);
		String origPath = "C:\\Tech-Arts\\EclipseRTM\\jp.go.aist.rtm.rtcbuilder.python\\";
		result = result.replace(origPath, rootPath);
		return result;
	}

	protected void checkCode(List<GeneratedResult> result, String resourceDir, String fileName, String sep) {
		index = getFileIndex(fileName, result);
		expPath = resourceDir + fileName;
		expContent = readFile(expPath);
		expContent = replaceRootPath(expContent);
		assertEquals(expContent,
				getGeneratedString(result.get(index).getCode(), sep));
	}
	
	protected void checkCode(List<GeneratedResult> result, String resourceDir, String fileName) {
		index = getFileIndex(fileName, result);
		expPath = resourceDir + fileName;
		expContent = readFile(expPath);
		expContent = replaceRootPath(expContent);
		expContent = expContent.replace("\uFEFF", "");
		assertEquals(replaceBlank(expContent) , replaceBlank(result.get(index).getCode()));
//		assertEquals(expContent,
//				getGeneratedString(result.get(index).getCode()));
	}
	private String replaceBlank(String source) {
		String result;
		result = source.replaceAll("(\n|\r|\n\r|\r\n){2,}", "\n");
		result = result.replaceAll("[ \t\\x0B\f]+(\n|\r|\n\r|\r\n)", "");
		return result;
	}

}
