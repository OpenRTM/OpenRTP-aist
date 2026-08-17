package jp.go.aist.rtm.nameserviceview.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import jp.go.aist.rtm.nameserviceview.nl.Messages;
import jp.go.aist.rtm.toolscommon.corba.CorbaUtil;

/**
 * IOR情報を表示するダイアログ
 */
public class ShowIORDialog extends TitleAreaDialog {

	private static final String TITLE_DIALOG = Messages.getString("ShowIORDialog.title");

	private Text iorText;
	private Text typeIdText;
	private Text profileText;

	private CorbaUtil.IORInfo selection;

	public ShowIORDialog(Shell parentShell, CorbaUtil.IORInfo iorInfo) {
		super(parentShell);
		setHelpAvailable(false);
		this.selection = iorInfo;
		setShellStyle(getShellStyle() | SWT.CENTER | SWT.RESIZE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout gl = new GridLayout();
		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gl);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		createComposite(mainComposite);
		return mainComposite;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
	    createButton(parent, IDialogConstants.OK_ID, "Close", true);
	}
	
	private void createComposite(Composite mainComposite) {
		Composite composite = new Composite(mainComposite, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		composite.setLayout(gl);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		// IOR
		Label ior = new Label(composite, SWT.NONE);
		ior.setText("IOR:");
		this.iorText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		GridData gd = new GridData(GridData.GRAB_HORIZONTAL);
		gd.widthHint = 600;
		gd.heightHint = 160;
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		this.iorText.setLayoutData(gd);
		this.iorText.setEditable(false);

		// Type ID
		Label typeId = new Label(composite, SWT.NONE);
		typeId.setText("Type ID:");
		this.typeIdText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		this.typeIdText.setLayoutData(gd);
		this.typeIdText.setEditable(false);

		// Profile
		Label profile = new Label(composite, SWT.NONE);
		profile.setText("Profile:");
		gd = new GridData(GridData.GRAB_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		profile.setLayoutData(gd);
		this.profileText = new Text(composite, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER);
		gd = new GridData(GridData.GRAB_HORIZONTAL);
		gd.widthHint = 600;
		gd.heightHint = 160;
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		this.profileText.setLayoutData(gd);
		this.profileText.setEditable(false);

		refreshIORInfo();
	}

	/**
	 * IOR情報を画面表示に設定します。
	 */
	private void refreshIORInfo() {
		this.iorText.setText("");
		this.typeIdText.setText("");
		this.profileText.setText("");
		if (this.selection == null) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		if (!this.selection.taggedProfiles.isEmpty()) {
			CorbaUtil.IORInfo.TaggedProfile prof = this.selection.taggedProfiles.get(0);
			sb.append(String.format("Server: %s:%s\r\n", prof.host, prof.port));
			sb.append(String.format("GPIO Version: %s\r\n", prof.gpioVersion));
			sb.append(String.format("Object Key: %s\r\n", prof.objKey));
			sb.append("Components:\r\n");
			for (String comp : prof.components) {
				sb.append(String.format("- %s\r\n", comp));
			}
		}
		this.iorText.setText(this.selection.ior);
		this.typeIdText.setText(this.selection.typeId);
		this.profileText.setText(sb.toString());
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(TITLE_DIALOG);
	}

	@Override
	protected Point getInitialSize() {
		return getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT, true);
	}

}
