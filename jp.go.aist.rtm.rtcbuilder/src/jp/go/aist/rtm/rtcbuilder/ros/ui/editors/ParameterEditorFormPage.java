package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.util.List;

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
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.param.ParameterParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * Parameterページ
 */
public class ParameterEditorFormPage extends AbstractEditorFormPage {
	private TableViewer parmeterTableViewer;
	//
	private Text parameterNameDetailText;
	private Combo typeCombo;
	private Text defaultValueText;
	private Text minText;
	private Text maxText;
	private Text stepText;
	private Button readOnlyBtn;
	//
	private Text datanameText;
	private Text defaultValText;
	private Text descriptionText;
	private Text unitText;
	private Text rangeText;
	private Text constraintText;
	//
	private ParameterParam preSelection;
	private ParameterParam selectParam;
	//
	private String[] defaultTypeList = {"double", "int", "string", "bool"};

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public ParameterEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.ROS_PARAMETER_SECTION"));
		//
		preSelection = null;
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.ROS_PARAMETER_SECTION"));
		FormToolkit toolkit = managedForm.getToolkit();
		//
		parmeterTableViewer = createParameterSection(toolkit, form);
		createHintSection(toolkit, form);
		createDetailSection(toolkit, form);
		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private void createDetailSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Detail", IMessageConstantsROS.PARAMETER_DOCUMENT_EXPL, 2);

