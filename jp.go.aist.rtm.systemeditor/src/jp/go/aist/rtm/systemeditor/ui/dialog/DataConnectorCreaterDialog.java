package jp.go.aist.rtm.systemeditor.ui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import jp.go.aist.rtm.systemeditor.manager.SystemEditorPreferenceManager;
import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.views.configurationview.configurationwrapper.ConfigurationCondition;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.ComponentSpecification;
import jp.go.aist.rtm.toolscommon.model.component.ConnectorProfile;
import jp.go.aist.rtm.toolscommon.model.component.InPort;
import jp.go.aist.rtm.toolscommon.model.component.NameValue;
import jp.go.aist.rtm.toolscommon.model.component.OutPort;
import jp.go.aist.rtm.toolscommon.model.component.Port;
import jp.go.aist.rtm.toolscommon.util.ConnectorUtil;
import jp.go.aist.rtm.toolscommon.util.ConnectorUtil.SerializerInfo;

/**
 * データポート間の接続のコネクタプロファイルの選択ダイアログ
 * <P>
 * コネクタプロファイルの選択可能なタイプを、OutPortおよびInPortから判断しプルダウンとして表示する。（「Any」自体は表示されない）(文字のケースは無視してマッチングし、マッチした際に表示されるのはOutPortの文字列とする)<br>
 * 選択可能であるのは、データポートだけであり、サービスポートは含まれない。<br>
 * OutPortもしくはInPortに「Any」が含まれている場合には、相手のすべて型を受け入れるものとする。<br>
 * OutPortおよびInportにAnyが含まれている場合には、コンボボックスに任意の文字列を入力可能とし、「*任意入力可」を表示する。<br>
 * サブスクリプションタイプは、データフロータイプが「Push」の時のみ表示される。<br>
 * PushRateは、サブスクリプションタイプが「Periodic」であり、かつデータフロータイプが「Push」の時のみ表示される<br>
 */
public class DataConnectorCreaterDialog extends ConnectorDialogBase {
	private static final int NAME_WIDTH = 150;

	private static final String NORMAL_COLOR = "NORMAL_COLOR"; // @jve:decl-index=0: //$NON-NLS-1$
	private static final String ERROR_COLOR = "ERROR_COLOR"; //$NON-NLS-1$

	private static ColorRegistry colorRegistry = null;

	private Text nameText;
	private Combo serializerTypeCombo;
	private Combo interfaceTypeCombo;
	private Combo dataflowTypeCombo;
	private Combo subscriptionTypeCombo;
	private Text pushRateText;
	private Combo pushPolicyCombo;
	private Text skipCountText;
	private Combo timePolicyCombo;
	
	private Combo directionCombo;
	private Label directionLabel;
	private Label directionLabel2;

	private String outport2inPort;
	private String inport2outPort;

	private ScrolledComposite propertyScrollArea;
	
	private ConnectorProfile connectorProfile;
	private ConnectorProfile dialogResult;
	private OutPort outport;
	private InPort inport;
	private BufferPackage ob;
	private BufferPackage ib;

	private TableViewer additionalTableViewer;
	private boolean disableNotify;
	
	private Map<String, List<PropertyElem>> propertyList = new HashMap<String, List<PropertyElem>>();
	private boolean existIFOpt = false;

	static class BufferPackage {
		Text lengthText;
		Combo fullPolicyCombo;
		Text writeTimeoutText;
		Combo emptyPolicyCombo;
		Text readTimeoutText;
		boolean enable;

		public BufferPackage() {
			this.enable = true;
		}
	}

	public DataConnectorCreaterDialog(Shell parentShell) {
		super(parentShell);
		
		if (colorRegistry == null) {
			colorRegistry = new ColorRegistry();
			colorRegistry.put(NORMAL_COLOR, new RGB(255, 255, 255));
			colorRegistry.put(ERROR_COLOR, new RGB(255, 0, 0));
		}
	}

	/**
	 * ConnectorProfileCreaterインタフェースの実装メソッド
	 * <p>
	 * ConnectorProfileとなる候補が複数ある場合には、ダイアログを表示し、ConnectorProfileを作成する。
	 */
	public ConnectorProfile getConnectorProfile(OutPort outport, InPort inport, boolean isReverse) {
		this.outport = outport;
		this.inport = inport;

		String outName = (outport != null) ? outport.getNameL() : "none";
		String inName = (inport != null) ? inport.getNameL() : "none";

		connectorProfile = ComponentFactory.eINSTANCE.createConnectorProfile();
		if(isReverse) {
			connectorProfile.setName(inName + "_" + outName);
		} else {
			connectorProfile.setName(outName + "_" + inName);
		}
		connectorProfile.setIsReverse(isReverse);

		setShellStyle(this.getShellStyle() | SWT.RESIZE);
		open();

		return dialogResult;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		GridLayout gl;
		GridData gd;

		disableNotify = false;

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		gl = new GridLayout();
		gd = new GridData(GridData.FILL_BOTH);
		mainComposite.setLayout(gl);
		mainComposite.setLayoutData(gd);

		Label label = createLabel(mainComposite, Messages
				.getString("ConnectorCreaterDialogBase.1"));
		GridData labelLayloutData = new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(labelLayloutData);
		labelLayloutData.heightHint = 20;

		createConnectorProfileComposite(mainComposite);

		return mainComposite;
	}

	/**
	 * メインとなる表示部を作成する
	 */
	private void createConnectorProfileComposite(final Composite mainComposite) {
		GridData gd;
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;

		parseProperty();

		TabFolder tabFolder = new TabFolder(mainComposite, SWT.NONE);
		tabFolder.setLayoutData(gd);
		/////
		int tabIdx = 0;
		TabItem itemBasic = new TabItem(tabFolder, SWT.NONE, tabIdx);
		itemBasic.setText(Messages.getString("DataConnectorCreaterDialog.tab.basic"));
		itemBasic.setControl(createBasicTab(tabFolder));
		/////
		tabIdx++;
		TabItem itemBuffer = new TabItem(tabFolder, SWT.NONE, tabIdx);
		itemBuffer.setText(Messages.getString("DataConnectorCreaterDialog.tab.buffer"));
		itemBuffer.setControl(createBufferTab(tabFolder));
		/////
		if(existIFOpt) {
			tabIdx++;
			TabItem itemInterface = new TabItem(tabFolder, SWT.NONE, tabIdx);
			itemInterface.setText(Messages.getString("DataConnectorCreaterDialog.tab.interface"));
			itemInterface.setControl(createInterfaceTab(tabFolder));
		}
		/////
		tabIdx++;
		TabItem itemProperty = new TabItem(tabFolder, SWT.NONE, tabIdx);
		itemProperty.setText(Messages.getString("DataConnectorCreaterDialog.tab.properties"));
		itemProperty.setControl(createPropertyTab(tabFolder));
		
		loadData();
	}

