package jp.go.aist.rtm.rtcbuilder.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataPortParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.preference.ComponentPreferenceManager;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;
import jp.go.aist.rtm.rtcbuilder.util.ValidationUtil;

/**
 * DataPortページ
 */
public class DataPortEditorFormPage extends AbstractEditorFormPage {

	private TableViewer inportTableViewer;
	private Button inportAddButton;
	private Button inportDeleteButton;
	//
	private TableViewer outportTableViewer;
	private Button outportAddButton;
	private Button outportDeleteButton;
	//
	private Text portNameText;
	private Combo typeCombo;
	private Text idlFileText;
	private Text varNameText;
	private Combo positionCombo;
	private Text descriptionText;
	private Text typeText;
	private Text numberText;
	private Text semanticsText;
	private Text unitText;
	private Text occurrenceText;
	private Text operationText;
	//
	private DataPortParam preSelection;
	private DataPortParam selectParam;
	//
	private String defaultPortName;
	private String defaultPortType;
	private String defaultPortVarName;
	private String[] defaultTypeList;
	
	private List<DataParam> typeList = new ArrayList<DataParam>();
	private List<DataParam> currentList = new ArrayList<DataParam>();

	public void setDefaultTypeList(String[] defaultTypeList) {
		this.defaultTypeList = defaultTypeList;
	}
	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public DataPortEditorFormPage(RtcBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.DATAPORT_SECTION"));
		//
		preSelection = null;
		
		IPreferenceStore store = RtcBuilderPlugin.getDefault().getPreferenceStore();
		defaultPortName = ComponentPreferenceManager.getInstance().getDataPort_Name();
		defaultPortType = store.getString(ComponentPreferenceManager.Generate_DataPort_Type);
		defaultPortVarName = store.getString(ComponentPreferenceManager.Generate_DataPort_VarName);
	}