		parameterNameDetailText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.CONFIGURATION_LBL_PARAMNAME"), SWT.BORDER);
		parameterNameDetailText.setEditable(false);
		parameterNameDetailText.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		//
		Group detailGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		detailGroup.setLayout(new GridLayout(6, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		detailGroup.setLayoutData(gd);
		//
		typeCombo = createCombo(toolkit, detailGroup,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_TYPE"), defaultTypeList, SWT.COLOR_RED, 4);
		typeCombo.addSelectionListener(new SelectionListener() {
			  public void widgetDefaultSelected(SelectionEvent e){}
			  public void widgetSelected(SelectionEvent e){
				  boolean isEnable = typeCombo.getSelectionIndex() <= 1;
				  minText.setEnabled(isEnable);
				  maxText.setEnabled(isEnable);
				  stepText.setEnabled(isEnable);
				  update();
			  }
			});
		readOnlyBtn = createRadioCheckButton(toolkit, detailGroup, Messages.getString("IMC.ROS_PARAMETER_READ_ONLT_LBL"), SWT.CHECK);
		defaultValueText = createLabelAndText(toolkit, detailGroup,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_DEFAULTVAL"), SWT.BORDER, SWT.COLOR_RED, 5);
		minText = createLabelAndText(toolkit, detailGroup, Messages.getString("IMC.ROS_PARAMETER_MIN_LBL"), SWT.BORDER, SWT.COLOR_BLACK);
		maxText = createLabelAndText(toolkit, detailGroup, Messages.getString("IMC.ROS_PARAMETER_MAX_LBL"), SWT.BORDER, SWT.COLOR_BLACK);
		stepText = createLabelAndText(toolkit, detailGroup, Messages.getString("IMC.ROS_PARAMETER_STEP_LBL"), SWT.BORDER, SWT.COLOR_BLACK);
		/////
		Group documentGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		documentGroup.setLayout(new GridLayout(2, false));
		documentGroup.setText("Documentation");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		documentGroup.setLayoutData(gd);
		//
		datanameText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_DATANAME"), SWT.BORDER);
		defaultValText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_DEFAULT"), SWT.BORDER);
		descriptionText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_DESCRIPTION"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		descriptionText.setLayoutData(gridData);
		unitText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_UNIT"), SWT.BORDER);
		rangeText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_RANGE"), SWT.BORDER);
		constraintText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.CONFIGURATION_LBL_CONSTRAINT"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		constraintText.setLayoutData(gridData);
	}

	private TableViewer createParameterSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_PARAMETER_TITLE"), Messages.getString("IMC.ROS_PARAMETER_EXPL"), 3);
		//
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_NAME"),
				3,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));

		final TableViewer parameterTableViewer = createTableViewer(toolkit,	composite);
		parameterTableViewer.getTable().setHeaderVisible(false);

		final TableViewerColumn col = super.createColumn(parameterTableViewer,
				IMessageConstants.REQUIRED + Messages.getString("IMC.CONFIGURATION_TBLLBL_NAME"),
				IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		col.setEditingSupport(new ParameterModifier(parameterTableViewer));
//		col.getColumn().setResizable(false);
		parameterTableViewer.setLabelProvider(new ParameterLabelProvider());
		composite.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Point size = parameterTableViewer.getControl().getSize();
				ScrollBar vBar = parameterTableViewer.getTable().getVerticalBar();
				col.getColumn().setWidth(size.x- vBar.getSize().x*2);
			}
		});
		//
		Composite buttonComposite = toolkit.createComposite(composite, SWT.NONE);
		GridLayout gl = new GridLayout();
		buttonComposite.setLayout(gl);
		gl.marginWidth = 1;
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
				ParameterParam selectParam = new ParameterParam();
				((List) parameterTableViewer.getInput()).add(selectParam);
				parameterTableViewer.refresh();
				update();
				parameterTableViewer.setSelection(new StructuredSelection(selectParam), true);
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
				int selectionIndex = parameterTableViewer.getTable()
						.getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List) parameterTableViewer.getInput()).size() >= selectionIndex + 1) {
					((List) parameterTableViewer.getInput())
							.remove(selectionIndex);
					parameterTableViewer.refresh();
					preSelection = null;
					clearText();
					update();
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
		//
		parameterTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setDocumentContents();
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				selectParam = (ParameterParam)selection.getFirstElement();
				if( selectParam != null ) {
					parameterNameDetailText.setText(selectParam.getName());
					typeCombo.setText(selectParam.getType());
					boolean isEnable = typeCombo.getSelectionIndex() <= 1;
					minText.setEnabled(isEnable);
					maxText.setEnabled(isEnable);
					stepText.setEnabled(isEnable);

					defaultValueText.setText(selectParam.getDefaultValue());
					if(selectParam.getMin() != null) {
						minText.setText(Double.valueOf(selectParam.getMin()).toString());
					} else {
						minText.setText("");
					}
					if(selectParam.getMax() != null) {
						maxText.setText(Double.valueOf(selectParam.getMax()).toString());
					} else {
						maxText.setText("");
					}
					if(selectParam.getStep() != null) {
						stepText.setText(Double.valueOf(selectParam.getStep()).toString());
					} else {
						stepText.setText("");
					}
					readOnlyBtn.setSelection(selectParam.isReadOnly());
					//
					descriptionText.setText(StringUtil.getDisplayDocText(selectParam.getDocDescription()));
					datanameText.setText(StringUtil.getDisplayDocText(selectParam.getDocDataname()));
					defaultValText.setText(StringUtil.getDisplayDocText(selectParam.getDocDefault()));
					unitText.setText(StringUtil.getDisplayDocText(selectParam.getDocUnit()));
					rangeText.setText(StringUtil.getDisplayDocText(selectParam.getDocRange()));
					constraintText.setText(StringUtil.getDisplayDocText(selectParam.getDocConstraint()));
					preSelection = selectParam;
				}
			}
		});
		return parameterTableViewer;
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 4);
		//
		createHintLabel(Messages.getString("IMC.ROS_PARAMETER_HINT_PARAMETER_TITLE"), IMessageConstantsROS.PARAMETER_HINT_PARAMETER_EXPL, toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_PARAMNAME_TITLE"), Messages.getString("IMC.ROS_PARAMETER_HINT_PARAMETER_NAME_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_PARAMTYPE_TITLE"), Messages.getString("IMC.ROS_PARAMETER_HINT_PARAMETER_TYPE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_PARAMETER_READ_ONLT_LBL"), Messages.getString("IMC.ROS_PARAMETER_HINT_READ_ONLY_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_HINT_DEFAULT_TITLE"), IMessageConstantsROS.PARAMETER_HINT_DEFAULT_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_PARAMETER_MIN_LBL"), IMessageConstantsROS.PARAMETER_HINT_MIN_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_PARAMETER_MAX_LBL"), IMessageConstantsROS.PARAMETER_HINT_MAX_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_PARAMETER_STEP_LBL"), IMessageConstantsROS.PARAMETER_HINT_STEP_EXPL, toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.HINT_DOCUMENT_TITLE"), "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_DATANAME"), Messages.getString("IMC.CONFIGURATION_HINT_DOC_NAME_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_DEFAULT"), Messages.getString("IMC.CONFIGURATION_HINT_DOC_DEFAULT_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_DESCRIPTION"), Messages.getString("IMC.ROS_PARAMETER_HINT_DESC_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_UNIT"), IMessageConstantsROS.PARAMETER_HINT_DESC_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_RANGE"), IMessageConstants.CONFIGURATION_HINT_DOC_RANGE, toolkit, composite);
		createHintLabel(Messages.getString("IMC.CONFIGURATION_LBL_CONSTRAINT"), IMessageConstants.CONFIGURATION_HINT_DOC_CONSTRAINT, toolkit, composite);
	}

	public void updateForOutput() {
		setDocumentContents();
	}

	private void setDocumentContents() {
		setContents(preSelection);
	}

	public void update() {
		setContents(selectParam);
		editor.updateDirty();
	}

	private void setContents(ParameterParam target) {
		if( target != null ) {
			target.setType(typeCombo.getText());
			target.setDefaultValue(StringUtil.getDocText(defaultValueText.getText()));
			try {
				double val = 0.0;
				val = Double.parseDouble(StringUtil.getDocText(minText.getText()));
				target.setMin(val);
			} catch(Exception ex) {}
			try {
				double val = 0.0;
				val = Double.parseDouble(StringUtil.getDocText(maxText.getText()));
				target.setMax(val);
			} catch(Exception ex) {}
			try {
				double val = 0.0;
				val = Double.parseDouble(StringUtil.getDocText(stepText.getText()));
				target.setStep(val);
			} catch(Exception ex) {}
			target.setReadOnly(readOnlyBtn.getSelection());
			//
			target.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			target.setDocDataname(StringUtil.getDocText(datanameText.getText()));
			target.setDocDefault(StringUtil.getDocText(defaultValText.getText()));
			target.setDocUnit(StringUtil.getDocText(unitText.getText()));
			target.setDocRange(StringUtil.getDocText(rangeText.getText()));
			target.setDocConstraint(StringUtil.getDocText(constraintText.getText()));
		}
	}

	private void clearText() {
		//
		parameterNameDetailText.setText("");
		typeCombo.select(0);
		defaultValueText.setText("");
		minText.setText("");
		maxText.setText("");
		stepText.setText("");
		readOnlyBtn.setSelection(false);
		//
		datanameText.setText("");
		defaultValText.setText("");
		descriptionText.setText("");
		unitText.setText("");
		rangeText.setText("");
		constraintText.setText("");
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (parmeterTableViewer == null) return;
		//
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		parmeterTableViewer.setInput(rosParam.getParameters());
		//Mac版では列の幅が最小化してしまうため，再度列幅を設定
		parmeterTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		//
		StructuredSelection selection = (StructuredSelection) parmeterTableViewer
				.getSelection();
		ParameterParam param = (ParameterParam) selection.getFirstElement();
		if (param == null) 	clearText();
	}

	public String validateParam() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		return rosParam.validateParameterInfo();
	}

	private class ParameterLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/**
		 * {@inheritDoc}
		 */
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ParameterParam == false) return null;
			ParameterParam param = (ParameterParam) element;
			return param.getName();
		}
	}

	private class ParameterModifier extends EditingSupport {
		private ColumnViewer viewer;

		public ParameterModifier(ColumnViewer viewer) {
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
			if (element instanceof ParameterParam == false) return null;
			ParameterParam configSetParam = (ParameterParam) element;

			return configSetParam.getName();
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ParameterParam == false) return;
			ParameterParam param = (ParameterParam) element;

			param.setName((String) value);
			parameterNameDetailText.setText(param.getName());

			getViewer().update(element, null);
			update();
		}
	}
}
