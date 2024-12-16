package jp.go.aist.rtm.rtcbuilder.ui.compare;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class GeneratedCautionDialog extends MessageDialog {
	private String targetFile;
	private Button confirmCheckbox;
	private boolean selected;

	public void setTargetFile(String targetFile) {
		this.targetFile = targetFile;
	}
	public boolean getSelected() {
		return selected;
	}

	public GeneratedCautionDialog() {
        super(null, "", null, "",
        		MessageDialog.QUESTION,
    			new String[] { IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL },
    			1);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
    	this.message = Messages.getString("IMC.GEN_CAUTION_MSG1") + System.getProperty("line.separator")
    					+ Messages.getString("IMC.GEN_CAUTION_MSG2") +"\"" + targetFile + "\"" + Messages.getString("IMC.GEN_CAUTION_MSG3");
		//
    	GridLayout gridLayout = new GridLayout();

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gridLayout);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		confirmCheckbox = new Button(parent, SWT.CHECK);
		confirmCheckbox.setText(Messages.getString("IMC.GEN_CAUTION_CHECK"));
		GridData labelLayloutData = new GridData(GridData.HORIZONTAL_ALIGN_END);
		confirmCheckbox.setLayoutData(labelLayloutData);

		return mainComposite;
    }
	@Override
	protected void buttonPressed(int buttonId) {
		if(buttonId == OK) {
			this.selected = confirmCheckbox.getSelection();
		}
		super.buttonPressed(buttonId);
	}

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
		shell.setText("Confirm");
    }
}
