package jp.go.aist.rtm.rtcbuilder.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import jp.go.aist.rtm.rtcbuilder.extension.ImportExtension;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;

public class ImportDialog extends Dialog {
	private String targetKind = "";

	private String selectedKind = "";
	private String selectedFile = "";
	
	private Button rtcBtn;
	private Button isoBtn;
	private Text fileText;
	
	private ImportExtension extension;

	public void setTargetKind(String source) {
		this.targetKind = source;
	}
	public void setExtension(ImportExtension source) {
		this.extension = source;
	}
	
	public String getSelectedFile() {
		return selectedFile;
	}
	public String getSelectedKind() {
		return selectedKind;
	}

	public ImportDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.CENTER);
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("IMC.BASIC_BTN_IMPORT"));
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(500, 180);
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	protected Control createDialogArea(Composite parent) {
		GridLayout gridLayout = new GridLayout();

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gridLayout);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Composite kindComposite = new Composite(mainComposite, SWT.NULL);
		GridLayout gl = new GridLayout(3, false);
		kindComposite.setLayout(gl);
		kindComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		
		Label label = new Label(kindComposite, SWT.NONE);
		label.setText(Messages.getString("IMC.IMPORT_KIND")); //$NON-NLS-1$
		GridData gd = new GridData(GridData.BEGINNING, SWT.CENTER, false, false);
		label.setLayoutData(gd);
		
		rtcBtn = new Button(kindComposite, SWT.RADIO);
		gd = new GridData(GridData.BEGINNING, SWT.CENTER, false, false);
		rtcBtn.setText(targetKind);
		rtcBtn.setLayoutData(gd);
		
		isoBtn = new Button(kindComposite, SWT.RADIO);
		gd = new GridData(GridData.BEGINNING, SWT.CENTER, false, false);
		isoBtn.setText("ISO 22166-202 Profile");
		isoBtn.setLayoutData(gd);
		
		rtcBtn.setSelection(true);
		isoBtn.setSelection(false);
		/////
		Composite fileComposite = new Composite(mainComposite, SWT.NULL);
		gl = new GridLayout(3, false);
		fileComposite.setLayout(gl);
		gd = new GridData(GridData.FILL, SWT.CENTER, true, false);
		fileComposite.setLayoutData(gd);
		
		Label lblFile = new Label(fileComposite, SWT.NONE);
		lblFile.setText(Messages.getString("IMC.IMPORT_FILE")); //$NON-NLS-1$
		gd = new GridData(GridData.BEGINNING, SWT.CENTER, false, false);
		lblFile.setLayoutData(gd);
		
		fileText = new Text(fileComposite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL, SWT.CENTER, false, false);
		gd.grabExcessHorizontalSpace = true;
		fileText.setLayoutData(gd);

		Button refBtn = new Button(fileComposite, SWT.PUSH);
		refBtn.setText("Ref...");
		gd = new GridData(GridData.END, SWT.CENTER, false, false);
		refBtn.setLayoutData(gd);
		refBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(),SWT.OPEN);
		        dialog.setText(Messages.getString("IMC.BASIC_BTN_IMPORT"));
				String[] names = extension == null ? new String[] { IMessageConstants.FILETYPE_XML }
					: extension.getFileDialogFilterNames();
				String[] exts = extension == null	 ? new String[] { "*.xml" }
					: extension.getFileDialogFilterExtensions();
				dialog.setFilterNames(names);
				dialog.setFilterExtensions(exts);
				dialog.setFileName(fileText.getText());
				
				String selectedFileName = dialog.open();
				if(selectedFileName==null) return;
				fileText.setText(selectedFileName);
			}
		});
		
		return mainComposite;
	}
	
	@Override
	protected void okPressed() {
		selectedFile = fileText.getText().trim();
		if(selectedFile.length() == 0) {
			MessageDialog.openError(getShell(),
					"Error", Messages.getString("IMC.IMPORT_CAUTION_FILE"));
			return;
		}
		if(rtcBtn.getSelection()) {
			selectedKind = "RTC";
		} else if(isoBtn.getSelection()) {
			selectedKind = "ISO";
		} else {
			MessageDialog.openError(getShell(), "Error", Messages.getString("IMC.IMPORT_CAUTION_TYPE"));
			return;
		}
		super.okPressed();
	}

}
