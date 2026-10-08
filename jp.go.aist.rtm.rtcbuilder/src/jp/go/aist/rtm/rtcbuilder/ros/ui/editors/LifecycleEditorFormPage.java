package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TimerParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * ライフサイクルページ
 */
public class LifecycleEditorFormPage extends AbstractEditorFormPage {

	private static final String ACTIVITY_CONFIGURE = "on_configure";
	private static final String ACTIVITY_ACTIVATE = "on_activate";
	private static final String ACTIVITY_DEACTIVATE = "on_deactivate";
	private static final String ACTIVITY_CLEANUP = "on_cleanup";
	private static final String ACTIVITY_SHUTDOWN = "on_shutdown";
	private static final String ACTIVITY_ERROR = "on_error";

	private List<Label> implChk;
	private Text actionNameText;
	private Button onBtn;	
	private Button offBtn;
	private Text activityText;
	private Text preConditionText;
	private Text postConditionText;
	//
	private int preSelection;
	//
	private TableViewer timerListViewer;
	private Button addButton;	
	private Button deleteButton;
	private List<TimerParam> timerList = new ArrayList<TimerParam>();

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public LifecycleEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.ROS_LIFECYCLE_SECTION"));
		//
		preSelection = -1;
		implChk = new ArrayList<Label>();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.ROC_LIFECYCLE_TITLE"));
		FormToolkit toolkit = managedForm.getToolkit();
		createLifecycleSection(managedForm.getToolkit(), form);
		createHintSection(toolkit, form);
		createDocumentSection(managedForm.getToolkit(), form);
		
		createTimerSection(toolkit, form);

		// 言語・環境ページより先にこのページが表示された場合、ここで言語を判断する
		editor.setEnabledInfoByLang();

