package jp.go.aist.rtm.rtcbuilder.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ComponentPreferenceManager;

/**
 * ユーティリティクラス
 */
public class FileUtil {

	/**
	 * ダイアログによって、ディレクトリのパスを取得する
	 *
	 * @param defaultValue
	 *            デフォルト値
	 * @return ディレクトリのパス
	 */
	public static IPath getDirectoryPathByDialog(IPath defaultPath) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		Shell shell = window.getShell();
		DirectoryDialog dialog = new DirectoryDialog(shell, SWT.OPEN);

		if (defaultPath != null) {
			dialog.setFilterPath(defaultPath.toOSString());
		}
		String pathString = dialog.open();
		if (pathString == null) return null;

		IPath result = new Path(pathString + File.separator + IRtcBuilderConstants.DEFAULT_RTC_XML);

		return result;
	}

	/**
	 * ダイアログによって、IDLファイルのパスを取得する
	 *
	 * @param defaultValue
	 *            デフォルト値
	 * @return IDLファイルのパス
	 */
	public static IPath getFilePathByDialog(IFile defaultFile, int style) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		Shell shell = window.getShell();
		FileDialog dialog = new FileDialog(shell, style);

		if (defaultFile != null) {
			dialog.setFileName(defaultFile.toString());
			dialog.setFilterNames(new String[] { "RTCビルダ設定ファイル" });
			dialog.setFilterExtensions(new String[] { "*.xml" });
		}

		String pathString = dialog.open();

		if (pathString == null) {
			return null;
		}

		IPath result = new Path(pathString);

		return result;
	}

	/**
	 * ファイルを読み込み、内容を返す
	 *
	 * @param path
	 *            パス
	 * @return ファイル内容
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException {
		StringBuffer result = null;
		try( FileInputStream fis = new FileInputStream(path);
             InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			 BufferedReader br = new BufferedReader(isr) ) {
			result = new StringBuffer();
            String ch;
            String sep = System.getProperty("line.separator");
            boolean firstLine = true;
            while ((ch = br.readLine() ) != null) {
            	if (firstLine) {
            		ch = removeUTF8BOM(ch);
                    firstLine = false;
                }
            	result.append(ch + sep);
            }
		} catch (IOException e) {
			throw new IOException(Messages.getString("IRTCBMessageConstants.ERROR_PROFILE_RESTORE_P1") + " path:" + path);  //$NON-NLS-1$
		}

		return result == null ? null : result.toString();
	}

	private static String removeUTF8BOM(String s) {
		String UTF8_BOM = "\uFEFF";
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }

	/**
	 * バックアップファイルの整理を行う
	 *
	 * @param project 対象パス名
	 * @param targetFile 対象ファイル名
	 */
	public static void removeBackupFiles(String targetPath, String targetFile) {
		String targetRealFile = targetPath;
		//
		if(targetFile.contains("\\")) {
			//ファイル名にパスが含まれる場合
			String paths[] = targetFile.split("\\\\");

			for(int index=0;index<paths.length;index++) {
				targetRealFile = targetRealFile + File.separator + paths[index];
				if(index<paths.length-1) {
					targetPath = targetPath + File.separator + paths[index];
				}
			}

			File dir = new File(targetPath);
			File[] files = dir.listFiles();
			List<String> targets = new ArrayList<String>();
			for(File target : files) {
				if( target.getPath().startsWith(targetRealFile) ) {
					targets.add(target.getPath());
				}
			}
			if(ComponentPreferenceManager.getInstance().getBackup_Num() < targets.size()) {
				Collections.sort(targets);
				for(int index=0;index<targets.size()-ComponentPreferenceManager.getInstance().getBackup_Num();index++) {
					File remTarget = new File(targets.get(index));
					remTarget.delete();
				}
			}
		} else {
			//ファイル名のみの場合
			File dir = new File(targetPath);
			File[] files = dir.listFiles();
			List<String> targets = new ArrayList<String>();
			for(File target : files) {
				if( target.getName().startsWith(targetFile) ) {
					targets.add(target.getName());
				}
			}
			if(ComponentPreferenceManager.getInstance().getBackup_Num() < targets.size()) {
				Collections.sort(targets);
				for(int index=0;index<targets.size()-ComponentPreferenceManager.getInstance().getBackup_Num();index++) {
					File remTarget = new File(targetPath + File.separator + targets.get(index));
					remTarget.delete();
				}
			}
		}
	}

    public static boolean fileCompare(String fileA, File fileB) {
        boolean result = false;
        try {
            if( new File(fileA).length() != fileB.length() ){
                return result;
            }
            byte[] byteA = Files.readAllBytes(Paths.get(fileA));
            byte[] byteB = Files.readAllBytes(fileB.toPath());
            result = Arrays.equals(byteA, byteB);
        } catch (IOException e) {
        }
        return result;
    }
    
	public static List<File> listAllFiles(File dir) {
	    List<File> result = new ArrayList<>();
	    File[] files = dir.listFiles();
	    if (files != null) {
	        for (File each : files) {
	        	if(each.isFile()) {
	        		result.add(each);
	        	}
	            if (each.isDirectory()) {
	                result.addAll(listAllFiles(each));
	            }
	        }
	    }
	    return result;
	}


}
