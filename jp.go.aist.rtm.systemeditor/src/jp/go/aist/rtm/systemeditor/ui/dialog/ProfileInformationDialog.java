package jp.go.aist.rtm.systemeditor.ui.dialog;

import static jp.go.aist.rtm.systemeditor.ui.util.RTMixin.isBlank;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import jp.go.aist.rtm.systemeditor.extension.SaveProfileExtension;
import jp.go.aist.rtm.systemeditor.factory.ProfileSaver;
import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.profiles.util.IDUtil;

/**
 * RTSプロファイルの保存時に表示するダイアログ
 *
 */
public class ProfileInformationDialog extends Dialog {

	static final String DIALOG_TITLE_CONFIRM = Messages
			.getString("Common.dialog.confirm_title");
	static final String DIALOG_TITLE_ERROR = Messages
			.getString("Common.dialog.error_title");

	private Text txtVendor;
	private Text txtSystemName;
	private Text txtVersion;
	private Text txtPath;
	private Button chkReplace;
	//
	IDUtil.RTSId inputId;
	private String inputPath;
	private boolean isReplace;
	//
	private boolean isOverWrite = false;
	
//	 必須コンポーネント設定の対象 2008.12.11
	private CheckboxTableViewer viewer;
	private SystemDiagram systemDiagram;

	/**
	 * コンストラクタ
	 * 
	 * @param shell
	 */
	public ProfileInformationDialog(Shell shell) {
		super(shell);
		this.setShellStyle(this.getShellStyle() | SWT.RESIZE  );
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout gridLayout = new GridLayout(3, false);

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gridLayout);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		KeyListener listener = new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				doValidate();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};

		txtVendor = createLabelAndText(mainComposite, "Vendor:"); //$NON-NLS-1$
		if (inputId != null) txtVendor.setText(inputId.vendor);
		txtVendor.addKeyListener(listener);
		txtSystemName = createLabelAndText(mainComposite, "System Name:"); //$NON-NLS-1$
		if (inputId != null) txtSystemName.setText(inputId.name);
		txtSystemName.addKeyListener(listener);
		txtVersion = createLabelAndText(mainComposite, "Version:"); //$NON-NLS-1$
		if (inputId != null) txtVersion.setText(inputId.version);
		txtVersion.addKeyListener(listener);
		//
		txtPath = createLabelAndTextAndButton(mainComposite, "Path:", inputPath, isOverWrite); //$NON-NLS-1$
		//
		// 必須コンポーネント選択エリアを追加  2008.12.11
		createRequiedComponentsArea(mainComposite);
		chkReplace = createRadioCheckButton(mainComposite,
				Messages.getString("ProfileInformationDialog.19"), SWT.CHECK);

		// 拡張ポイントボタンエリアを追加
		Composite extensionButtonComposite = new Composite(parent, SWT.NONE);
		gridLayout = new GridLayout(1, false);
		extensionButtonComposite.setLayout(gridLayout);
		extensionButtonComposite
				.setLayoutData(new GridData(GridData.FILL_BOTH));

		ProfileSaver creator = new ProfileSaver();
		for (ProfileSaver.ExtentionButton eb : creator
				.loadExtentionButtons(systemDiagram)) {
			if (isBlank(eb.getLabel()) || eb.getListener() == null) {
				continue;
			}
			Button button = new Button(extensionButtonComposite, SWT.PUSH);
			button.setText(eb.getLabel());
			GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
			gd.horizontalAlignment = SWT.FILL;
			button.setLayoutData(gd);
			button.addSelectionListener(eb.getListener());
		}

		return mainComposite;
	}

	private Text createLabelAndTextAndButton(Composite mainComposite,
			String labelString, String inputPath, boolean isOverWrite) {
		GridData gd;
		Label label = new Label(mainComposite, SWT.NULL);
		label.setText(labelString);
		final Text txtPathLocal = new Text(mainComposite, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
		if(inputPath!=null) txtPathLocal.setText(inputPath);
		KeyListener listener = new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				doValidate();
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}
		};
		txtPathLocal.addKeyListener(listener);
		txtPathLocal.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				doValidate();
			}
		});
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		txtPathLocal.setLayoutData(gd);
		if( isOverWrite ) txtPathLocal.setEnabled(false);
		//
		Button checkButton = new Button(mainComposite, SWT.PUSH);
		checkButton.setText(Messages.getString("ProfileInformationDialog.4") ); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalAlignment = SWT.FILL;
		checkButton.setLayoutData(gd);
		checkButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(getShell(), SWT.SAVE);
				if (txtPathLocal.getText().length() > 0)
					dialog.setFilterPath(txtPathLocal.getText());
				String newPath = dialog.open();
				if (newPath != null) {
					txtPathLocal.setText(newPath);
				}
			}
		});
		if( isOverWrite ) checkButton.setEnabled(false);
		return txtPathLocal;
	}
	private Button createRadioCheckButton(Composite composite, String labelString, int style) {
		GridData gd;
		Button radio = new Button(composite, style);
		radio.setText(labelString);
		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalSpan = 3;
		radio.setLayoutData(gd);

		return radio;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Profile Information"); //$NON-NLS-1$
	}

	@Override
	protected void okPressed() {
		ProfileSaver creator = new ProfileSaver();

		for (SaveProfileExtension.ErrorInfo info : creator
				.validate(systemDiagram)) {
			if (info.isError()) {
				openError(DIALOG_TITLE_ERROR, info.getMessage());
				return;
			} else {
				if (!openConfirm(DIALOG_TITLE_CONFIRM, info.getMessage())) {
					return;
				}
			}
		}

		if (inputId == null) {
			inputId = new IDUtil.RTSId(null, null, null);
		}
		inputId.vendor = txtVendor.getText();
		inputId.name = txtSystemName.getText();
		inputId.version = txtVersion.getText();
		inputPath = txtPath.getText();
		isReplace = chkReplace.getSelection();
//		inputUpdateLog = txtUpdateLog.getText();
		// OKボタンが押されたときに必須コンポーネントであるかの設定を更新する
		syncRequiredComponents();

		for (SaveProfileExtension.ErrorInfo info : creator.prepareSave(
				systemDiagram, inputId.vendor, inputId.name, inputId.version,
				inputPath)) {
			if (info.isError()) {
				openError(DIALOG_TITLE_ERROR, info.getMessage());
				return;
			} else {
				if (!openConfirm(DIALOG_TITLE_CONFIRM, info.getMessage())) {
					return;
				}
			}
		}

		super.okPressed();
	}

	public void setInputPath(String inputPath) {
		this.inputPath = inputPath;
	}

	public String getInputPath() {
		return inputPath;
	}

	public boolean isReplace() {
		return isReplace;
	}

	public String getInputSystemName() {
		return (inputId == null) ? null : inputId.name;
	}