	public void updateDefaultValue() {
		IPreferenceStore store = RtcBuilderPlugin.getDefault().getPreferenceStore();
		defaultPortName = ComponentPreferenceManager.getInstance().getDataPort_Name();
		defaultPortType = store.getString(ComponentPreferenceManager.Generate_DataPort_Type);
		defaultPortVarName = store.getString(ComponentPreferenceManager.Generate_DataPort_VarName);
		//
		defaultTypeList = super.extractDataTypes();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.DATAPORT_SECTION"));
		FormToolkit toolkit = managedForm.getToolkit();
		//
		final Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.DATAPORT_TITLE"), Messages.getString("IMC.DATAPORT_EXPL"), 4);
		
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.DATAPORT_TBLLBL_INPORTNAME"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.DATAPORT_TBLLBL_OUTPORTNAME"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));

		inportTableViewer = createPortSection(toolkit, composite, "", 0, true);
		outportTableViewer = createPortSection(toolkit, composite, "", 1, false);
		createHintSection(toolkit, form);

		createDetailSection(toolkit, form);
		//
		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 2);
		//
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_DATAPORT_TITLE"), IMessageConstants.DATAPORT_HINT_DATAPORT_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_INPORT_TITLE"), IMessageConstants.DATAPORT_HINT_INPORT_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_OUTPORT_TITLE"), IMessageConstants.DATAPORT_HINT_OUTPORT_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_PORTNAME_TITLE"), IMessageConstants.DATAPORT_HINT_PORTNAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_DATATYPE_TITLE"), IMessageConstants.DATAPORT_HINT_DATATYPE_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_VARNAME_TITLE"), IMessageConstants.DATAPORT_HINT_VARNAME_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_HINT_POSITION_TITLE"), IMessageConstants.DATAPORT_HINT_POSITION_DESC, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.HINT_DOCUMENT_TITLE"), "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_DESCRIPTION"), Messages.getString("IMC.DATAPORT_HINT_DOC_OVERVIEW"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_SEMANTICS"), Messages.getString("IMC.DATAPORT_HINT_DOC_DETAIL"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_PORTTYPE"), Messages.getString("IMC.DATAPORT_HINT_DOC_DATATYPE"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_DATANUM"), Messages.getString("IMC.DATAPORT_HINT_DOC_DATANUM"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_UNIT"), Messages.getString("IMC.DATAPORT_HINT_DOC_UNIT"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_OCCUR"), IMessageConstants.DATAPORT_HINT_OCCUR_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.DATAPORT_LBL_OPERAT"), IMessageConstants.DATAPORT_HINT_OPERAT_DESC, toolkit, composite);
	}

	private void createDetailSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Detail", IMessageConstants.DATAPORT_DOCUMENT_EXPL, 2, 2);
		//
		portNameText = createLabelAndRefText(toolkit, composite,
				Messages.getString("IMC.DATAPORT_LBL_PORTNAME"), SWT.BORDER, 1);
		//
		Group detailGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		detailGroup.setLayout(new GridLayout(3, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		detailGroup.setLayoutData(gd);
		//
		Label label = toolkit.createLabel(detailGroup, IMessageConstants.REQUIRED + Messages.getString("IMC.DATAPORT_TBLLBL_DATATYPE"));
		label.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		typeCombo = new Combo(detailGroup, SWT.DROP_DOWN);
		/////
		List<DataTypeParam> dataTypes = editor.getGeneratorParam().getDataTypeParams();
		typeList.clear();
		for(DataTypeParam each : dataTypes) {
			for(String eachType : each.getDefinedTypes()) {
				typeList.add(new DataParam(eachType, each.getDispPath()));
			}
		}
		Collections.sort(typeList, new DataParamComparator());
		currentList.clear();
		currentList.addAll(typeList);
		for(DataParam item : currentList) {
			typeCombo.add(item.typeName);
		}
		/////
		typeCombo.setText("");
		typeCombo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				String target = typeCombo.getText();
				String[] keyList = target.split(" ");
				currentList.clear();
				for (DataParam each : typeList) {
					boolean isHit = true;
					for(String itemKey: keyList) {
					  if (each.typeName.contains(itemKey)==false) {
						  isHit = false;
						  break;
					  }
					}
					if (isHit) {
						currentList.add(each);
					}
				}
				Collections.sort(currentList, new DataParamComparator());
				typeCombo.removeAll();
				for(DataParam item : currentList) {
					typeCombo.add(item.typeName);
				}
				typeCombo.setText(target);
				typeCombo.setSelection(new Point(typeCombo.getText().length(), typeCombo.getText().length()) );
			}
			public void keyPressed(KeyEvent e) { }
		});
		typeCombo.addSelectionListener(new SelectionListener() {
			  public void widgetDefaultSelected(SelectionEvent e){}
			  public void widgetSelected(SelectionEvent e){
				  idlFileText.setText(currentList.get(typeCombo.getSelectionIndex()).idlPath);
				  update();
			  }
			});
		GridData gdcombo = new GridData(GridData.FILL_HORIZONTAL);
		typeCombo.setLayoutData(gdcombo);

		Button reloadButton = toolkit.createButton(detailGroup, "ReLoad", SWT.PUSH);
		reloadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				defaultTypeList = extractDataTypes();
				/////
				List<DataTypeParam> dataTypes = editor.getGeneratorParam().getDataTypeParams();
				typeList.clear();
				typeCombo.removeAll();
				idlFileText.setText("");
				for(DataTypeParam each : dataTypes) {
					for(String eachType : each.getDefinedTypes()) {
						typeList.add(new DataParam(eachType, each.getDispPath()));
					}
				}
				Collections.sort(typeList, new DataParamComparator());
				currentList.clear();
				currentList.addAll(typeList);
				for(DataParam item : currentList) {
					typeCombo.add(item.typeName);
				}
			}
		});
		//
		idlFileText = createLabelAndRefText(toolkit, detailGroup,
				Messages.getString("IMC.SERVICEPORT_LBL_IDLFILE"), SWT.BORDER, 2);
		//
		varNameText = createLabelAndText(toolkit, detailGroup, Messages.getString("IMC.DATAPORT_TBLLBL_VARNAME"), SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		varNameText.setLayoutData(gd);
		positionCombo = createLabelAndCombo(toolkit, detailGroup, Messages.getString("IMC.DATAPORT_TBLLBL_POSITION"), DataPortParam.COMBO_ITEM);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		positionCombo.setLayoutData(gd);
		/////
		Group documentGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		documentGroup.setLayout(new GridLayout(2, false));
		documentGroup.setText("Documentation");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		documentGroup.setLayoutData(gd);
		//
		descriptionText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_DESCRIPTION"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		descriptionText.setLayoutData(gridData);
		semanticsText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_SEMANTICS"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		semanticsText.setLayoutData(gridData);
		typeText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_PORTTYPE"), SWT.BORDER);
		numberText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_DATANUM"), SWT.BORDER);
		unitText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_UNIT"), SWT.BORDER);
		occurrenceText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_OCCUR"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		occurrenceText.setLayoutData(gridData);
		operationText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.DATAPORT_LBL_OPERAT"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		operationText.setLayoutData(gridData);
	}

	private TableViewer createPortSection(FormToolkit toolkit, Composite parent,
			String columnLabel, final int initSel, boolean isInPort) {

		final TableViewer portParamTableViewer = createTableViewer(toolkit,	parent, 70);
		portParamTableViewer.getTable().setHeaderVisible(false);

		final TableViewerColumn col = super.createColumn(portParamTableViewer, columnLabel, IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		col.setEditingSupport(new DataPortEditingSuport(portParamTableViewer));
//		col.getColumn().setResizable(false);
		portParamTableViewer.setLabelProvider(new DataPortParamLabelProvider());
		//
		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Point size = portParamTableViewer.getControl().getSize();
				ScrollBar vBar = portParamTableViewer.getTable().getVerticalBar();
				col.getColumn().setWidth(size.x- vBar.getSize().x*2);
			}
		});
		//
		Composite buttonComposite = toolkit.createComposite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.marginWidth = 1;
		buttonComposite.setLayout(gl);
		GridData gd = new GridData();
		gd.verticalAlignment = SWT.BEGINNING;
		gd.widthHint = 80;
		buttonComposite.setLayoutData(gd);

		Button addButton = toolkit.createButton(buttonComposite, "Add", SWT.PUSH);
		addButton.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				String selected = typeCombo.getText();
				updateDefaultValue();
				DataPortParam selectParam = new DataPortParam(defaultPortName, defaultPortType, defaultPortVarName, initSel);
				((List) portParamTableViewer.getInput()).add(selectParam);
				portParamTableViewer.refresh();
				update();
				portParamTableViewer.setSelection(new StructuredSelection(selectParam), true);
				typeCombo.setText(selected);
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		//
		Button deleteButton = toolkit.createButton(buttonComposite, "Delete", SWT.PUSH);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = portParamTableViewer.getTable()
						.getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List) portParamTableViewer.getInput()).size() >= selectionIndex + 1) {
					((List) portParamTableViewer.getInput())
							.remove(selectionIndex);
					portParamTableViewer.refresh();
					preSelection = null;
					clearText();
					update();
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
		//
		portParamTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setDocumentContents();
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				selectParam = (DataPortParam)selection.getFirstElement();
				if( selectParam != null ) {
					StringBuffer portName = new StringBuffer(selectParam.getName());
					if(event.getSource().equals(inportTableViewer)) {
						portName.append(" (InPort)");
					} else {
						portName.append(" (OutPort)");
					}
					portNameText.setText(portName.toString());
					typeCombo.setText(selectParam.getType());
					idlFileText.setText(selectParam.getDispIdlFile());
					varNameText.setText(StringUtil.getDisplayDocText(selectParam.getVarName()));
					positionCombo.setText(selectParam.getPosition());
					descriptionText.setText(StringUtil.getDisplayDocText(selectParam.getDocDescription()));
					typeText.setText(StringUtil.getDisplayDocText(selectParam.getDocType()));
					numberText.setText(StringUtil.getDisplayDocText(selectParam.getDocNum()));
					semanticsText.setText(StringUtil.getDisplayDocText(selectParam.getDocSemantics()));
					unitText.setText(StringUtil.getDisplayDocText(selectParam.getDocUnit()));
					occurrenceText.setText(StringUtil.getDisplayDocText(selectParam.getDocOccurrence()));
					operationText.setText(StringUtil.getDisplayDocText(selectParam.getDocOperation()));
					preSelection = selectParam;
				}
			}
		});

		if( isInPort ) {
			inportAddButton = addButton;
			inportDeleteButton = deleteButton;
		} else {
			outportAddButton = addButton;
			outportDeleteButton = deleteButton;
		}

		return portParamTableViewer;
	}

	public void update() {
		if (selectParam != null) {
			selectParam.setType(typeCombo.getText());
			selectParam.setVarName(StringUtil.getDocText(varNameText.getText()));
			selectParam.setPosition(positionCombo.getText());
			selectParam.setDispIdlFile(idlFileText.getText());
			
			selectParam.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			selectParam.setDocType(StringUtil.getDocText(typeText.getText()));
			selectParam.setDocNum(StringUtil.getDocText(numberText.getText()));
			selectParam.setDocSemantics(StringUtil.getDocText(semanticsText.getText()));
			selectParam.setDocUnit(StringUtil.getDocText(unitText.getText()));
			selectParam.setDocOccurrence(StringUtil.getDocText(occurrenceText.getText()));
			selectParam.setDocOperation(StringUtil.getDocText(operationText.getText()));
			if(typeCombo.getText() != null && typeCombo.getText().length()==0) {
				idlFileText.setText("");
			}
		}
		//
		editor.updateEMFDataPorts(
				editor.getRtcParam().getInports(), editor.getRtcParam().getOutports(),
				editor.getRtcParam().getEventports(), editor.getRtcParam().getServicePorts());
		editor.updateDirty();
	}

	public void updateForOutput() {
		update();
		setDocumentContents();
	}

	private void setDocumentContents() {
		if( preSelection != null ) {
			preSelection.setType(typeCombo.getText());
			preSelection.setVarName(StringUtil.getDocText(varNameText.getText()));
			preSelection.setPosition(positionCombo.getText());
			//
			preSelection.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			preSelection.setDocType(StringUtil.getDocText(typeText.getText()));
			preSelection.setDocNum(StringUtil.getDocText(numberText.getText()));
			preSelection.setDocSemantics(StringUtil.getDocText(semanticsText.getText()));
			preSelection.setDocUnit(StringUtil.getDocText(unitText.getText()));
			preSelection.setDocOccurrence(StringUtil.getDocText(occurrenceText.getText()));
			preSelection.setDocOperation(StringUtil.getDocText(operationText.getText()));
		}
	}

	private void clearText() {
		portNameText.setText("");
		typeCombo.setText("");
		idlFileText.setText("");
		varNameText.setText("");
		positionCombo.select(0);
		descriptionText.setText("");
		typeText.setText("");
		numberText.setText("");
		semanticsText.setText("");
		unitText.setText("");
		occurrenceText.setText("");
		operationText.setText("");
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (inportTableViewer == null) return;
		RtcParam rtcParam = editor.getRtcParam();
		outportTableViewer.setInput(rtcParam.getOutports());
		inportTableViewer.setInput(rtcParam.getInports());
		//Mac版では列の幅が最小化してしまうため，再度列幅を設定
		outportTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		inportTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		//
		StructuredSelection selection = (StructuredSelection) outportTableViewer
				.getSelection();
		DataPortParam outParam = (DataPortParam) selection.getFirstElement();
		selection = (StructuredSelection) inportTableViewer.getSelection();
		DataPortParam inParam = (DataPortParam) selection.getFirstElement();
		if (outParam == null && inParam == null) clearText();
		//
		editor.updateEMFDataPorts(
				editor.getRtcParam().getInports(), editor.getRtcParam().getOutports(),
				editor.getRtcParam().getEventports(), editor.getRtcParam().getServicePorts());
	}

	public String validateParam() {
		String result = null;

		RtcParam rtcParam = editor.getRtcParam();
		Set<String> checkSet = new HashSet<String>();
		Set<String> checkVarSet = new HashSet<String>();

		for(DataPortParam dataport : rtcParam.getInports()) {
			result = checkDataPort(dataport, checkSet, checkVarSet);
			if( result != null) return result;
		}
		//
		for(DataPortParam dataport : rtcParam.getOutports()) {
			result = checkDataPort(dataport, checkSet, checkVarSet);
			if( result != null) return result;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private String checkDataPort(DataPortParam dataport, Set checkSet, Set checkVarSet) {
		String result = ValidationUtil.validateDataPort(dataport);
		if( result!=null ) return result;
		//名称重複
		if( checkSet.contains(dataport.getName()) ) {
			return IMessageConstants.DATAPORT_VALIDATE_DUPLICATE;
		}
		checkSet.add(dataport.getName());
		//変数名重複
		if( checkVarSet.contains(dataport.getTmplVarName()) ) {
			return IMessageConstants.DATAPORT_VALIDATE_VAR_DUPLICATE;
		}
		checkVarSet.add(dataport.getTmplVarName());
		//型存在チェック
		if(Arrays.asList(defaultTypeList).contains(dataport.getType().trim())==false) {
			return IMessageConstants.DATAPORT_VALIDATE_PORTTYPE_INVALID;
		}
		return null;
	}

	private class DataPortParamLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof DataPortParam == false) return null;
			DataPortParam dataPortParam = (DataPortParam) element;
			return dataPortParam.getName();
		}
	}

	private class DataPortEditingSuport extends EditingSupport {
		private ColumnViewer viewer;

		public DataPortEditingSuport(ColumnViewer viewer) {
			super(viewer);
			this.viewer = viewer;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor((Composite) viewer.getControl()) {
	            @Override
	            public LayoutData getLayoutData() {
	                LayoutData data = super.getLayoutData();
	                Control control = getControl();
	                if (control != null && !control.isDisposed()) {
	                    GC gc = new GC(control);
	                    int fontHeight = gc.getFontMetrics().getHeight();
	                    gc.dispose();
	                    
	                    data.minimumHeight = fontHeight + 4;
	                }
	                return data;
	            }
	        };
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof DataPortParam == false) return null;
			DataPortParam dataPortParam = (DataPortParam) element;
			return dataPortParam.getName();
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof DataPortParam == false) return;
			DataPortParam dataPortParam = (DataPortParam) element;

			dataPortParam.setName((String) value);
			StringBuffer portName = new StringBuffer(dataPortParam.getName());
			if( this.getViewer()==inportTableViewer ) {
				portName.append(" (InPort)");
			} else {
				portName.append(" (OutPort)");
			}
			portNameText.setText(portName.toString());

			getViewer().update(element, null);
			update();
		}
	}

	/**
	 * DataPortフォーム内の要素の有効/無効を設定します。
	 * <ul>
	 * <li>dataport.inPort.table : InPortセクションのテーブル</li>
	 * <li>dataport.inPort.addButton : InPortセクションの Addボタン</li>
	 * <li>dataport.inPort.deleteButton : InPortセクションの Deleteボタン</li>
	 * <li>dataport.outPort.table : OutPortセクションのテーブル</li>
	 * <li>dataport.outPort.addButton : OutPortセクションの Addボタン</li>
	 * <li>dataport.outPort.deleteButton : OutPortセクションの Deleteボタン</li>
	 * </ul>
	 */
	public void setEnabledInfo(WidgetInfo widgetInfo, boolean enabled) {
		if (widgetInfo.matchSection("inPort")) {
			if (inportTableViewer != null) {
				if (widgetInfo.matchWidget("table"))        setViewerEnabled(inportTableViewer, enabled);
				if (widgetInfo.matchWidget("addButton"))    setButtonEnabled(inportAddButton, enabled);
				if (widgetInfo.matchWidget("deleteButton")) setButtonEnabled(inportDeleteButton, enabled);
			}
		}
		if (widgetInfo.matchSection("outPort")) {
			if (outportTableViewer != null) {
				if (widgetInfo.matchWidget("table"))        setViewerEnabled(outportTableViewer, enabled);
				if (widgetInfo.matchWidget("addButton"))    setButtonEnabled(outportAddButton, enabled);
				if (widgetInfo.matchWidget("deleteButton")) setButtonEnabled(outportDeleteButton, enabled);
			}
		}
	}
	
	private class DataParam {
		private String typeName;
		private String idlPath;
		
		public DataParam(String typeName, String idlPath) {
			this.typeName = typeName;
			this.idlPath = idlPath;
		}
	}
	private class DataParamComparator implements Comparator<DataParam> {
		@Override
		public int compare(DataParam p1, DataParam p2) {
			return p1.typeName.compareTo(p2.typeName);
		}
	}}
