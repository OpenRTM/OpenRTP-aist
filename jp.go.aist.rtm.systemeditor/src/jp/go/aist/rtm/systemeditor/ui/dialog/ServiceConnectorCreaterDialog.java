package jp.go.aist.rtm.systemeditor.ui.dialog;

import static jp.go.aist.rtm.systemeditor.nl.Messages.getString;
import static jp.go.aist.rtm.systemeditor.ui.util.RTMixin.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.ConnectorProfile;
import jp.go.aist.rtm.toolscommon.model.component.Port;
import jp.go.aist.rtm.toolscommon.model.component.PortInterfaceProfile;
import jp.go.aist.rtm.toolscommon.model.component.ServicePort;

/**
 * サービスポート間の接続のコネクタプロファイルの選択ダイアログ
 * <P>
 * ポート名を入力する 接続しようとしているServicePort間でマッチングを行い、必要に応じて警告を表示する。
 * 
 * ここでいうマッチングは、PortInterfaceProfile の type、および instance_name」が同じで polarity が
 * PROVIDED と REQUIRED で対応することをいう。
 * <ul>
 * <li>完全一致した場合 → 警告なし</li>
 * <li>一部一致した場合 → 警告「Port interfaces do not match completely.」</li>
 * <li>完全不一致した場合 → 警告「No corresponding port interface.」</li>
 * </ul>
 * 
 * また、対応するインスタンスをコネクタプロファイルのプロパティに設定する場合は以下のチェックを行います。
 * <ul>
 * <li>type が不一致 → エラー「Unmatch interface type consumer={0} provider={1}」</li>
 * <li>instance_name が不一致 → 警告「Unmatch interface instance consumer={0}
 * provider={1}」</li>
 * </ul>
 */
public class ServiceConnectorCreaterDialog extends ConnectorDialogBase {

	static final int EXEC_BUTTON_WIDTH = 70;

	static final int PROPERTY_CONSUMER = 0;
	static final int PROPERTY_PROVIDER = 1;

	private Text nameText;

	private Combo directionCombo;
	private Label directionLabel;
	private Label directionLabel2;

	private String firstPort2secondPort;
	private String secondPort2firstPort;

	private Composite detailComposite;

	private TableViewer interfaceTableViewer;
	private Table interfaceTable;

	private Button addButton;
	private Button deleteButton;

	private Point defaultDialogSize;

	private ConnectorProfile connectorProfile;
	private ConnectorProfile dialogResult;

	private ServicePort first;
	private ServicePort second;

	private List<InterfaceEntry> interfaceList;
	private InterfaceEntry selectedEntry;

	private Map<String, ConnectorProfile.InterfaceId> consumerMap;
	private List<String> consumerLabels;

	private Map<String, ConnectorProfile.InterfaceId> providerMap;
	private List<String> providerLabels;

	private String baseMessage;
	private TableViewer additionalTableViewer;

	public ServiceConnectorCreaterDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * ConnectorProfileCreaterインタフェースの実装メソッド
	 * <p>
	 * ConnectorProfileとなる候補が複数ある場合には、ダイアログを表示し、ConnectorProfileを作成する。
	 */
	public ConnectorProfile getConnectorProfile(ServicePort first,
			ServicePort second) {
		this.first = first;
		this.second = second;

		consumerMap = new HashMap<String, ConnectorProfile.InterfaceId>();
		consumerLabels = new ArrayList<String>();
		providerMap = new HashMap<String, ConnectorProfile.InterfaceId>();
		providerLabels = new ArrayList<String>();
		if (first != null) {
			registInterfaceMap(first);
		}
		if (second != null) {
			registInterfaceMap(second);
		}

		String firstName = (first != null) ? first.getNameL() : "none";
		String secondName = (second != null) ? second.getNameL() : "none";

  		connectorProfile = ComponentFactory.eINSTANCE.createConnectorProfile();
		connectorProfile.setName(firstName + "_" + secondName);
		this.connectorProfile.setProperty("port.connection.strictness", "strict");

		open();

		return dialogResult;
	}

