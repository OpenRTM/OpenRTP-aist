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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;

public class ExportDialog extends Dialog {
	private String targetKind = "";

	private boolean outputRtc = false;
	private boolean outputIso = false;
	private String rtcFileName = "";
	private String isoFileName = "";
	
	private Button rtcBtn;
	private Button isoBtn;
	private Text rtcFileText;
	private Text isoFileText;
	private Button rtcRefBtn;
	private Button isoRefBtn;

	public void setTargetKind(String source) {
		this.targetKind = source;
	}

	public boolean outputRtc() {
		return outputRtc;
	}
	public boolean outputIso() {
		return outputIso;
	}
	public String getRtcFileName() {
		return rtcFileName;
	}
	public String getIsoFileName() {
		return isoFileName;
	}

	public ExportDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.CENTER | SWT.RESIZE);
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.getString("IMC.BASIC_BTN_EXPORT"));
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

		Composite selectComposite = new Composite(mainComposite, SWT.NULL);
		GridLayout gl = new GridLayout(3, false);
		selectComposite.setLayout(gl);
		selectComposite.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_CENTER));
		///////
		rtcBtn = new Button(selectComposite, SWT.CHECK);
		GridData gd = new GridData(GridData.BEGINNING, SWT.CENTER, false, false);
		rtcBtn.setText(targetKind);
		rtcBtn.setLayoutData(gd);
		rtcBtn.setSelection(false);
		rtcBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(rtcBtn.getSelection()) {
					rtcFileText.setEnabled(true);
					rtcRefBtn.setEnabled(true);
				} else {
					rtcFileText.setEnabled(false);
					rtcRefBtn.setEnabled(false);
				}
			}
		});
		
		rtcFileText = new Text(selectComposite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL, SWT.CENTER, false, false);
		gd.grabExcessHorizontalSpace = true;
		rtcFileText.setLayoutData(gd);
		rtcFileText.setEnabled(false);

		rtcRefBtn = new Button(selectComposite, SWT.PUSH);
		rtcRefBtn.setText("Ref...");
		gd = new GridData(GridData.END, SWT.CENTER, false, false);
		rtcRefBtn.setLayoutData(gd);
		rtcRefBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(),SWT.SAVE);
		        dialog.setText(Messages.getString("IMC.BASIC_BTN_EXPORT"));
				String[] names = new String[] { IMessageConstants.FILETYPE_XML };
				String[] exts = new String[] { "*.xml" };
				dialog.setFilterNames(names);
				dialog.setFilterExtensions(exts);
				dialog.setFileName(rtcFileText.getText());
				
				String selectedFileName = dialog.open();
				if(selectedFileName==null) return;
				rtcFileText.setText(selectedFileName);
			}
		});
		rtcRefBtn.setEnabled(false);
		///////
		isoBtn = new Button(selectComposite, SWT.CHECK);
		gd = new GridData(GridData.BEGINNING, SWT.CENTER, false, false);
		isoBtn.setText("ISO 22166-202 Profile");
		isoBtn.setLayoutData(gd);
		isoBtn.setSelection(false);
		isoBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(isoBtn.getSelection()) {
					isoFileText.setEnabled(true);
					isoRefBtn.setEnabled(true);
				} else {
					isoFileText.setEnabled(false);
					isoRefBtn.setEnabled(false);
				}
			}
		});
		
		isoFileText = new Text(selectComposite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL, SWT.CENTER, false, false);
		gd.grabExcessHorizontalSpace = true;
		isoFileText.setLayoutData(gd);
		isoFileText.setEnabled(false);

		isoRefBtn = new Button(selectComposite, SWT.PUSH);
		isoRefBtn.setText("Ref...");
		gd = new GridData(GridData.END, SWT.CENTER, false, false);
		isoRefBtn.setLayoutData(gd);
		isoRefBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(),SWT.SAVE);
		        dialog.setText(Messages.getString("IMC.BASIC_BTN_EXPORT"));
				String[] names = new String[] { IMessageConstants.FILETYPE_XML };
				String[] exts = new String[] { "*.xml" };
				dialog.setFilterNames(names);
				dialog.setFilterExtensions(exts);
				dialog.setFileName(rtcFileText.getText());
				
				String selectedFileName = dialog.open();
				if(selectedFileName==null) return;
				isoFileText.setText(selectedFileName);
			}
		});
		isoRefBtn.setEnabled(false);
		
		return mainComposite;
	}
	
	@Override
	protected void okPressed() {
		outputRtc = rtcBtn.getSelection();
		if(outputRtc) {
			rtcFileName = rtcFileText.getText().trim();
			if(rtcFileName.length() == 0) {
				MessageDialog.openError(getShell(),
						"Error", Messages.getString("IMC.EXPORT_CAUTION_FILE"));
				return;
			}
		}
		
		outputIso = isoBtn.getSelection();
		if(outputIso) {
			isoFileName = isoFileText.getText().trim();
			if(isoFileName.length() == 0) {
				MessageDialog.openError(getShell(),
						"Error", Messages.getString("IMC.EXPORT_CAUTION_FILE"));
				return;
			}
		}

		super.okPressed();
	}
}