	private Composite createBasicTab(TabFolder tabFolder) {
		GridData gd;
		GridLayout gl;
		Composite portProfileEditComposite = new Composite(tabFolder,
				SWT.NONE);
		gl = new GridLayout(3, false);
		gl.marginBottom = 3;
		gl.marginHeight = 3;
		gl.marginLeft = 3;
		gl.marginRight = 3;
		gl.marginTop = 3;
		gl.marginWidth = 3;
		portProfileEditComposite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		portProfileEditComposite.setLayoutData(gd);

		int style;

		Label name = new Label(portProfileEditComposite, SWT.NONE);
		name.setText("Name :"); //$NON-NLS-1$
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
		/////
		Composite portDataTypeComposite = new Composite(portProfileEditComposite,
				SWT.NONE);
		gl = new GridLayout(4, false);
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 5;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		portDataTypeComposite.setLayout(gl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 3;
		portDataTypeComposite.setLayoutData(gd);
		
		Label dataTypeOutLabel = new Label(portDataTypeComposite, SWT.NONE);
		dataTypeOutLabel.setText("Data Type OutPort :"); //$NON-NLS-1$
		Combo dataTypeOutCombo = new Combo(portDataTypeComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		dataTypeOutCombo.setLayoutData(gd);
		if(outport != null) {
			List<String> outportTypes = outport.getDataTypes();
			dataTypeOutCombo.setItems(outportTypes.toArray(new String[0]));
			if(0<outportTypes.size()) dataTypeOutCombo.select(0);
		}
		
		Label dataTypeInLabel = new Label(portDataTypeComposite, SWT.NONE);
		dataTypeInLabel.setText("InPort :"); //$NON-NLS-1$
		Combo dataTypeInCombo = new Combo(portDataTypeComposite, SWT.DROP_DOWN | SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		dataTypeInCombo.setLayoutData(gd);
		if(inport != null) {
			List<String> inportTypes = inport.getDataTypes();
			dataTypeInCombo.setItems(inportTypes.toArray(new String[0]));
			if(0<inportTypes.size()) dataTypeInCombo.select(0);
		}
		/////
		Label interfaceTypeLabel = new Label(portProfileEditComposite, SWT.NONE);
		interfaceTypeLabel.setText("Interface Type :"); //$NON-NLS-1$
		style = ConnectorUtil.isAllowAnyInterfaceType(outport, inport) ? SWT.DROP_DOWN
				: SWT.DROP_DOWN | SWT.READ_ONLY;
		interfaceTypeCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		interfaceTypeCombo.setLayoutData(gd);
		interfaceTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setInterfaceType(interfaceTypeCombo.getText());
				if(existIFOpt) {
					buildInterfaceOptionTab();
				}
				notifyModified();
			}
		});
		Label interfaceTypeFooterLabel = new Label(portProfileEditComposite,
				SWT.NONE);
		interfaceTypeFooterLabel.setText(ConnectorUtil
				.isAllowAnyInterfaceType(outport, inport) ? Messages.getString("DataConnectorCreaterDialog.2") : ""); //$NON-NLS-1$ //$NON-NLS-2$
		
		Label serializerTypeLabel = new Label(portProfileEditComposite, SWT.NONE);
		serializerTypeLabel.setText("Marshaling Type :"); //$NON-NLS-1$
		style = ConnectorUtil.isAllowAnyDataType(outport, inport) ? SWT.DROP_DOWN
				: SWT.DROP_DOWN | SWT.READ_ONLY;
		serializerTypeCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		serializerTypeCombo.setLayoutData(gd);
		serializerTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setDataType(serializerTypeCombo.getText());
				notifyModified();
			}
		});
		serializerTypeCombo.addSelectionListener(new SelectionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selected = serializerTypeCombo.getSelectionIndex();
				List<SerializerInfo> serTypes = (List<SerializerInfo>)serializerTypeCombo.getData();
				connectorProfile.setDataType(serTypes.get(selected).dataType);
				if( serTypes.get(selected).useSerializer ) {
					connectorProfile.setInportSerializerType(serTypes.get(selected).inPortSerializer);
					connectorProfile.setOutportSerializerType(serTypes.get(selected).outPortSerializer);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		Label serializerTypeFooterLabel = new Label(portProfileEditComposite,
				SWT.NONE);
		serializerTypeFooterLabel.setText(ConnectorUtil.isAllowAnyDataType(
				outport, inport) ? Messages.getString("DataConnectorCreaterDialog.2") : ""); //$NON-NLS-1$ //$NON-NLS-2$

		Label dataflowTypeLabel = new Label(portProfileEditComposite, SWT.NONE);
		dataflowTypeLabel.setText("Dataflow Type :"); //$NON-NLS-1$
		style = ConnectorUtil.isAllowAnyDataflowType(outport, inport) ? SWT.DROP_DOWN
				: SWT.DROP_DOWN | SWT.READ_ONLY;
		dataflowTypeCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		dataflowTypeCombo.setLayoutData(gd);
		dataflowTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setDataflowType(dataflowTypeCombo.getText());
				if (!connectorProfile.isSubscriptionTypeAvailable()) {
					subscriptionTypeCombo.select(0);
				}
				notifyModified();
			}
		});
		dataflowTypeCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selected = dataflowTypeCombo.getSelectionIndex();
				int selectedDir = directionCombo.getSelectionIndex();
				directionLabel2.setText("");
				if(selected == 0) {
					if((selectedDir == 0 && connectorProfile.isIsReverse())
							|| (selectedDir == 1 && connectorProfile.isIsReverse() == false)) {
						directionLabel2.setText(outport2inPort);
					}
				} else {
					if((selectedDir == 1 && connectorProfile.isIsReverse())
							|| (selectedDir == 0 && connectorProfile.isIsReverse() == false)) {
						directionLabel2.setText(inport2outPort);
					}
				}
				directionLabel2.pack();
				portProfileEditComposite.layout();
				
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		Label dataflowTypeFooterLabel = new Label(portProfileEditComposite,
				SWT.NONE);
		dataflowTypeFooterLabel.setText(ConnectorUtil
				.isAllowAnyDataflowType(outport, inport) ? Messages.getString("DataConnectorCreaterDialog.2") : ""); //$NON-NLS-1$ //$NON-NLS-2$

		Label subscriptionTypeLabel = new Label(portProfileEditComposite,
				SWT.NONE);
		subscriptionTypeLabel.setText("Subscription Type :"); //$NON-NLS-1$
		style = ConnectorUtil.isAllowAnySubscriptionType(outport, inport) ? SWT.DROP_DOWN
				: SWT.DROP_DOWN | SWT.READ_ONLY;
		subscriptionTypeCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		subscriptionTypeCombo.setLayoutData(gd);
		subscriptionTypeCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setSubscriptionType(subscriptionTypeCombo
						.getText());
				if (!connectorProfile.isPushIntervalAvailable()) {
					pushRateText.setText("");
				} else {
					if (pushRateText.getText().isEmpty()) {
						pushRateText.setText("1000.0");
					}
				}
				if (!connectorProfile.isPushPolicyAvailable()) {
					pushPolicyCombo.select(0);
				}
				notifyModified();
			}
		});
		Label subscriptionTypeFooterLabel = new Label(portProfileEditComposite,
				SWT.NONE);
		subscriptionTypeFooterLabel.setText(ConnectorUtil
				.isAllowAnySubscriptionType(outport, inport) ? Messages.getString("DataConnectorCreaterDialog.2") : ""); //$NON-NLS-1$ //$NON-NLS-2$
		subscriptionTypeCombo.setEnabled(false);

		Label pushRate = new Label(portProfileEditComposite, SWT.NONE);
		pushRate.setText("Push Rate(Hz) :"); //$NON-NLS-1$
		pushRateText = new Text(portProfileEditComposite, SWT.SINGLE
				| SWT.BORDER);
		pushRateText.setEnabled(false);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pushRateText.setLayoutData(gd);
		pushRateText.setTextLimit(9);
		pushRateText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = pushRateText.getText();

				boolean isDouble = false;
				try {
					Double.parseDouble(text);
					isDouble = true;
				} catch (Exception ex) {
					// void
				}

				if (isDouble) {
					connectorProfile.setPushRate(Double.parseDouble(text));
				}

				notifyModified();
			}
		});
		Label footerLabel = new Label(portProfileEditComposite, SWT.NONE);
		footerLabel.setText("");

		// Push Policy
		createLabel(portProfileEditComposite, "Push Policy :");
		style = SWT.DROP_DOWN | SWT.READ_ONLY;
		pushPolicyCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pushPolicyCombo.setLayoutData(gd);
		pushPolicyCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setPushPolicy(pushPolicyCombo.getText());
				if (!connectorProfile.isSkipCountAvailable()) {
					skipCountText.setText("");
				} else {
					if (skipCountText.getText().isEmpty()) {
						skipCountText.setText("0");
					}
				}

				notifyModified();
			}
		});
		createLabel(portProfileEditComposite, "");

		// Skip Count
		createLabel(portProfileEditComposite, "Skip Count :");
		skipCountText = new Text(portProfileEditComposite, SWT.SINGLE
				| SWT.BORDER);
		skipCountText.setEnabled(false);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		skipCountText.setLayoutData(gd);
		skipCountText.setTextLimit(9);
		skipCountText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = skipCountText.getText();
				try {
					int i = Integer.parseInt(text);
					connectorProfile.setSkipCount(i);
				} catch (Exception ex) {
					// void
				}
				notifyModified();
			}
		});
		createLabel(portProfileEditComposite, "");

		// TimeStamp Policy
		createLabel(portProfileEditComposite, "TimeStamp Policy :");
		style = SWT.DROP_DOWN | SWT.READ_ONLY;
		timePolicyCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		timePolicyCombo.setLayoutData(gd);
		timePolicyCombo.add("");
		timePolicyCombo.add("on_write");
		timePolicyCombo.add("on_send");
		timePolicyCombo.add("on_received");
		timePolicyCombo.add("on_read");
		timePolicyCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				connectorProfile.setTimestampPolicy(timePolicyCombo.getText());
				notifyModified();
			}
		});
		createLabel(portProfileEditComposite, "");
		
		//Direction
		createLabel(portProfileEditComposite, "Connect Direction :");
		style = SWT.DROP_DOWN | SWT.READ_ONLY;
		directionCombo = new Combo(portProfileEditComposite, style);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		directionCombo.setLayoutData(gd);
		if(connectorProfile.isIsReverse()) {
			directionCombo.add(Messages.getString("DataConnectorCreaterDialog.combo.direction1"));
			directionCombo.add(Messages.getString("DataConnectorCreaterDialog.combo.direction2"));
		} else {
			directionCombo.add(Messages.getString("DataConnectorCreaterDialog.combo.direction2"));
			directionCombo.add(Messages.getString("DataConnectorCreaterDialog.combo.direction1"));
		}
		directionCombo.select(0);
		directionCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selected = directionCombo.getSelectionIndex();
				if(selected == 0) {
					if(connectorProfile.isIsReverse()) {
						directionLabel.setText(inport2outPort);
						if(dataflowTypeCombo.getSelectionIndex() == 0) {
							directionLabel2.setText(outport2inPort);
						} else {
							directionLabel2.setText("");
						}
						directionLabel2.pack();
						portProfileEditComposite.layout();
					} else {
						directionLabel.setText(outport2inPort);
						directionLabel2.setText("");
					}
				} else {
					if(connectorProfile.isIsReverse()) {
						directionLabel.setText(outport2inPort);
						directionLabel2.setText("");

					} else {
						directionLabel.setText(inport2outPort);
						if(dataflowTypeCombo.getSelectionIndex() == 0) {
							directionLabel2.setText(outport2inPort);
						} else {
							directionLabel2.setText("");
						}
						directionLabel2.pack();
						portProfileEditComposite.layout();
					}
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		createLabel(portProfileEditComposite, "");
		//
		String outportIP = getPortInfo(outport, false, false);
		String inportIP = getPortInfo(inport, false, false);
		String outportIPPort = getPortInfo(outport, false, true);
		String inportIPPort = getPortInfo(inport, false, true);
		
		outport2inPort = Messages.getString("DataConnectorCreaterDialog.label.direction2_1")
				+ " "
				+ outportIP
				+ " "
				+ Messages.getString("DataConnectorCreaterDialog.label.direction2_2")
				+ inportIPPort
				+ " "
				+ Messages.getString("DataConnectorCreaterDialog.label.direction2_3");
		inport2outPort = Messages.getString("DataConnectorCreaterDialog.label.direction1_1")
				+ " "
				+ inportIP
				+ " "
				+ Messages.getString("DataConnectorCreaterDialog.label.direction1_2")
				+ outportIPPort
				+ " "
				+ Messages.getString("DataConnectorCreaterDialog.label.direction1_3"); 
		
		createLabel(portProfileEditComposite, "");
		directionLabel = new Label(portProfileEditComposite, SWT.WRAP);
		if(connectorProfile.isIsReverse()) {
			directionLabel.setText(inport2outPort);
		} else {
			directionLabel.setText(outport2inPort);
		}
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		directionLabel.setLayoutData(gd);
		
		createLabel(portProfileEditComposite, "");
		directionLabel2 = new Label(portProfileEditComposite, SWT.WRAP);
		if(connectorProfile.isIsReverse()) {
			directionLabel2.setText(outport2inPort);
		}
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = 2;
		directionLabel2.setLayoutData(gd);

		
		return portProfileEditComposite;
	}

	/**
	 * 詳細設定の表示部を作成する
	 */
	private Composite createBufferTab(Composite parent) {
		GridLayout gl;
		GridData gd;

		Composite detailComposite = new Composite(parent, SWT.NONE);
		gl = new GridLayout(2, false);
		gd = new GridData(GridData.FILL_BOTH);
		detailComposite.setLayout(gl);
		detailComposite.setLayoutData(gd);
		detailComposite.setVisible(false);
		/////
		ob = new BufferPackage();
		createBufferComposite(detailComposite, "Buffer (Outport)", ob);

		ib = new BufferPackage();
		createBufferComposite(detailComposite, "Buffer (Inport)", ib);
		
		loadDetailData();

		return detailComposite;
	}
	/**
	 * ポートバッファ設定の表示部を作成する
	 */
	private Composite createBufferComposite(Composite parent, String label,
			BufferPackage pkg) {
		GridLayout gl;
		GridData gd;

		Group composite = new Group(parent, SWT.NONE);
		gl = new GridLayout(3, false);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		composite.setLayout(gl);
		composite.setLayoutData(gd);
		composite.setText(label);

		final boolean isOutport = (pkg == ob);

		// Buffer length
		createLabel(composite, "Buffer length :");
		pkg.lengthText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pkg.lengthText.setLayoutData(gd);
		pkg.lengthText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				BufferPackage bp = (isOutport) ? ob : ib;
				String text = bp.lengthText.getText();
				try {
					int i = Integer.parseInt(text);
					if (isOutport) {
						connectorProfile.setOutportBufferLength(i);
					} else {
						connectorProfile.setInportBufferLength(i);
					}
				} catch (Exception ex) {
					// void
				}
				notifyModified();
			}
		});
		createLabel(composite, "");

		// Buffer full policy
		createLabel(composite, "Buffer full policy :");
		pkg.fullPolicyCombo = new Combo(composite, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pkg.fullPolicyCombo.setLayoutData(gd);
		pkg.fullPolicyCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				BufferPackage bp = (isOutport) ? ob : ib;
				String value = bp.fullPolicyCombo.getText();
				if (isOutport) {
					connectorProfile.setOutportBufferFullPolicy(value);
				} else {
					connectorProfile.setInportBufferFullPolicy(value);
				}
				notifyModified();
			}
		});
		createLabel(composite, "");

		// Buffer write timeout
		createLabel(composite, "Buffer write timeout :");
		pkg.writeTimeoutText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pkg.writeTimeoutText.setLayoutData(gd);
		pkg.writeTimeoutText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				BufferPackage bp = (isOutport) ? ob : ib;
				String text = bp.writeTimeoutText.getText();
				try {
					double d = Double.parseDouble(text);
					if (isOutport) {
						connectorProfile.setOutportBufferWriteTimeout(d);
					} else {
						connectorProfile.setInportBufferWriteTimeout(d);
					}
				} catch (Exception ex) {
					// void
				}
				notifyModified();
			}
		});
		createLabel(composite, "");

		// Buffer empty policy
		createLabel(composite, "Buffer empty policy :");
		pkg.emptyPolicyCombo = new Combo(composite, SWT.DROP_DOWN
				| SWT.READ_ONLY);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pkg.emptyPolicyCombo.setLayoutData(gd);
		pkg.emptyPolicyCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				BufferPackage bp = (isOutport) ? ob : ib;
				String value = bp.emptyPolicyCombo.getText();
				if (isOutport) {
					connectorProfile.setOutportBufferEmptyPolicy(value);
				} else {
					connectorProfile.setInportBufferEmptyPolicy(value);
				}
				notifyModified();
			}
		});
		createLabel(composite, "");

		// Buffer read timeout
		createLabel(composite, "Buffer read timeout :");
		pkg.readTimeoutText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		pkg.readTimeoutText.setLayoutData(gd);
		pkg.readTimeoutText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				BufferPackage bp = (isOutport) ? ob : ib;
				String text = bp.readTimeoutText.getText();
				try {
					double d = Double.parseDouble(text);
					if (isOutport) {
						connectorProfile.setOutportBufferReadTimeout(d);
					} else {
						connectorProfile.setInportBufferReadTimeout(d);
					}
				} catch (Exception ex) {
					// void
				}
				notifyModified();
			}
		});
		createLabel(composite, "");

		return composite;
	}

	private class PropertyElem {
		private String name;
		private List<String> portType;
		private int type;
		private List<String> elemList;
		private List<String> constraints;
		private ConfigurationCondition condition;
		private Boolean isError;
		
		private List<String> selectedValue = new ArrayList<String>();
		
		private String getSelectedValue() {
			StringBuilder builder = new StringBuilder();
			
			for(String each : selectedValue) {
				if(0 < builder.length()) {
					builder.append(", ");
				}
				builder.append(each);
			}
			
			return builder.toString();
		}
	}

	private Composite createPropertyTab(Composite parent) {
		
		GridLayout gl;
		GridData gd;

		Composite propertyComposite = new Composite(parent, SWT.NONE);
		gl = new GridLayout(1, false);
		gd = new GridData(GridData.FILL_BOTH);
		propertyComposite.setLayout(gl);
		propertyComposite.setLayoutData(gd);
		propertyComposite.setVisible(true);
		/////
		additionalTableViewer = createAdditionalTableViewer(propertyComposite);
		
		return propertyComposite;
	}
	
	private Composite createInterfaceTab(Composite parent) {
		
		GridLayout gl;
		GridData gd;

		Composite propertyComposite = new Composite(parent, SWT.NONE);
		gl = new GridLayout(1, false);
		gd = new GridData(GridData.FILL_BOTH);
		propertyComposite.setLayout(gl);
		propertyComposite.setLayoutData(gd);
		propertyComposite.setVisible(true);
		/////
		propertyScrollArea = new ScrolledComposite(propertyComposite, SWT.V_SCROLL);
		propertyScrollArea.setLayout(new FillLayout());
		propertyScrollArea.setExpandHorizontal(true);
		propertyScrollArea.setExpandVertical(true);
		propertyScrollArea.setLayoutData(gd);
		propertyScrollArea.setMinHeight(400);
		buildInterfaceOptionTab();
		
		return propertyComposite;
	}

	private void buildInterfaceOptionTab() {
		if( propertyScrollArea.getContent() != null ) {
			propertyScrollArea.setContent(null);
		}
		GridLayout gl = new GridLayout(1, false);
		GridData gd = new GridData(GridData.FILL_BOTH);
		Composite proprtyComposite = new Composite(propertyScrollArea, SWT.NONE);
		proprtyComposite.setLayout(gl);
		proprtyComposite.setLayoutData(gd);
		
		String ifType = interfaceTypeCombo.getText();
		List<PropertyElem> propList = propertyList.get(ifType);
		if(propList != null && 0 < propList.size()) {
			if(propList.size() <= 10) {
				for(PropertyElem prop : propList) {
					createProprtyListComposite(proprtyComposite, prop, 0);
				}
			} else {
				Map<String, List<PropertyElem>> localPropertyList = new HashMap<String, List<PropertyElem>>();
				int nameIdx = 0;
				for(int index=0; index<4; index++) {
					for(PropertyElem prop : propList) {
						String[] elems = prop.name.split("\\.");
						StringBuilder builder = new StringBuilder();
						for(int idxElem = 0; idxElem < nameIdx; idxElem++) {
							if(elems.length <= idxElem) continue;
							if(0<builder.length()) {
								builder.append(".");
							}
							builder.append(elems[idxElem]);
						}
						String newKey = builder.toString();
						List<PropertyElem> propListLc;
						if(localPropertyList.keySet().contains(newKey)) {
							propListLc = localPropertyList.get(newKey);
						} else {
							propListLc = new ArrayList<DataConnectorCreaterDialog.PropertyElem>();
							localPropertyList.put(newKey, propListLc);
						}
						propListLc.add(prop);
						if(index < 3 && 10 < propListLc.size()) {
							localPropertyList.clear();
							nameIdx++;
							break;
						}
					}
				}
				//
				for(String section : localPropertyList.keySet()) {
					Composite composite = createOptionSection(proprtyComposite, section);

					List<PropertyElem> propListLc = localPropertyList.get(section);
					Collections.sort(propListLc, new Comparator<PropertyElem>() {
						public int compare(PropertyElem a, PropertyElem b) {
							return a.name.compareTo(b.name);
						}
					});
					for(PropertyElem prop : propListLc) {
						createProprtyListComposite(composite, prop, nameIdx);
					}
				}
			}
		}
		propertyScrollArea.setContent(proprtyComposite);
		int minSize = proprtyComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		if(minSize < 400) {
			minSize = 400;
		}
		propertyScrollArea.setMinHeight(minSize);
		propertyScrollArea.layout(true, true);
	}

	private Composite createOptionSection(Composite parentComposite, String sectionName) {
		GridLayout gl;
		GridData gd;
		FormToolkit toolkit = new FormToolkit(getShell().getDisplay());
		Section sctProperty = toolkit.createSection(parentComposite,
				Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		sctProperty.setText(sectionName);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gl = new GridLayout(1, false);
		gl.verticalSpacing = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		gl.marginBottom = 0;
		sctProperty.setLayout(gl);
		sctProperty.setLayoutData(gridData);
		//
		Composite composite = toolkit.createComposite(sctProperty, SWT.NULL);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(composite);
		gl = new GridLayout(1, false);
		gl.marginTop = 0;
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.verticalSpacing = 2;
		composite.setLayout(gl);
		gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		sctProperty.setClient(composite);
		return composite;
	}
	
	private void createProprtyListComposite(Composite parent, PropertyElem prop, int nameLength) {
		GridLayout gl;
		gl = new GridLayout(2, false);
		gl.verticalSpacing = 0;
		gl.marginTop = 2;
		gl.marginBottom = 2;
		gl.marginHeight = 0;

		GridData gd;
		gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.CENTER;

		Composite namedValueGroup = new Composite(parent, SWT.BORDER);
		namedValueGroup.setLayout(gl);
		namedValueGroup.setLayoutData(gd);

		gd = new GridData();
		gd.widthHint = NAME_WIDTH;

		String strName = prop.name;
		if(0<nameLength) {
			String[] elems = strName.split("\\.");
			StringBuilder builder = new StringBuilder();
			for(int index=nameLength; index<elems.length; index++) {
				if(0<builder.length()) {
					builder.append(".");
				}
				builder.append(elems[index]);
			}
			strName = builder.toString();
			if(strName.length() == 0) {
				strName = elems[elems.length-1];
			}
		}
		Label keyLabel = new Label(namedValueGroup, SWT.WRAP | SWT.CENTER);
		keyLabel.setText(strName);
		keyLabel.setLayoutData(gd);

		gl = new GridLayout(1, false);
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		gl.marginTop = 0;
		gl.verticalSpacing = 0;

		Composite proprtyComposite = new Composite(namedValueGroup, SWT.NONE);
		proprtyComposite.setLayout(gl);
		proprtyComposite.setLayoutData(createGridData());

		createProprtyComposite(proprtyComposite, prop);
	}
	
	private void createProprtyComposite(final Composite parent, PropertyElem prop) {

		switch (prop.type) {
		case 1: {
			// widget種別がcomboboxの場合
			Combo valueCombo = createCombo(parent);
			valueCombo.addSelectionListener(new SelectionListener() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					prop.selectedValue.clear();
					prop.selectedValue.add(valueCombo.getText());
				}
				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
				}
			});
			valueCombo.add("");
			int selectedIndex = 0;
			if(prop.constraints != null) {
				for (int index=0; index<prop.constraints.size(); index++) {
					String s = prop.constraints.get(index);
					valueCombo.add(s);
					if(prop.elemList != null ) {
						if(prop.elemList.contains(s)) {
							selectedIndex = index + 1;
						}
					}
				}
			}
			valueCombo.select(selectedIndex);
			prop.selectedValue.clear();
			prop.selectedValue.add(valueCombo.getText());
			break;
		}
		case 2: {
			// widget種別がCheckBoxの場合
			Composite valueCheckBoxGroup = createGroup(parent);
			SelectionListener sl = createCheckBoxSelectionListner(prop);
			if(prop.constraints != null) {
				for (String s : prop.constraints) {
					Button vb = createButton(valueCheckBoxGroup, SWT.CHECK);
					vb.setText(s);
					vb.addSelectionListener(sl);
					if( prop.elemList != null ) {
						if(prop.elemList.contains(s)) {
							vb.setSelection(true);
							if( prop.selectedValue.contains(s)==false) {
								prop.selectedValue.add(s);
							}
						}
					}
				}
			}
			break;
		}
		case 3: {
			// widget種別がradioの場合
			Composite valueRadioGroup = createGroup(parent);
			SelectionListener sl = createRadioSelectionListner(prop);
			if(prop.constraints != null) {
				for (String s : prop.constraints) {
					Button vb = createButton(valueRadioGroup, SWT.RADIO);
					vb.setText(s);
					vb.addSelectionListener(sl);
					if( prop.elemList != null ) {
						if(prop.elemList.contains(s)) {
							vb.setSelection(true);
							prop.selectedValue.add(s);
						}
					}
				}
			}
			break;
		}
		default: {
			// widget種別がtextの場合
			final Text valueText = createText(parent);
			valueText.setTextLimit(255);
			valueText.setEnabled(true);
			StringBuilder builder = new StringBuilder();
			if(prop.elemList == null) {
				prop.elemList = new ArrayList<String>();
			}
			for(String each : prop.elemList) {
				if(0 < builder.length()) {
					builder.append(",");
				}
				builder.append(each);
			}
			valueText.setText(builder.toString());
			prop.selectedValue.add(builder.toString());
				
			if(prop.constraints != null && 0 < prop.constraints.size()) {
				String cond = prop.constraints.get(0);
				if(cond != null && 0 < cond.trim().length()) {
					try {
						prop.condition = ConfigurationCondition.parse(cond);
						if (!prop.condition.validate(valueText.getText())) {
							valueText.setToolTipText(Messages.getString("ConfigurationDialog.6") + prop.condition + Messages.getString("ConfigurationDialog.7")); //$NON-NLS-1$ //$NON-NLS-2$
							valueText.setBackground(colorRegistry.get(ERROR_COLOR));
							prop.isError = true;
						} else {
							if(prop.condition.toString().equals("null") == false) {
								valueText.setToolTipText(prop.condition.toString()); //$NON-NLS-1$ //$NON-NLS-2$
							}
						}
					} catch (ConfigurationCondition.NumberException e) {
					} catch (ConfigurationCondition.FormatException e) {
					}
				}
			}
			valueText.addFocusListener(createFocusListner(prop, valueText));
			break;
		}
		}
	}
	
	private FocusListener createFocusListner(final PropertyElem prop, final Text valueText) {
		return new FocusListener(){
			public void focusGained(FocusEvent e) {
			}

			public void focusLost(FocusEvent e) {
				ConfigurationCondition condition = prop.condition;
				String value = valueText.getText();
				prop.selectedValue.add(value);
				if(condition!=null) {
					if (!condition.validate(value)) {
						valueText.setToolTipText(Messages.getString("ConfigurationDialog.6") + condition + Messages.getString("ConfigurationDialog.7")); //$NON-NLS-1$ //$NON-NLS-2$
						valueText.setBackground(colorRegistry.get(ERROR_COLOR));
						prop.isError = true;
						prop.selectedValue.clear();
					} else {
						if(prop.condition.toString().equals("null") == false) {
							valueText.setToolTipText(prop.condition.toString()); //$NON-NLS-1$ //$NON-NLS-2$
						} else {
							valueText.setToolTipText(null);
						}
						valueText.setBackground(colorRegistry.get(NORMAL_COLOR));
						prop.isError = false;
					}
				}
				checkConstraint();
			}};
	}

	private void checkConstraint() {
		boolean existError = false;
		for(String key : propertyList.keySet() ) {
			List<PropertyElem> propList = propertyList.get(key);
			for(PropertyElem each : propList) {
				if(each.condition == null) continue;
				if(each.isError) {
					existError = true;
					break;
				}
			}
			if(existError) break;
		}
		getButton(IDialogConstants.OK_ID).setEnabled(!existError);
	}
	
	private GridData createGridData() {
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.CENTER;
		return gd;
	}
	
	private Composite createGroup(final Composite parent) {
		Composite group = new Composite(parent, SWT.BORDER);

		GridLayout gl = new GridLayout(3, false);
		gl.marginTop = 2;
		gl.marginHeight = 0;
		gl.marginBottom = 2;
		gl.verticalSpacing = 2;
		group.setLayout(gl);
		
		group.setLayoutData(createGridData());
		
		return group;
	}
	private Button createButton(final Composite parent, int style) {
		Button button = new Button(parent, style);
		
		button.setLayoutData(createGridData());
		
		return button;
	}
	private Combo createCombo(final Composite parent) {
		Combo result = new Combo(parent, SWT.READ_ONLY);
		
		result.setLayoutData(createGridData());
		
		return result;
	}
	private Label createLabel(Composite parent, String label) {
		Label l = new Label(parent, SWT.NONE);
		l.setText(label);
		return l;
	}
	private Text createText(final Composite parent) {
		Text text = new Text(parent, SWT.SINGLE
				| SWT.BORDER);
		
		text.setLayoutData(createGridData());
		
		return text;
	}

	private SelectionListener createRadioSelectionListner(PropertyElem prop) {
		return new SelectionListener() {
			PropertyElem wd = prop;

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				if (b.getSelection()) {
					String value = b.getText();
					wd.selectedValue.clear();
					wd.selectedValue.add(value);
				}
			}
		};
	}
	
	private SelectionListener createCheckBoxSelectionListner(PropertyElem prop) {
		return new SelectionListener() {
			PropertyElem wd = prop;

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				Button b = (Button) e.widget;
				String value = b.getText();
				if (b.getSelection()) {
					if( wd.selectedValue.contains(value)==false) {
						wd.selectedValue.add(value);
					}
				} else {
					wd.selectedValue.remove(value);
				}
			}
		};
	}
	
	private void parseProperty() {
		propertyList.clear();
		List<NameValue> basePropList;
		Port targetPort;
		Port basePort;
		String targetPortStr = "";
		String basePortStr = "";
		if( outport != null ) {
			basePropList = outport.getProperties();
			basePort = outport;
			basePortStr = "outport";
			if(inport!=null) {
				targetPort = inport;
				targetPortStr = "inport";
			} else {
				targetPort = outport;
			}
		} else {
			basePropList = inport.getProperties();
			basePort = inport;
			basePortStr = "inport";
			if(outport!=null) {
				targetPort = outport;
				targetPortStr = "outport";
			} else {
				targetPort = inport;
			}
		}
		
		String interfaceType = targetPort.getInterfaceType();
		String[] ifTypes = interfaceType.split(",");
		for(String each : ifTypes) {
			propertyList.put(each.trim(), new ArrayList<DataConnectorCreaterDialog.PropertyElem>());
		}
		
		List<String> portType = new ArrayList<String>();
		for(NameValue each : basePropList) {
			portType.clear();
			String propName = each.getName();
			if(propName.startsWith("dataport.interface_option") == false) continue;

			String baseProp = each.getValue().trim();
			if(baseProp == null || baseProp.length() == 0) continue;
			
			portType.add(basePortStr);
			
			String targetProp = "Any";
			targetProp = targetPort.getProperty(each.getName());
			if(targetProp != null && 0 < targetProp.length()) {
				targetProp = targetProp.trim();
				portType.add(targetPortStr);
			} else {
				targetProp = "Any";
			}
			if(baseProp.equals("Any") && targetProp.equals("Any")) continue;
			
			List<String> elemList = new ArrayList<String>();
			boolean outPAny = false;
			boolean inPAny = false;
			if(baseProp.startsWith("[") && baseProp.endsWith("]")) {
				baseProp = baseProp.substring(1, baseProp.length() -1);
			} else if(baseProp.startsWith("(") && baseProp.endsWith(")")) {
				baseProp = baseProp.substring(1, baseProp.length() -1);
			} else {
				if(baseProp.equals("Any")) outPAny = true;
			}

			if(targetProp.startsWith("[") && targetProp.endsWith("]")) {
				targetProp = targetProp.substring(1, targetProp.length() -1);
			} else if(targetProp.startsWith("(") && targetProp.endsWith(")")) {
				targetProp = targetProp.substring(1, targetProp.length() -1);
			} else {
				if(targetProp.equals("Any")) inPAny = true;
			}
			
			String[] outPropElems = baseProp.split(",");
			outPropElems = StringUtils.stripAll(outPropElems);
			String[] inPropElems = targetProp.split(",");
			inPropElems = StringUtils.stripAll(inPropElems);
			if(outPAny) {
				elemList = Arrays.asList(inPropElems);
			} else if(inPAny) {
				elemList = Arrays.asList(outPropElems);
			} else {
				List<String> outElemList = Arrays.asList(outPropElems);
				List<String> inElemList = Arrays.asList(inPropElems);
				for(String elem : outElemList) {
					if(inElemList.contains(elem)) {
						elemList.add(elem);
					}
				}
			}
			if(elemList.size() == 0) continue; 

			String[] elems = propName.split("\\.");
			if(elems.length < 5) continue;
			setPropertyInfo(portType, elems, elemList);
		}
		
		for(NameValue each : targetPort.getProperties()) {
			String propName = each.getName();
			portType.clear();
			if(propName.startsWith("dataport.interface_option") == false) continue;

			String baseProp = each.getValue().trim();
			if(baseProp == null || baseProp.length() == 0) continue;
			
			if(basePort.getProperty(each.getName()) != null) continue;
			String targetProp = "Any";
			if(baseProp.equals("Any") && targetProp.equals("Any")) continue;
			
			portType.add(targetPortStr);
			
			List<String> elemList = new ArrayList<String>();
			if(baseProp.startsWith("[") && baseProp.endsWith("]")) {
				baseProp = baseProp.substring(1, baseProp.length() -1);
			} else if(baseProp.startsWith("(") && baseProp.endsWith(")")) {
				baseProp = baseProp.substring(1, baseProp.length() -1);
			}

			String[] outPropElems = baseProp.split(",");
			outPropElems = StringUtils.stripAll(outPropElems);
			elemList = Arrays.asList(outPropElems);
			
			if(elemList.size() == 0) continue; 

			String[] elems = propName.split("\\.");
			if(elems.length < 5) continue;
			setPropertyInfo(portType, elems, elemList);
		}
	}
	
	private void setPropertyInfo(List<String> portType, String[] elems, List<String> elemList) {
		List<PropertyElem> eachPropList = propertyList.get(elems[2]);
		StringBuilder builder = new StringBuilder();
		for(int index=3; index<elems.length; index++ ) {
			if(elems[index].startsWith("_")) break;
			if(0<builder.toString().length()) {
				builder.append(".");
			}
			builder.append(elems[index]);
		}
				
		String name = builder.toString();
		PropertyElem eachProp = null;
		for(PropertyElem prop : eachPropList) {
			if(prop.name.equals(name)) {
				eachProp = prop;
				break;
			}
		}
		if(eachProp == null) {
			eachProp = new PropertyElem();
			eachProp.name = name;
			eachProp.portType = new ArrayList<String>(portType);
			eachProp.isError = false;
			eachPropList.add(eachProp);
		}
		
		String tag = elems[elems.length - 1];
		if(tag.equals("__value__")) {
			eachProp.elemList = elemList;
		} else if(tag.equals("__widget__")) {
			String widget = elemList.get(0);
			if(widget.equals("radio")) {
				eachProp.type = 3;
			} else if(widget.equals("checkbox")) {
				eachProp.type = 2;
			} else if(widget.equals("combobox")) {
				eachProp.type = 1;
			} else {
				eachProp.type = 0;
			}
		} else if(tag.equals("__constraint__")) {
			eachProp.constraints = elemList;
		}
		existIFOpt = true;
	}
	
	private boolean isOffline() {
		if (inport != null) {
			return inport.eContainer() instanceof ComponentSpecification;
		} else if (outport != null) {
			return outport.eContainer() instanceof ComponentSpecification;
		}
		return false;
	}

	/**
	 * モデル情報にアクセスし、表示に設定する
	 */
	void loadData() {
		//
		nameText.setText(connectorProfile.getName());
		//
		List<SerializerInfo> serTypes = ConnectorUtil.getAllowDataTypes(outport, inport);
		boolean isAllowAny = ConnectorUtil.isAllowAnyDataType(outport, inport);
		int selected = 0;
		String outSer = connectorProfile.getOutportSerializerType();
		String inSer = connectorProfile.getInportSerializerType();
		serializerTypeCombo.removeAll();
		for(SerializerInfo each : serTypes) {
			serializerTypeCombo.add(each.toString());
			if(each.outPortSerializer.equals(outSer) && each.inPortSerializer.equals(inSer)) {
				selected = serializerTypeCombo.getItemCount() - 1;
			}
		}
		serializerTypeCombo.select(selected);
		connectorProfile.setDataType(serTypes.get(selected).dataType);
		if( serTypes.get(selected).useSerializer ) {
			connectorProfile.setInportSerializerType(serTypes.get(selected).inPortSerializer);
			connectorProfile.setOutportSerializerType(serTypes.get(selected).outPortSerializer);
		}
		serializerTypeCombo.setData(serTypes);

		SystemEditorPreferenceManager preference = SystemEditorPreferenceManager
				.getInstance();
		//
		List<String> types;
		if (!isOffline()) {
			types = ConnectorUtil.getAllowInterfaceTypes(outport, inport);
			isAllowAny = ConnectorUtil.isAllowAnyInterfaceType(outport, inport);
		} else {
			types = Arrays.asList(preference.getInterfaceTypes());
			isAllowAny = false;
		}
		String value = loadCombo(interfaceTypeCombo, types, connectorProfile
				.getInterfaceType(), isAllowAny);
		connectorProfile.setInterfaceType(value);
		//
		if (!isOffline()) {
			types = ConnectorUtil.getAllowDataflowTypes(outport, inport);
			isAllowAny = ConnectorUtil.isAllowAnyDataflowType(outport, inport);
		} else {
			types = Arrays.asList(preference.getDataFlowTypes());
			isAllowAny = false;
		}
		value = loadCombo(dataflowTypeCombo, types, connectorProfile
				.getDataflowType(), isAllowAny);
		connectorProfile.setDataflowType(value);
		//
		if (!isOffline()) {
			types = ConnectorUtil.getAllowSubscriptionTypes(outport, inport);
			isAllowAny = ConnectorUtil.isAllowAnySubscriptionType(outport,
					inport);
		} else {
			types = Arrays.asList(preference.getSubscriptionTypes());
			isAllowAny = false;
		}
		String defaultVal = connectorProfile.getSubscriptionType();
		if(defaultVal==null || defaultVal.length()==0) {
			defaultVal = "flush";
		}
		value = loadCombo(subscriptionTypeCombo, types, defaultVal, isAllowAny);
		connectorProfile.setSubscriptionType(value);
		//
		if (!isOffline()) {
			types = Arrays.asList(ConnectorProfile.PUSH_POLICY_TYPES);
		} else {
			types = Arrays.asList(preference.getPushPolicies());
		}
		isAllowAny = false;
		value = loadCombo(pushPolicyCombo, types, connectorProfile
				.getPushPolicy(), isAllowAny);
		connectorProfile.setPushPolicy(value);
		
		loadDetailData();
	}

	/**
	 * モデルの詳細設定項目を表示する
	 */
	void loadDetailData() {
		List<String> fullTypes;
		List<String> emptyTypes;
		String value;
		boolean isAllowAny = false;

		SystemEditorPreferenceManager preference = SystemEditorPreferenceManager
				.getInstance();

		if (!isOffline()) {
			fullTypes = Arrays
					.asList(ConnectorProfile.BUFFER_FULL_POLICY_TYPES);
			emptyTypes = Arrays
					.asList(ConnectorProfile.BUFFER_EMPTY_POLICY_TYPES);
		} else {
			fullTypes = Arrays.asList(preference.getBufferFullPolicies());
			emptyTypes = Arrays.asList(preference.getBufferEmptyPolicies());
		}
		
		if (ob != null && ob.enable) {
			//
			value = loadCombo(ob.fullPolicyCombo, fullTypes, connectorProfile
					.getOutportBufferFullPolicy(), isAllowAny);
			connectorProfile.setOutportBufferFullPolicy(value);
			//
			value = loadCombo(ob.emptyPolicyCombo, emptyTypes, connectorProfile
					.getOutportBufferEmptyPolicy(), isAllowAny);
			connectorProfile.setOutportBufferEmptyPolicy(value);
			//
			if (connectorProfile.getOutportBufferLength() == null) {
				connectorProfile.setOutportBufferLength(8);
			}
			value = connectorProfile.getOutportBufferLength().toString();
			ob.lengthText.setText(value);
			//
			if (connectorProfile.getOutportBufferWriteTimeout() == null) {
				connectorProfile.setOutportBufferWriteTimeout(1.0);
			}
			value = connectorProfile.getOutportBufferWriteTimeout().toString();
			ob.writeTimeoutText.setText(value);
			//
			if (connectorProfile.getOutportBufferReadTimeout() == null) {
				connectorProfile.setOutportBufferReadTimeout(1.0);
			}
			value = connectorProfile.getOutportBufferReadTimeout().toString();
			ob.readTimeoutText.setText(value);
		}
		//
		if (ib != null && ib.enable) {
			//
			value = loadCombo(ib.fullPolicyCombo, fullTypes, connectorProfile
					.getInportBufferFullPolicy(), isAllowAny);
			connectorProfile.setInportBufferFullPolicy(value);
			//
			value = loadCombo(ib.emptyPolicyCombo, emptyTypes, connectorProfile
					.getInportBufferEmptyPolicy(), isAllowAny);
			connectorProfile.setInportBufferEmptyPolicy(value);
			//
			if (connectorProfile.getInportBufferLength() == null) {
				connectorProfile.setInportBufferLength(8);
			}
			value = connectorProfile.getInportBufferLength().toString();
			ib.lengthText.setText(value);
			//
			if (connectorProfile.getInportBufferWriteTimeout() == null) {
				connectorProfile.setInportBufferWriteTimeout(1.0);
			}
			value = connectorProfile.getInportBufferWriteTimeout().toString();
			ib.writeTimeoutText.setText(value);
			//
			if (connectorProfile.getInportBufferReadTimeout() == null) {
				connectorProfile.setInportBufferReadTimeout(1.0);
			}
			value = connectorProfile.getInportBufferReadTimeout().toString();
			ib.readTimeoutText.setText(value);
		}
	}
	
	String loadCombo(Combo combo, List<String> types, String value,
			boolean isAllowAny) {
		combo.setItems(types.toArray(new String[0]));
		String def = getDefaultValue(types, value, isAllowAny);
		int index = types.indexOf(def);
		if (index != -1) {
			combo.select(index);
			return types.get(index);
		}
		return null;
	}

	/**
	 * コンボにおいて、「表示候補のリスト」と、「どのような文字列でも設定可能であるかどうか」を引数に取り、初期表示の文字列を決定する
	 * 
	 * @param candidateList
	 *            表示候補リスト
	 * @param isAllowAny
	 *            どのような文字列でも設定可能であるかどうか
	 * @return 初期表示の文字列
	 */
	private String getDefaultValue(List<String> candidateList, String value,
			boolean isAllowAny) {
		String result = null;
		if (candidateList.size() > 0) {
			if (candidateList.contains(value)) {
				result = value;
			} else {
				result = candidateList.get(0);
			}
		}
		if (result == null) {
			if (isAllowAny) {
				result = ConnectorProfile.ANY;
			}
		}
		return result;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Connector Profile"); //$NON-NLS-1$
		this.setHelpAvailable(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void okPressed() {
		String ifType = interfaceTypeCombo.getText();
		List<PropertyElem> propList = propertyList.get(ifType);
		for(PropertyElem each : propList) {
			String selected = each.getSelectedValue();
			if(selected != null && 0 < selected.length()) {
				for(String portTypeStr : each.portType) {
					String propName = "dataport." + portTypeStr + "." + each.name; 
					connectorProfile.setProperty(propName, each.getSelectedValue());
				}
			}
		}
		
		if(directionCombo.getSelectionIndex() == 1) {
			connectorProfile.setIsReverse(!connectorProfile.isIsReverse());
		}
		
		if (additionalTableViewer != null) {
			List<AdditionalEntry> additional = (List<AdditionalEntry>) additionalTableViewer
					.getInput();
			// 重複チェック
			if (checkProperties(additional) == false) {
				return;
			}
			for (AdditionalEntry target : additional) {
				connectorProfile.setProperty(target.getName(), target
						.getValue());
			}
		}
		dialogResult = connectorProfile;
		super.okPressed();
	}

	@Override
	/**
	 * メッセージを設定する。 メッセージとしてはエラーメッセージを想定しており、
	 * エラーメッセージが存在するか空文字かどうかにより、OKボタンのEnableの制御も行うように、オーバーライドした。
	 */
	public void setMessage(String newMessage, int newType) {
		super.setMessage(newMessage, newType);
		boolean isOkEnable = false;
		if (newMessage.length() == 0) {
			isOkEnable = true;
		}
		getButton(IDialogConstants.OK_ID).setEnabled(isOkEnable);
	}

	/**
	 * 設定に変更があった場合に呼び出されることを想定したメソッド。
	 * SubscriptionTypeコンボとPushInterbalのEnableを管理する。
	 * <p>
	 * 注意：設定値の変更がある場合には、必ずこのメソッドを呼び出すこと<br>
	 * 現在は、表示側で設定を変更した後に、このメソッドを必ず呼び出すように実装しているが、
	 * 項目数が増えるようならば、モデルの変更通知機能を使用して実装する方が良い。
	 */
	public void notifyModified() {
		if (disableNotify) {
			return;
		}
		if (getButton(IDialogConstants.OK_ID) != null) {
			setMessage("", IMessageProvider.NONE); //$NON-NLS-1$
		}

		if (connectorProfile.isPushIntervalAvailable()) {
			boolean isDouble = false;
			try {
				double value = Double.parseDouble(pushRateText.getText());
				if (value > 0) {
					isDouble = true;
				}
			} catch (Exception ex) {
				// void
			}

			if (!isDouble) {
				setMessage(Messages.getString("DataConnectorCreaterDialog.msg.push_rate"),
						IMessageProvider.ERROR);
			}
		}

		if (connectorProfile.isSkipCountAvailable()) {
			boolean isInt = false;
			try {
				int i = Integer.parseInt(skipCountText.getText());
				if (i >= 0) {
					isInt = true;
				}
			} catch (Exception ex) {
				// void
			}
			if (!isInt) {
				setMessage(Messages.getString("DataConnectorCreaterDialog.msg.skip_count"),
						IMessageProvider.ERROR);
			}
		}

		if (ob != null && ob.enable && 0 < ob.lengthText.getText().length()) {
			boolean isInt = false;
			try {
				int i = Integer.parseInt(ob.lengthText.getText());
				if (i >= 0) {
					isInt = true;
				}
			} catch (Exception ex) {
				// void
			}
			if (!isInt) {
				setMessage(Messages.getString("DataConnectorCreaterDialog.msg.outport_buff_length"),
						IMessageProvider.ERROR);
			}
			//
			boolean isDouble = false;
			if(0 < ib.lengthText.getText().length()) {
				try {
					double d = Double.parseDouble(ob.writeTimeoutText.getText());
					if (d >= 0.0) {
						isDouble = true;
					}
				} catch (Exception ex) {
					// void
				}
				if (!isDouble) {
					setMessage(Messages.getString("DataConnectorCreaterDialog.msg.outport_write_timeout"),
							IMessageProvider.ERROR);
				}
			}
			//
			isDouble = false;
			if(0 < ob.readTimeoutText.getText().length()) {
				try {
					double d = Double.parseDouble(ob.readTimeoutText.getText());
					if (d >= 0.0) {
						isDouble = true;
					}
				} catch (Exception ex) {
					// void
				}
				if (!isDouble) {
					setMessage(Messages.getString("DataConnectorCreaterDialog.msg.outport_read_timeout"),
							IMessageProvider.ERROR);
				}
			}
		}

		if (ib != null && ib.enable && 0 < ib.lengthText.getText().length()) {
			boolean isInt = false;
			try {
				int i = Integer.parseInt(ib.lengthText.getText());
				if (i >= 0) {
					isInt = true;
				}
			} catch (Exception ex) {
				// void
			}
			if (!isInt) {
				setMessage(Messages.getString("DataConnectorCreaterDialog.msg.inport_buff_length"),
						IMessageProvider.ERROR);
			}
			//
			boolean isDouble = false;
			if(0 < ib.writeTimeoutText.getText().length()) {
				try {
					double d = Double.parseDouble(ib.writeTimeoutText.getText());
					if (d >= 0.0) {
						isDouble = true;
					}
				} catch (Exception ex) {
					// void
				}
				if (!isDouble) {
					setMessage(Messages.getString("DataConnectorCreaterDialog.msg.inport_write_timeout"),
							IMessageProvider.ERROR);
				}
			}
			//
			isDouble = false;
			if(0 < ib.readTimeoutText.getText().length()) {
				try {
					double d = Double.parseDouble(ib.readTimeoutText.getText());
					if (d >= 0.0) {
						isDouble = true;
					}
				} catch (Exception ex) {
					// void
				}
				if (!isDouble) {
					setMessage(Messages.getString("DataConnectorCreaterDialog.msg.inport_read_timeout"),
							IMessageProvider.ERROR);
				}
			}
		}

		subscriptionTypeCombo.setEnabled(connectorProfile.isSubscriptionTypeAvailable());
		pushRateText.setEnabled(connectorProfile.isPushIntervalAvailable());
		pushPolicyCombo.setEnabled(connectorProfile.isPushPolicyAvailable());
		skipCountText.setEnabled(connectorProfile.isSkipCountAvailable());
	}

	@Override
	protected Point getInitialSize() {
		int height = 580;
		int width = 560;
		if(existIFOpt) {
			height = 830;
		}
		return getShell().computeSize(width, height, true);
	}

}