	/** Consumer/Providerのインターフェース一覧を登録 */
	void registInterfaceMap(ServicePort port) {
		if (port == null) {
			return;
		}
		String compName = "unknown";
		String portName = "unknown";
		//
		String name = port.getNameL();
		if (name != null) {
			String[] names = name.split("\\.");
			if (names.length < 2) {
				if (port.eContainer() instanceof Component) {
					compName = ((Component) port.eContainer())
							.getInstanceNameL();
				}
				portName = names[0];
			} else {
				compName = names[0];
				portName = names[1];
			}
		}
		for (PortInterfaceProfile ip : port.getInterfaces()) {
			ConnectorProfile.InterfaceId id = new ConnectorProfile.InterfaceId();
			id.rtc_name = compName;
			id.port_name = portName;
			id.setPolarityBy(ip);
			id.if_tname = ip.getTypeName();
			id.if_iname = ip.getInstanceName();

			String label = InterfaceEntry.toLabelString(id);
			if (ip.isRequiredPolarity()) {
				consumerMap.put(label, id);
				consumerLabels.add(label);
			} else {
				providerMap.put(label, id);
				providerLabels.add(label);
			}
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout gl;
		gl = new GridLayout();

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gl);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(mainComposite, SWT.NONE);
		label.setText(getString("ConnectorCreaterDialogBase.1"));
		GridData labelLayloutData = new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(labelLayloutData);
		labelLayloutData.heightHint = 20;

		createConnectorProfileComposite(mainComposite);

		baseMessage = "Error";
		try {
			List<PortInterfaceProfile> interfaces1 = (first != null) ? first
					.getInterfaces() : new ArrayList<PortInterfaceProfile>();
			List<PortInterfaceProfile> interfaces2 = (second != null) ? second
					.getInterfaces() : new ArrayList<PortInterfaceProfile>();
			int countMatch = countMatch(interfaces1, interfaces2);
			if (countMatch > 0
					&& countMatch == countTotal(interfaces1, interfaces2)) {
				baseMessage = null;
			} else {
				if (countMatch == 0) {
					baseMessage = getString("ServiceConnectorCreaterDialog.msg.interface_not_match");
				} else {
					baseMessage = getString("ServiceConnectorCreaterDialog.msg.interface_part_not_match");
				}
			}
		} catch (Exception e) {
		}
		if (baseMessage != null) {
			setMessage(baseMessage, IMessageProvider.WARNING);
		}

		return mainComposite;
	}

	/**
	 * メインとなる表示部を作成する
	 */
	private void createConnectorProfileComposite(final Composite mainComposite) {
		GridLayout gl;
		GridData gd;
		Composite portProfileEditComposite = new Composite(mainComposite,
				SWT.NONE);
		gl = new GridLayout(3, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		portProfileEditComposite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		portProfileEditComposite.setLayoutData(gd);

		Label name = new Label(portProfileEditComposite, SWT.NONE);
		name.setText("Name :");
		nameText = new Text(portProfileEditComposite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(gd);
		nameText.setTextLimit(255);
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setName(nameText.getText());
				notifyModified();
			}
		});
		createLabel(portProfileEditComposite, "");
		//Direction
		createLabel(portProfileEditComposite, "Connect Direction :");
		int style = SWT.DROP_DOWN | SWT.READ_ONLY;
		directionCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		directionCombo.setLayoutData(gd);
		
		String firstItem =	Messages.getString("ServiceConnectorCreaterDialog.combo.direction0")
							+ " "
							+ first.getNameL()
							+ " "
							+ Messages.getString("ServiceConnectorCreaterDialog.combo.direction1")
							+ " "
							+ second.getNameL()
							+ " "
							+ Messages.getString("ServiceConnectorCreaterDialog.combo.direction2");
		String secondItem = Messages.getString("ServiceConnectorCreaterDialog.combo.direction0") 
							+ " "
							+ second.getNameL()
							+ " "
							+ Messages.getString("ServiceConnectorCreaterDialog.combo.direction1")
							+ " "
							+ first.getNameL()
							+ " "
							+ Messages.getString("ServiceConnectorCreaterDialog.combo.direction2");
		if(connectorProfile.isIsReverse()) {
			directionCombo.add(secondItem);
			directionCombo.add(firstItem);
		} else {
			directionCombo.add(firstItem);
			directionCombo.add(secondItem);
		}
		