//	public String getInputUpdateLog() {
//		return inputUpdateLog;
//	}

	public String getInputVendor() {
		return (inputId == null) ? null : inputId.vendor;
	}

	public String getInputVersion() {
		return (inputId == null) ? null : inputId.version;
	}

	/**
	 * システムIDからベンダー、バージョン、システム名をセットする
	 * 
	 * @param id
	 *            システムID
	 */
	public void setSystemId(String id) {
		this.inputId = IDUtil.parseRTSId(id);
	}

	/**
	 * 入力値からシステムIDを生成して返します。
	 * 
	 * @return システムID
	 */
	public String getSystemId() {
		return (inputId == null) ? "RTSystem:::" : inputId.toString();
	}

	/**
	 * @param owflag	上書き保存であれば、true:新規保存であれば、false
	 */
	public void setOverWrite(boolean owflag) {
		this.isOverWrite = owflag;
	}

	private void doValidate() {
		if (!isBlank(txtVendor.getText())
				&& !isBlank(txtSystemName.getText())
				&& !isBlank(txtVersion.getText())
				&& (isOverWrite || (!isOverWrite && !isBlank(txtPath.getText())))) {
			this.getButton(IDialogConstants.OK_ID).setEnabled(true);
		} else {
			this.getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}

	private Text createLabelAndText(Composite baseComposite, String labelString) {
		GridData gd;
		Label label = new Label(baseComposite, SWT.NULL);
		label.setText(labelString);
		Text targetText = new Text(baseComposite, SWT.SINGLE | SWT.BORDER | SWT.LEFT);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		targetText.setLayoutData(gd);
		
		return targetText;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control result = super.createButtonBar(parent);
		doValidate();
		return result;
	}

	public void setSystemDiagram(SystemDiagram sd) {
		this.systemDiagram = sd;
	}

	// 必須コンポーネント選択エリア 2008.12.11
	private void createRequiedComponentsArea(Composite mainComposite) {
		Label pathLabel = new Label(mainComposite, SWT.LEFT);
		pathLabel.setText("Required:"); //$NON-NLS-1$
		viewer = CheckboxTableViewer.newCheckList(mainComposite, SWT.BORDER);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new ITableLabelProvider() {
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				Component item = (Component) element;
				return item.getInstanceNameL();
			}

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return true;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}
		});
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth = 320;
		gd.heightHint = 100;
		viewer.getTable().setLayoutData(gd);
		setViewerInput();
		
		Composite buttonComposite = new Composite(mainComposite, SWT.NONE);
		GridLayout gridLayout = new GridLayout(1, false);
		buttonComposite.setLayout(gridLayout);
		buttonComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Button selectButton = new Button(buttonComposite, SWT.PUSH);
		selectButton.setText(Messages.getString("ProfileInformationDialog.17") ); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalAlignment = SWT.FILL;
		selectButton.setLayoutData(gd);
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setAllChecked(true);
			}
		});

		Button deselectButton = new Button(buttonComposite, SWT.PUSH);
		deselectButton.setText(Messages.getString("ProfileInformationDialog.18") ); //$NON-NLS-1$
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.horizontalAlignment = SWT.FILL;
		deselectButton.setLayoutData(gd);
		deselectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewer.setAllChecked(false);
			}
		});

	}

	private void setViewerInput() {
		viewer.setInput(systemDiagram.getRegisteredComponents());
		TableItem[] children = viewer.getTable().getItems();
		for (int i = 0; i < children.length; i++) {
			TableItem item = children[i];
			Component component = (Component) item.getData();
			item.setChecked(component.isRequired());
		}
	}

	private void syncRequiredComponents() {
		TableItem[] children = viewer.getTable().getItems();
		for (int i = 0; i < children.length; i++) {
			TableItem item = children[i];
			Component component = (Component) item.getData();
			component.setRequired(item.getChecked());
		}
	}

	public void openError(String title, String message) {
		MessageDialog.openError(getShell(), title, message);
	}

	public boolean openConfirm(String title, String message) {
		return MessageDialog.openConfirm(getShell(), title, message);
	}

}
