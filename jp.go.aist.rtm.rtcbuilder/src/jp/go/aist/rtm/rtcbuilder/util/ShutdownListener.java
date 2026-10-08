package jp.go.aist.rtm.rtcbuilder.util;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.internal.WorkbenchMessages;

import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.ui.editors.RtcBuilderEditor;

public class ShutdownListener implements IWorkbenchListener {
	private final int RETURN_YES = 0;
	private final int RETURN_NO = 1;
	private final int RETURN_CANCEL = 2;

	@Override
	public void postShutdown(IWorkbench workbench) {
	}

	@Override
	public boolean preShutdown(IWorkbench workbench, boolean forced) {
		if( workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor() instanceof RtcBuilderEditor ) {
			RtcBuilderEditor editor = (RtcBuilderEditor)workbench.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if( editor.isDirty() ) {
				String[] buttons = new String[] { IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL, IDialogConstants.CANCEL_LABEL };
				MessageDialog dialog = new MessageDialog(workbench.getDisplay().getActiveShell(), WorkbenchMessages.Save_Resource,
					null, IMessageConstants.SAVE_MESSAGE, MessageDialog.QUESTION, buttons, 0);
				int result = dialog.open();
				switch (result) {
					case RETURN_YES:
						editor.doSave(new NullProgressMonitor());	
						break;
					case RETURN_NO: // no
						editor.setDirty(false);
						break;
					case RETURN_CANCEL: // cancel
						return false;
				}
			}
			return RtcBuilderPlugin.getDefault().isCanExit();
		} else {
			return true;
		}
	}
}