		load();
	}

	private void createLifecycleSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROC_LIFECYCLE_TITLE"), Messages.getString("IMC.ROS_LIFECYCLE_EXPL"), 3);
		//
		createActionSelection(composite, ACTIVITY_CONFIGURE);
		createActionSelection(composite, ACTIVITY_ACTIVATE);
		createActionSelection(composite, ACTIVITY_DEACTIVATE);
		createActionSelection(composite, ACTIVITY_CLEANUP);
		createActionSelection(composite, ACTIVITY_SHUTDOWN);
		createActionSelection(composite, ACTIVITY_ERROR);
	}

	private void createActionSelection(Composite composite, String actionName) {
		Label impl = new Label(composite, SWT.CHECK);
		impl.setText(actionName);
		impl.addMouseListener(new MouseListener() {
			@Override
			public void mouseDown(MouseEvent e) {
				ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
				if (preSelection >= 0) {
					rosParam.setActionImplemented(preSelection, onBtn.getSelection());
					rosParam.setDocActionOverView(preSelection, StringUtil.getDocText(activityText.getText()));
					rosParam.setDocActionPreCondition(preSelection, StringUtil.getDocText(preConditionText.getText()));
					rosParam.setDocActionPostCondition(preSelection, StringUtil.getDocText(postConditionText.getText()));
				}
				int index = implChk.indexOf(e.getSource());
				if(index<=IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE) {
					onBtn.setSelection(true);
					offBtn.setSelection(false);
					onBtn.setEnabled(false);
					offBtn.setEnabled(false);
				} else {
					if( rosParam.getActionImplemented(index) ) {
						onBtn.setSelection(true);
						offBtn.setSelection(false);
					} else {
						onBtn.setSelection(false);
						offBtn.setSelection(true);
					}
					onBtn.setEnabled(true);
					offBtn.setEnabled(true);
				}
				actionNameText.setText(IRtcBuilderConstantsROS.ACTION_TYPE_ITEMS[index]);
				activityText.setText(StringUtil.getDisplayDocText(rosParam.getDocActionOverView(index)));
				preConditionText.setText(StringUtil.getDisplayDocText(rosParam.getDocActionPreCondition(index)));
				postConditionText.setText(StringUtil.getDisplayDocText(rosParam.getDocActionPostCondition(index)));
				preSelection = index;
				//
				for(Label target : implChk) {
					target.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_BLACK));
				}
				implChk.get(index).setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
				update();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
				int index = implChk.indexOf(e.getSource());
				if(index<=IRtcBuilderConstantsROS.ACTIVITY_DEACTIVATE) {
					onBtn.setSelection(true);
					offBtn.setSelection(false);
					onBtn.setEnabled(false);
					offBtn.setEnabled(false);
				} else {
					rosParam.setActionImplemented(index, !rosParam.getActionImplemented(index));
					if( rosParam.getActionImplemented(index) ) {
						onBtn.setSelection(true);
						offBtn.setSelection(false);
						implChk.get(index).setBackground(new Color(PlatformUI.getWorkbench().getDisplay(), RTCUtil.defaultRGBMap.get(RTCUtil.COLOR_COMPONENT)));
					} else {
						onBtn.setSelection(false);
						offBtn.setSelection(true);
						implChk.get(index).setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
					}
					onBtn.setEnabled(true);
					offBtn.setEnabled(true);
				}
				update();
			}

			@Override
			public void mouseUp(MouseEvent e) {
			}
		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		impl.setLayoutData(gridData);
		impl.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
		implChk.add(impl);
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 12);
		//
		createHintLabel(ACTIVITY_CONFIGURE, IMessageConstantsROS.LIFECYCLE_CONFIGURE_EXPL, toolkit, composite);
		createHintLabel(ACTIVITY_ACTIVATE, IMessageConstantsROS.LIFECYCLE_ACTIVATE_EXPL, toolkit, composite);
		createHintLabel(ACTIVITY_DEACTIVATE, IMessageConstantsROS.LIFECYCLE_DEACTIVATE_EXPL, toolkit, composite);
		createHintLabel(ACTIVITY_CLEANUP, IMessageConstantsROS.LIFECYCLE_CLEANUP_EXPL, toolkit, composite);
		createHintLabel(ACTIVITY_SHUTDOWN, IMessageConstantsROS.LIFECYCLE_SHUTDOWN_EXPL, toolkit, composite);
		createHintLabel(ACTIVITY_ERROR, IMessageConstantsROS.LIFECYCLE_ERROR_EXPL, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		//
		createHintLabel(IMessageConstants.ACTIVITY_HINT_DESCRIPTION_TITLE, Messages.getString("IMC.ROS_LIFECYCLE_HINT_DESCRIPTION_DESC"), toolkit, composite);
		createHintLabel(IMessageConstants.ACTIVITY_HINT_PRECONDITION_TITLE, Messages.getString("IMC.ROS_LIFECYCLE_HINT_PRECONDITION_DESC"), toolkit, composite);
		createHintLabel(IMessageConstants.ACTIVITY_HINT_POSTCONDITION_TITLE, Messages.getString("IMC.ROS_LIFECYCLE_HINT_POSTCONDITION_DESC"), toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		//
		createHintLabel(Messages.getString("IMC.ROS_LIFECYCLE_LBL_TIMER"), IMessageConstantsROS.TIMER_DOCUMENT_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_LIFECYCLE_LBL_TIMER_NAME"), IMessageConstantsROS.TIMER_NAME_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_LIFECYCLE_LBL_TIMER_RATE"), Messages.getString("IMC.ROS_LIFECYCLE_HINT_TIMER_RATE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_LIFECYCLE_LBL_TIMER_CALLBACK"), IMessageConstantsROS.TIMER_CALLBACK_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_LIFECYCLE_LBL_TIMER_DESC"), IMessageConstantsROS.TIMER_DESC_EXPL, toolkit, composite);
	}

	private void createDocumentSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Documentation", IMessageConstantsROS.ACTIVITY_DOCUMENT_EXPL, 3);

		actionNameText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_LIFECYCLE_LBL_CALLBACK"), SWT.BORDER);
		actionNameText.setEditable(false);
		actionNameText.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		//
		Group compGroup = new Group(composite, SWT.NONE);
		compGroup.setLayout(new GridLayout(2, false));
		GridData gd = new GridData();
		compGroup.setLayoutData(gd);
		onBtn = createRadioCheckButton(toolkit, compGroup, "ON", SWT.RADIO);
		onBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(preSelection>=0) {
					implChk.get(preSelection).setBackground(new Color(PlatformUI.getWorkbench().getDisplay(), RTCUtil.defaultRGBMap.get(RTCUtil.COLOR_COMPONENT)));
				}
			}
		});
		offBtn = createRadioCheckButton(toolkit, compGroup, "OFF", SWT.RADIO);
		offBtn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if(preSelection>=0) {
					implChk.get(preSelection).setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
				}
			}
		});
		//
		activityText = createLabelAndText(toolkit, composite,
				IMessageConstants.ACTIVITY_LBL_DESCRIPTION, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData.horizontalSpan = 2;
		activityText.setLayoutData(gridData);
		preConditionText = createLabelAndText(toolkit, composite,
				IMessageConstants.ACTIVITY_LBL_PRECONDITION, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		preConditionText.setLayoutData(gridData);
		postConditionText = createLabelAndText(toolkit, composite,
				IMessageConstants.ACTIVITY_LBL_POSTCONDITION, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		postConditionText.setLayoutData(gridData);
	}

	private void createTimerSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_TIMER_TITLE"), Messages.getString("IMC.ROS_TIMER_EXPL"), 3);

		timerListViewer = new TableViewer(composite, SWT.FULL_SELECTION
				| SWT.SINGLE | SWT.BORDER);
		timerListViewer.setContentProvider(new ArrayContentProvider());
		Table timerListTable = timerListViewer.getTable();
		timerListTable.setLinesVisible(true);
		timerListTable.setHeaderVisible(true);
		GridData gd = new GridData();
		gd.heightHint = 150;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.grabExcessHorizontalSpace = true;
		timerListTable.setLayoutData(gd);
		
		TableViewerColumn nameColumn = createColumn(timerListViewer, "Timer Name", 100);
		nameColumn.setEditingSupport(new TimerCellModifier(timerListViewer, 0));
		TableViewerColumn rateColumn = createColumn(timerListViewer, "Rate", 100);
		rateColumn.setEditingSupport(new TimerCellModifier(timerListViewer, 1));
		TableViewerColumn versionColumn = createColumn(timerListViewer, "Callback", 100);
		versionColumn.setEditingSupport(new TimerCellModifier(timerListViewer, 2));
		TableViewerColumn otherColumn = createColumn(timerListViewer, "Description", 150);
		otherColumn.setEditingSupport(new TimerCellModifier(timerListViewer, 3));

		timerListViewer.setLabelProvider(new TimerLabelProvider());
		timerListViewer.setInput(timerList);
		
		Composite libraryComposite = new Composite(composite, SWT.NONE);
		GridLayout gl = new GridLayout(1, false);
		gd = new GridData(GridData.FILL_VERTICAL);
		libraryComposite.setLayout(gl);
		libraryComposite.setLayoutData(gd);

		addButton = new Button(libraryComposite, SWT.PUSH);
		addButton.setText(Messages.getString("IPreferenceMessageConstants.CONFIG_BTN_ADD"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TimerParam elem = new TimerParam();
				timerList.add(elem);
				timerListViewer.refresh();
				update();
			}
		});
		
		deleteButton = new Button(libraryComposite, SWT.PUSH);
		deleteButton.setText(Messages.getString("IPreferenceMessageConstants.CONFIG_BTN_DELETE"));
		gd = new GridData();
		gd.widthHint = EXEC_BUTTON_WIDTH;
		deleteButton.setLayoutData(gd);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = timerListViewer.getTable().getSelectionIndex();
				if (0 <= selectionIndex
						&& selectionIndex + 1 <= ((List) timerListViewer.getInput()).size()) {
					timerList.remove(selectionIndex);
					timerListViewer.refresh();
					update();
				}
			}
		});
	}

	public void update() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();

		if (preSelection >= 0) {
			rosParam.setActionImplemented(preSelection, onBtn.getSelection());
			rosParam.setDocActionOverView(preSelection, StringUtil.getDocText(activityText.getText()));
			rosParam.setDocActionPreCondition(preSelection, StringUtil.getDocText(preConditionText.getText()));
			rosParam.setDocActionPostCondition(preSelection, StringUtil.getDocText(postConditionText.getText()));
		}

		editor.updateDirty();
	}

	/**
	 * データをロードする
	 */
	public void load() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();

		if( activityText != null ) {
			for( int intidx=IRtcBuilderConstantsROS.ACTIVITY_CONFIGURE; intidx<IRtcBuilderConstantsROS.ACTIVITY_DUMMY; intidx++) {
				if( rosParam.getActionImplemented(intidx) ) {
					implChk.get(intidx).setBackground(new Color(PlatformUI.getWorkbench().getDisplay(), RTCUtil.defaultRGBMap.get(RTCUtil.COLOR_COMPONENT)));
				} else {
					implChk.get(intidx).setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WHITE));
				}
			}
			if (preSelection >= 0) {
				activityText.setText(StringUtil.getDisplayDocText(rosParam.getDocActionOverView(preSelection)));
				preConditionText.setText(StringUtil.getDisplayDocText(rosParam.getDocActionPreCondition(preSelection)));
				postConditionText.setText(StringUtil.getDisplayDocText(rosParam.getDocActionPostCondition(preSelection)));
			}
		}
		
		if(timerListViewer != null) {
			timerList = rosParam.getTimers();
			timerListViewer.setInput(timerList);
		}
	}

	public String validateParam() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		return rosParam.validateLifecycleInfo();
	}
	//////////
	private class TimerLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof TimerParam == false) return null;
			TimerParam elem = (TimerParam) element;
			if (columnIndex == 0) {
				return elem.getName();
			} else if (columnIndex == 1) {
				return elem.getRate().toString();
			} else if (columnIndex == 2) {
				return elem.getCallBack();
			} else if (columnIndex == 3) {
				return elem.getDescription();
			} else {
				return "";
			}
		}
	}
	private class TimerCellModifier extends EditingSupport {
		private CellEditor editor;
		int column;

		public TimerCellModifier(ColumnViewer viewer, int column) {
			super(viewer);
			this.column = column;
			editor = new TextCellEditor(((TableViewer) viewer).getTable());
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
			if (element instanceof TimerParam == false) return null;
			TimerParam elem = (TimerParam) element;
			String label = null;
			if(this.column == 0) {
				label = elem.getName();
			} else if(this.column == 1) {
				label = elem.getRate().toString();
			} else if(this.column == 2) {
				label = elem.getCallBack();
			} else if(this.column == 3) {
				label = elem.getDescription();
			}
			return label;
		}
		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof TimerParam == false) return;
			
			TimerParam elem = (TimerParam) element;
			if(this.column == 0) {
				elem.setName((String) value);
			} else if(this.column == 1) {
				try {
					double rate = Double.parseDouble((String) value);
					elem.setRate(rate);
				} catch (Exception ex) {
				}
			} else if(this.column == 2) {
				elem.setCallBack((String) value);
			} else if(this.column == 3) {
				elem.setDescription((String) value);
			}
			getViewer().update(element, null);
			update();
		}
	}
}