		directionCombo.select(0);
		directionCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				directionLabel2.setText("");
				int selected = directionCombo.getSelectionIndex();
				if(selected == 0) {
					boolean disp2 = checkInterface(first, second);
					if(connectorProfile.isIsReverse()) {
						directionLabel.setText(secondPort2firstPort);
						if(disp2) {
							directionLabel2.setText(firstPort2secondPort);
						}
					} else {
						directionLabel.setText(firstPort2secondPort);
						if(disp2) {
							directionLabel2.setText(secondPort2firstPort);
						}
					}
				} else {
					boolean disp2 = checkInterface(second, first);
					if(connectorProfile.isIsReverse()) {
						directionLabel.setText(firstPort2secondPort);
						if(disp2) {
							directionLabel2.setText(secondPort2firstPort);
						}
					} else {
						directionLabel.setText(secondPort2firstPort);
						if(disp2) {
							directionLabel2.setText(firstPort2secondPort);
						}
					}
				}
				directionLabel2.pack();
				mainComposite.layout();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		createLabel(portProfileEditComposite, "");
		//
		createLabel(portProfileEditComposite, "");
		
		String firstPortIP = getPortInfo(first, true, false);
		String secondPortIP = getPortInfo(second, true, false);
		String firstPortIPPort = getPortInfo(first, true, true);
		String secondPortIPPort = getPortInfo(second, true, true);
		
		firstPort2secondPort = Messages.getString("ServiceConnectorCreaterDialog.label.direction2_1")
								+ firstPortIP
								+ Messages.getString("ServiceConnectorCreaterDialog.label.direction2_2")
								+ secondPortIPPort
								+ Messages.getString("ServiceConnectorCreaterDialog.label.direction2_3");
		secondPort2firstPort = Messages.getString("ServiceConnectorCreaterDialog.label.direction1_1")
								+ secondPortIP
								+ Messages.getString("ServiceConnectorCreaterDialog.label.direction1_2")
								+ firstPortIPPort
								+ Messages.getString("ServiceConnectorCreaterDialog.label.direction1_3");

		directionLabel = new Label(portProfileEditComposite, SWT.WRAP);
		if(connectorProfile.isIsReverse()) {
			directionLabel.setText(secondPort2firstPort);
		} else {
			directionLabel.setText(firstPort2secondPort);
		}
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		directionLabel.setLayoutData(gd);
		//
		createLabel(portProfileEditComposite, "");
		directionLabel2 = new Label(portProfileEditComposite, SWT.WRAP);
		if(checkInterface(first, second)) {
			if(connectorProfile.isIsReverse()) {
				directionLabel2.setText(firstPort2secondPort);
			} else {
				directionLabel2.setText(secondPort2firstPort);
			}
		}
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		directionLabel2.setLayoutData(gd);
		//
		final Button detailCheck = new Button(portProfileEditComposite,
				SWT.CHECK);
		detailCheck.setText(getString("ServiceConnectorCreaterDialog.detail"));
		detailCheck.setSelection(false);
		createLabel(portProfileEditComposite, "");
		createLabel(portProfileEditComposite, "");

		detailCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = detailCheck.getSelection();
				if (selection && detailComposite == null) {
					createDetailComposite(mainComposite);
				}
				detailComposite.setVisible(selection);
				if (!selection) {
					// 詳細チェック解除時に、元のダイアログのサイズに戻す
					getShell().setSize(defaultDialogSize);
				} else {
					getShell().setSize(
							getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));
				}
			}
		});

		loadData();
	}
	
	private boolean checkInterface(Port first, Port second) {
		for(PortInterfaceProfile eachSecond : second.getInterfaces()) {
			if(eachSecond.getPolarity().equals("REQUIRED") == false ) continue;
			String ifName = eachSecond.getTypeName();
			for(PortInterfaceProfile eachFirst : first.getInterfaces()) {
				if(eachFirst.getPolarity().equals("PROVIDED") == false ) continue;
				if(eachFirst.getTypeName().equals(ifName) == false) continue;
				return true;
			}
		}
		return false;
	}

	/**
	 * 詳細設定の表示部を作成する
	 */
	private Composite createDetailComposite(Composite parent) {
		GridLayout gl;
		GridData gd;

		detailComposite = new Composite(parent, SWT.NONE);
		gl = new GridLayout(2, false);
		gd = new GridData(GridData.FILL_BOTH);
		detailComposite.setLayout(gl);
		detailComposite.setLayoutData(gd);
		detailComposite.setVisible(false);

		interfaceTableViewer = new TableViewer(detailComposite,
				SWT.FULL_SELECTION | SWT.SINGLE | SWT.BORDER);
		interfaceTableViewer.setContentProvider(new ArrayContentProvider());
		interfaceTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						StructuredSelection selection = (StructuredSelection) event
								.getSelection();
						selectedEntry = (InterfaceEntry) selection
								.getFirstElement();
						deleteButton.setEnabled(true);
					}
				});

		interfaceTable = interfaceTableViewer.getTable();
		gl = new GridLayout(1, false);
		gl.numColumns = 1;
		gd = new GridData();
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;	
		gd.heightHint = 120;
		interfaceTable.setLayout(gl);
		interfaceTable.setLayoutData(gd);
		interfaceTable.setLinesVisible(true);
		interfaceTable.setHeaderVisible(true);

		TableViewerColumn col = null;
		col = createColumn(interfaceTableViewer, "Consumer", 300);
		col.setEditingSupport(new InterfaceTableEdittingSupport(
				interfaceTableViewer, PROPERTY_CONSUMER));
		col = createColumn(interfaceTableViewer, "Provider", 300);
		col.setEditingSupport(new InterfaceTableEdittingSupport(
				interfaceTableViewer, PROPERTY_PROVIDER));

		interfaceTableViewer
				.setLabelProvider(new InterfaceEntryLabelProvider());

		Composite buttonComposite = new Composite(detailComposite, SWT.NONE);
		gl = new GridLayout();
		gd = new GridData(GridData.FILL_BOTH);
		buttonComposite.setLayout(gl);
		buttonComposite.setLayoutData(gd);

		addButton = new Button(buttonComposite, SWT.TOP);
		addButton.setText(getString("Common.button.add"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		addButton.setLayoutData(gd);
		addButton.setEnabled(true);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				InterfaceEntry entry = newEntry();
				interfaceList.add(entry);
				interfaceTableViewer.refresh();
				validateEntry();
			}
		});

		deleteButton = new Button(buttonComposite, SWT.TOP);
		deleteButton.setText(getString("Common.button.delete"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		deleteButton.setLayoutData(gd);
		deleteButton.setEnabled(false);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedEntry == null) {
					return;
				}
				interfaceList.remove(selectedEntry);
				interfaceTableViewer.refresh();
				deleteButton.setEnabled(false);
				validateEntry();
			}
		});

		additionalTableViewer = createAdditionalTableViewer(detailComposite);
		
		loadDetailData();

		defaultDialogSize = getShell().getSize();
		getShell().setSize(getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT));

		return detailComposite;
	}

	Label createLabel(Composite parent, String label) {
		Label l = new Label(parent, SWT.NONE);
		l.setText(label);
		return l;
	}

	/**
	 * モデル情報にアクセスし、表示に設定する
	 */
	void loadData() {
		nameText.setText(connectorProfile.getName());

		loadDetailData();
	}

	void loadDetailData() {
		if (interfaceTableViewer == null) {
			return;
		}
		interfaceTableViewer.setInput(Collections.EMPTY_LIST);
		if (interfaceList == null) {
			interfaceList = new ArrayList<InterfaceEntry>();
		}
		interfaceList.clear();
		interfaceTableViewer.setInput(interfaceList);
	}

	/** Consumer/Providerの新規エントリを作成 */
	InterfaceEntry newEntry() {
		InterfaceEntry entry = new InterfaceEntry();
		//
		String label = consumerLabels.get(0);
		ConnectorProfile.InterfaceId id = consumerMap.get(label);
		entry.consumer = id.clone();
		//
		label = providerLabels.get(0);
		id = providerMap.get(label);
		entry.provider = id.clone();
		return entry;
	}

	/** Consumer/Providerのエントリをプロファイルへ反映 */
	void applyEntry() {
		if (interfaceList == null) {
			return;
		}
		List<String> keys = new ArrayList<String>();
		// Consumer/Providerのプロパティをクリア
		for (String key : connectorProfile.getPropertyKeys()) {
			if (ConnectorProfile.InterfaceId.isValid(key)) {
				keys.add(key);
			}
		}
		for (String key : keys) {
			connectorProfile.removeProperty(key);
		}
		// 新しいConsumer/Providerを設定
		for (InterfaceEntry e : interfaceList) {
			String consumer = e.consumer.toString();
			String provider = e.provider.toString();
			connectorProfile.setProperty(consumer, provider);
		}
		
		if (additionalTableViewer != null) {
			List<?> additional = (List<?>) additionalTableViewer.getInput();
			for (Object o : additional) {
				AdditionalEntry target = (AdditionalEntry) o;
				connectorProfile.setProperty(target.getName(), target
						.getValue());
			}
		}
	}

	/** Consumer/Providerのエントリの整合性チェック */
	void validateEntry() {
		if (interfaceList == null) {
			return;
		}
		String message = baseMessage;
		int level = IMessageProvider.WARNING;
		for (InterfaceEntry entry : interfaceList) {
			if (entry.validate()) {
				continue;
			}
			if (message == null) {
				message = "";
			}
			if (!message.isEmpty()) {
				message += "\n";
			}
			if (entry.getErrorMessage() != null) {
				message += entry.getErrorMessage();
				level = IMessageProvider.ERROR;
			}
			if (entry.getWarningMessage() != null) {
				message += entry.getWarningMessage();
			}
		}
		setMessage(message, level);
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Connector Profile");
		this.setHelpAvailable(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		if(directionCombo.getSelectionIndex() == 1) {
			connectorProfile.setIsReverse(!connectorProfile.isIsReverse());
		}

		if (additionalTableViewer != null) {
			// 重複チェック
			if (!checkProperties((List<AdditionalEntry>) additionalTableViewer
					.getInput())) {
				return;
			}
		}
		applyEntry();
		dialogResult = connectorProfile;
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		connectorProfile = null;
		super.cancelPressed();
	}

	/**
	 * メッセージを設定する。
	 */
	@Override
	public void setMessage(String newMessage, int newType) {
		super.setMessage(newMessage, newType);
	}

	/**
	 * 設定に変更があった場合に呼び出されることを想定したメソッド。
	 * <p>
	 * 注意：設定値の変更がある場合には、必ずこのメソッドを呼び出すこと<br>
	 * 現在は、表示側で設定を変更した後に、このメソッドを必ず呼び出すように実装しているが、
	 * 項目数が増えるようならば、モデルの変更通知機能を使用して実装する方が良い。
	 */
	public void notifyModified() {
	}

	/**
	 * PortInterfaceProfileのマッチ数を数える
	 *    requiredだけが対象
	 * 
	 * @param interfaces1
	 * @param interfaces2
	 * @return
	 */
	private int countMatch(List<PortInterfaceProfile> interfaces1,
			List<PortInterfaceProfile> interfaces2) {

		int result = 0;
		for (PortInterfaceProfile profile : interfaces1) {
			if (profile.isRequiredPolarity()) result += hasMatch(profile, interfaces2);
		}
		for (PortInterfaceProfile profile : interfaces2) {
			if (profile.isRequiredPolarity()) result += hasMatch(profile, interfaces1);
		}

		return result;
	}

	//　requiring interface の数を返す
	private int countTotal(List<PortInterfaceProfile> interfaces1,
			List<PortInterfaceProfile> interfaces2) {
		int result = 0;
		for (PortInterfaceProfile profile : interfaces1) {
			if (profile.isRequiredPolarity()) result++;
		}
		for (PortInterfaceProfile profile : interfaces2) {
			if (profile.isRequiredPolarity()) result++;
		}
		return result;
	}

	private int hasMatch(PortInterfaceProfile profile,
			List<PortInterfaceProfile> profiles2) {
		for (PortInterfaceProfile profile2 : profiles2) {
			if (isMatch(profile, profile2)) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * PortInterfaceProfileがマッチするかどうか
	 * 
	 * @param profile
	 * @param profile2
	 * @return マッチするかどうか
	 */
	private boolean isMatch(PortInterfaceProfile profile1,
			PortInterfaceProfile profile2) {
		if (!profile1.getTypeName().equals(profile2.getTypeName()))
			return false;
		if (!profile1.getInstanceName().equals(profile2.getInstanceName()))
			return false;
		if (profile1.isProvidedPolarity())
			return profile2.isRequiredPolarity();
		if (profile1.isRequiredPolarity())
			return profile2.isProvidedPolarity();
		return false;
	}

	@Override
	protected Point getInitialSize() {
		int width = 600;
		int height = 330;
		return getShell().computeSize(width, height, true);
	}

	/** インターフェース一覧のエントリ */
	public static class InterfaceEntry {
		ConnectorProfile.InterfaceId consumer;
		ConnectorProfile.InterfaceId provider;
		String errorMessage = null;
		String warningMessage = null;

		public String getConsumerLabel() {
			return toLabelString(consumer);
		}

		public String getProviderLabel() {
			return toLabelString(provider);
		}

		public static String toLabelString(ConnectorProfile.InterfaceId id) {
			return id.rtc_name + ":" + id.if_tname + ":" + id.if_iname;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public String getWarningMessage() {
			return warningMessage;
		}

		public boolean validate() {
			errorMessage = null;
			warningMessage = null;
			if (consumer.if_tname != null
					&& !consumer.if_tname.equals(provider.if_tname)) {
				errorMessage = form(getString("ServiceConnectorCreaterDialog.msg.type_not_match"),
						consumer.if_tname, provider.if_tname);
				return false;
			}
			if (consumer.if_iname != null
					&& !consumer.if_iname.equals(provider.if_iname)) {
				warningMessage = form(getString("ServiceConnectorCreaterDialog.msg.name_not_match"),
						consumer.if_iname, provider.if_iname);
				return false;
			}
			return true;
		}
	}

	/** ポート一覧表示のLabelProvider */
	public class InterfaceEntryLabelProvider extends LabelProvider implements
			ITableLabelProvider {
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			InterfaceEntry entry = (InterfaceEntry) element;
			if (columnIndex == 0) {
				return entry.getConsumerLabel();
			} else if (columnIndex == 1) {
				return entry.getProviderLabel();
			}
			return null;
		}
	}

	/** インターフェース一覧のEditingSupport */
	public class InterfaceTableEdittingSupport extends EditingSupport {
		CellEditor editor;
		int column;

		public InterfaceTableEdittingSupport(ColumnViewer viewer, int column) {
			super(viewer);

			// Create the correct editor based on the column index
			this.column = column;
			switch (this.column) {
			case PROPERTY_CONSUMER:
			case PROPERTY_PROVIDER:
				editor = new ComboBoxCellEditor(((TableViewer) viewer)
						.getTable(), itemLabels().toArray(new String[0]),
						SWT.READ_ONLY);
				break;
			default:
				break;
			}
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected Object getValue(Object element) {
			InterfaceEntry entry = (InterfaceEntry) element;
			//
			String label = null;
			if (column == PROPERTY_CONSUMER) {
				label = entry.getConsumerLabel();
			} else if (column == PROPERTY_PROVIDER) {
				label = entry.getProviderLabel();
			}
			if (label == null) {
				return null;
			}
			int index = itemLabels().indexOf(label);
			return new Integer(index);
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (!(element instanceof InterfaceEntry)) {
				return;
			}
			InterfaceEntry entry = (InterfaceEntry) element;
			//
			if (column == PROPERTY_CONSUMER) {
				Integer index = (Integer) value;
				if (index >= 0 && index < itemLabels().size()) {
					ConnectorProfile.InterfaceId id = itemMap().get(
							itemLabels().get(index));
					entry.consumer = id.clone();
				}
			} else if (column == PROPERTY_PROVIDER) {
				Integer index = (Integer) value;
				if (index >= 0 && index < itemLabels().size()) {
					ConnectorProfile.InterfaceId id = itemMap().get(
							itemLabels().get(index));
					entry.provider = id.clone();
				}
			}
			validateEntry();
			getViewer().update(element, null);
		}

		Map<String, ConnectorProfile.InterfaceId> itemMap() {
			if (column == PROPERTY_CONSUMER) {
				return consumerMap;
			} else if (column == PROPERTY_PROVIDER) {
				return providerMap;
			}
			return new HashMap<String, ConnectorProfile.InterfaceId>();
		}

		List<String> itemLabels() {
			if (column == PROPERTY_CONSUMER) {
				return consumerLabels;
			} else if (column == PROPERTY_PROVIDER) {
				return providerLabels;
			}
			return new ArrayList<String>();
		}

	}

}
