package jp.go.aist.rtm.systemeditor.ui.editor;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.FileEditorInput;
import org.openrtp.namespaces.rts.version02.RtsProfileExt;

import jp.go.aist.rtm.systemeditor.extension.LoadProfileExtension;
import jp.go.aist.rtm.systemeditor.factory.ProfileLoader;
import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagramKind;
import jp.go.aist.rtm.toolscommon.util.RtsProfileHandler;

/**
 * SystemDiagramEditorクラス
 */
public class OfflineSystemDiagramEditor extends AbstractSystemDiagramEditor {

	static final String MSG_PROGRESS_BEGIN = Messages
			.getString("OfflineSystemDiagramEditor.2");
	static final String MSG_PROGRESS_SUB = Messages
			.getString("OfflineSystemDiagramEditor.3");
	static final String MSG_FAIL_LOAD = Messages
			.getString("OfflineSystemDiagramEditor.5");
	static final String MSG_FAIL_OPEN = Messages
			.getString("OfflineSystemDiagramEditor.6");

	/**
	 * システムダイアグラムエディタのID
	 */
	public static final String OFFLINE_SYSTEM_DIAGRAM_EDITOR_ID = "jp.go.aist.rtm.systemeditor.ui.editor.OfflineSystemDiagramEditor"; //$NON-NLS-1$

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		IEditorInput newInput = new NullEditorInput();
		super.init(site, newInput);
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		getSystemDiagram().setKind(SystemDiagramKind.OFFLINE_LITERAL);
	}

	protected IEditorInput load(IEditorInput input, final IEditorSite site)
			throws PartInitException {
		
		IEditorInput targetInput = getTargetInput(input, "Offline System Editor");

		if (targetInput instanceof FileEditorInput)	 {
			// RTSプロファイルをファイルからロードする
			doLoad(site, (FileEditorInput)targetInput);
		}

		postLoad();

		return targetInput;
	}

	private void doLoad(final IEditorSite site, FileEditorInput editorInput)
			throws PartInitException {
		String strPath = editorInput.getPath().toOSString();
		doLoad(site.getShell(), strPath);
	}

	public void doLoad(String path) throws PartInitException {
		doLoad(getEditorSite().getShell(), path);
	}

	public void doLoad(Shell shell, String path) throws PartInitException {
		try {
			final String strPath = path;
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
			IRunnableWithProgress runable = new IRunnableWithProgress() {

				public void run(IProgressMonitor monitor)
						throws InvocationTargetException, InterruptedException {

					monitor.beginTask(MSG_PROGRESS_BEGIN, 100);
					monitor.subTask(MSG_PROGRESS_SUB);

					try {
						RtsProfileHandler handler = new RtsProfileHandler();

						// STEP1: ファイルからRTSプロファイルオブジェクトを作成
						monitor.internalWorked(20);

						RtsProfileExt profile = handler.load(strPath);

						// STEP2: 拡張ポイント (ダイアグラム生成前)
						monitor.internalWorked(20);

						ProfileLoader creator = new ProfileLoader();
						for (LoadProfileExtension.ErrorInfo info : creator
								.preLoad(profile, strPath)) {
							if (info.isError()) {
								openError(DIALOG_TITLE_ERROR, info.getMessage());
								return;
							} else {
								if (!openConfirm(DIALOG_TITLE_CONFIRM, info
										.getMessage())) {
									return;
								}
							}
						}

						// STEP3: RTSプロファイルオブジェクトからダイアグラムを作成
						monitor.internalWorked(20);

						handler.setBasePath(strPath);
						SystemDiagram diagram = handler.load(profile,
								SystemDiagramKind.OFFLINE_LITERAL);
						handler.restoreConfigSet(diagram);
						handler.restoreCompositeComponentPort(diagram);
						handler.restoreConnection(diagram);
						handler.restoreExecutionContext(diagram);

						SystemDiagram oldDiagram = getSystemDiagram();
						setSystemDiagram(diagram);

						// STEP4: 拡張ポイント (ダイアグラム生成後)
						monitor.internalWorked(20);

						for (LoadProfileExtension.ErrorInfo info : creator
								.postLoad(diagram, profile, oldDiagram)) {
							if (info.isError()) {
								openError(DIALOG_TITLE_ERROR, info.getMessage());
								return;
							} else {
								if (!openConfirm(DIALOG_TITLE_CONFIRM, info
										.getMessage())) {
									return;
								}
							}
						}

					} catch (Exception e) {
						monitor.done();
						throw new InvocationTargetException(e, MSG_FAIL_LOAD
								+ e.getMessage());
					}
					monitor.done();
				}
			};
			dialog.run(false, false, runable);

		} catch (Exception e) {
			throw new PartInitException(MSG_FAIL_OPEN, e);
		}
	}

	@Override
	public String getEditorId() {
		return OFFLINE_SYSTEM_DIAGRAM_EDITOR_ID;
	}

	@Override
	public boolean isOnline() {
		return false;
	}

}
