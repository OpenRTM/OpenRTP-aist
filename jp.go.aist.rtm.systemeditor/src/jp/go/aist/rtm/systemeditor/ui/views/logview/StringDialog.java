package jp.go.aist.rtm.systemeditor.ui.views.logview;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.views.logview.FilteringParam.FilteringKind;

public class StringDialog extends Dialog {
	private FilteringParam targetParam;
	private FilteringKind kind;
	
	private Text txtCond;
	private Button chkRegExp;
	
	public void setTargetParam(FilteringParam targetParam) {
		this.targetParam = targetParam;
	}
	
	public void setKind(FilteringKind kind) {
		this.kind = kind;
	}

	public StringDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.CENTER | SWT.RESIZE);
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout gridLayout = new GridLayout(1, true);

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gridLayout);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		String strName = "";
		switch(this.kind) {
		case MANAGER:
			strName = Messages.getString("LogView.columnManager");
			break;
		case IDENTIFIER:
			strName = Messages.getString("LogView.columnID");
			break;
		case PID:
			strName = Messages.getString("LogView.columnPid");
			break;
		case HOST:
			strName = Messages.getString("LogView.columnHost");
			break;
		case MESSAGE:
			strName = Messages.getString("LogView.columnMessage");
			break;
		default:
			break;
		}
		
		Group compGroup = new Group(mainComposite, SWT.NONE);
		compGroup.setLayout(new GridLayout(2, false));
		compGroup.setText(strName);
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		compGroup.setLayoutData(gd);
		
		txtCond = new Text(compGroup, SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		txtCond.setLayoutData(gd);
		
		chkRegExp = new Button(compGroup, SWT.CHECK);
		chkRegExp.setText(Messages.getString("LogView.regexp"));
		
		switch(kind) {
		case MANAGER:
			if(targetParam.getManagerCond()!=null) {
				txtCond.setText(targetParam.getManagerCond());
			}
			chkRegExp.setSelection(targetParam.isRegexpManager());
			break;
		case IDENTIFIER:
			if(targetParam.getIdentifierCond()!=null) {
				txtCond.setText(targetParam.getIdentifierCond());
			}
			chkRegExp.setSelection(targetParam.isRegexpIdentifier());
			break;
		case PID:
			if(targetParam.getPidCond()!=null) {
				txtCond.setText(targetParam.getPidCond());
			}
			chkRegExp.setSelection(targetParam.isRegexpPid());
			break;
		case HOST:
			if(targetParam.getHostCond()!=null) {
				txtCond.setText(targetParam.getHostCond());
			}
			chkRegExp.setSelection(targetParam.isRegexpHost());
			break;
		case MESSAGE:
			if(targetParam.getMessageCond()!=null) {
				txtCond.setText(targetParam.getMessageCond());
			}
			chkRegExp.setSelection(targetParam.isRegexpMessage());
			break;
		default:
			break;
		}
		
		return mainComposite;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Condition"); //$NON-NLS-1$
	}
	
	protected Point getInitialSize() {
		return new Point(500, 150);
	}
	@Override
	protected void okPressed() {
		if(chkRegExp.getSelection()) {
			String regEx = txtCond.getText();
			try {
			Pattern p = Pattern.compile(regEx);
			} catch(PatternSyntaxException ex) {
				MessageDialog.openWarning(getParentShell(),
						Messages.getString("LogView.regexp"),
						Messages.getString("LogView.regexpWarning") + System.lineSeparator() + ex.getMessage());
				return;
			}
		}
		switch(kind) {
		case MANAGER:
			targetParam.setManagerCond(txtCond.getText());
			targetParam.setRegexpManager(chkRegExp.getSelection());
			break;
		case IDENTIFIER:
			targetParam.setIdentifierCond(txtCond.getText());
			targetParam.setRegexpIdentifier(chkRegExp.getSelection());
			break;
		case PID:
			targetParam.setPidCond(txtCond.getText());
			targetParam.setRegexpPid(chkRegExp.getSelection());
			break;
		case HOST:
			targetParam.setHostCond(txtCond.getText());
			targetParam.setRegexpHost(chkRegExp.getSelection());
			break;
		case MESSAGE:
			targetParam.setMessageCond(txtCond.getText());
			targetParam.setRegexpMessage(chkRegExp.getSelection());
			break;
		default:
			break;
		}
		super.okPressed();
	}

}
