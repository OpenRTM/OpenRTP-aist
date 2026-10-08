package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.param.ActionParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * Actionページ
 */
public class ActionEditorFormPage extends AbstractEditorFormPage {

	private TableViewer serverTableViewer;
	private TableViewer clientTableViewer;
	//
	private Text actionNameText;
	private Combo actionTypeCombo;
	private Text typePackageText;
	private Text callbackNameText;
	
	private Text descriptionText;
	private Text goalText;
	private Text feedbackText;
	private Text resultText;
	//
	private ActionParam preSelection;
	private ActionParam selectParam;
	//
	private List<String> typeList = new ArrayList<String>();
	private List<String> currentList = new ArrayList<String>();

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public ActionEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.ROS_ACTION_SECTION"));
		//
		preSelection = null;
		updateDefaultValue();
	}

	public void updateDefaultValue() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		typeList.clear();
		typeList.addAll(extractROSEtcTypes(rosParam.getOutputProject(), "action"));
		typeList.sort(null);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.ROS_ACTION_SECTION"));
		FormToolkit toolkit = managedForm.getToolkit();
		//
		final Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_ACTION_TITLE"), Messages.getString("IMC.ROS_ACTION_EXPL"), 4);

		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_ACTION_TBLLBL_SERVER"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_ACTION_TBLLBL_CLENT"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));

		serverTableViewer = createPortSection(toolkit, composite, "", 0,
				IRtcBuilderConstantsROS.SPEC_ACTION_SERVER);
		clientTableViewer = createPortSection(toolkit, composite, "", 1,
				IRtcBuilderConstantsROS.SPEC_ACTION_CLIENT);
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
		createHintLabel(Messages.getString("IMC.ROS_ACTION_HINT_ACTION_TITLE"), IMessageConstantsROS.ACTION_HINT_ACTION_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_HINT_SERVER_TITLE"), Messages.getString("IMC.ROS_ACTION_HINT_SERVER_DESC_"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_HINT_CLIENT_TITLE"), Messages.getString("IMC.ROS_ACTION_HINT_CLIENT_DESC"), toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_ACTIONNAME"), IMessageConstantsROS.ACTION_HINT_NAME_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_ACTIONTYPE"), Messages.getString("IMC.ROS_ACTION_HINT_ACTION_TYPE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_PACKAGE_TITLE"), IMessageConstantsROS.ACTION_HINT_PACKAGE_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_CALLBACK"), IMessageConstantsROS.ACTION_HINT_CALLBACK_EXPL, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.HINT_DOCUMENT_TITLE"), "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_DESCRIPTION"), IMessageConstantsROS.ACTION_HINT_DESC_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_GAOL"), Messages.getString("IMC.ROS_ACTION_HINT_GOAL_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_FEEDBACK"), Messages.getString("IMC.ROS_ACTION_HINT_FEEDBACK_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_ACTION_LBL_RESULT"), Messages.getString("IMC.ROS_ACTION_HINT_RESULT_DESC"), toolkit, composite);
	}

	private void createDetailSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Detail", IMessageConstantsROS.ACTION_DOCUMENT_EXPL, 2, 2);
		//
		actionNameText = createLabelAndRefText(toolkit, composite,
				Messages.getString("IMC.ROS_ACTION_LBL_ACTIONNAME"), SWT.BORDER, 1);
		//
		Group detailGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		detailGroup.setLayout(new GridLayout(5, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		detailGroup.setLayoutData(gd);
		//
		Label label = toolkit.createLabel(detailGroup, IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_ACTION_LBL_ACTIONTYPE"));
		label.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		actionTypeCombo = new Combo(detailGroup, SWT.DROP_DOWN);
		/////
		currentList.clear();
		currentList.addAll(typeList);
		for(String item : currentList) {
			actionTypeCombo.add(item);
		}
		/////
		actionTypeCombo.setText("");
		actionTypeCombo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				String target = actionTypeCombo.getText();
				String[] keyList = target.split(" ");
				currentList.clear();
				for (String each : typeList) {
					boolean isHit = true;
					for(String itemKey: keyList) {
					  if (each.contains(itemKey)==false) {
						  isHit = false;
						  break;
					  }
					}
					if (isHit) {
						currentList.add(each);
					}
				}
				currentList.sort(null);
				actionTypeCombo.removeAll();
				for(String item : currentList) {
					actionTypeCombo.add(item);
				}
				actionTypeCombo.setText(target);
				actionTypeCombo.setSelection(new Point(actionTypeCombo.getText().length(), actionTypeCombo.getText().length()) );
			}
			public void keyPressed(KeyEvent e) { }
		});
		GridData gdcombo = new GridData(GridData.FILL_HORIZONTAL);
		gdcombo.horizontalSpan = 2;
		actionTypeCombo.setLayoutData(gdcombo);

		Button selectButton = toolkit.createButton(detailGroup, "Select", SWT.PUSH);
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		        fileDialog.setText("Select action file");
		        fileDialog.setFilterExtensions(new String[] { "*.action" });
		        fileDialog.setFilterNames(new String[] { "Action Files (*.action)" });
		        String selectedPath = fileDialog.open();
		        if (selectedPath == null) return;
		        
	        	File srcFile = new File(selectedPath);
	        	
	    		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
	    		IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
	    		IProject project = workspaceHandle.getProject(rosParam.getOutputProject());
	    		IFolder targetFolder = project.getFolder("action");
	    		IFile destFile = targetFolder.getFile(srcFile.getName());
	    		if(destFile.exists()) {
					MessageBox message = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
					message.setText("File Copy");
					message.setMessage(Messages.getString("IMC.FILE_OVERWRITE"));
					if( message.open() != SWT.YES) return;
	    		}

	    		try (FileInputStream fis = new FileInputStream(srcFile)) {
	                if (destFile.exists()) {
	                    destFile.setContents(fis, IResource.FORCE, new NullProgressMonitor());
	                } else {
	                    destFile.create(fis, IResource.NONE, new NullProgressMonitor());
	                }
	                targetFolder.refreshLocal(IResource.DEPTH_ONE, null);
	            } catch (IOException e1) {
	            } catch (CoreException e2) {
	            }
	    		
	    		updateDefaultValue();
				actionTypeCombo.removeAll();
				currentList.clear();
				currentList.addAll(typeList);
				for(String item : currentList) {
					actionTypeCombo.add(item);
				}
			}
		});
		
		Button reloadButton = toolkit.createButton(detailGroup, "ReLoad", SWT.PUSH);
		reloadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	    		updateDefaultValue();
	    		actionTypeCombo.removeAll();
				currentList.clear();
				currentList.addAll(typeList);
				for(String item : currentList) {
					actionTypeCombo.add(item);
				}

				preSelection = null;
				typePackageText.setText("");
				if(0 < actionTypeCombo.getItemCount()) {
					actionTypeCombo.select(0);
				}
			}
		});
		//
		typePackageText = createLabelAndText(toolkit, detailGroup,
							IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_ACTION_LBL_PACKAGE_TITLE"),
							SWT.BORDER, SWT.COLOR_RED, 2, 1);
		toolkit.createLabel(detailGroup, "");
		/////
		callbackNameText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.ROS_ACTION_LBL_CALLBACK"), SWT.BORDER, SWT.COLOR_BLACK, 3, 2);

		/////
		Group documentGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		documentGroup.setLayout(new GridLayout(2, false));
		documentGroup.setText("Documentation");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		documentGroup.setLayoutData(gd);
		//
		descriptionText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_DESCRIPTION"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		descriptionText.setLayoutData(gridData);
		goalText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_ACTION_LBL_GAOL"), SWT.BORDER);
		feedbackText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_ACTION_LBL_FEEDBACK"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		feedbackText.setLayoutData(gridData);
		resultText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_ACTION_LBL_RESULT"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		resultText.setLayoutData(gridData);
	}

	private TableViewer createPortSection(FormToolkit toolkit, Composite parent,
			String columnLabel, final int initSel, String role) {

		final TableViewer actionTableViewer = createTableViewer(toolkit,	parent, 70);
		actionTableViewer.getTable().setHeaderVisible(false);

		final TableViewerColumn col = super.createColumn(actionTableViewer, columnLabel, IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		col.setEditingSupport(new ActionEditingSuport(actionTableViewer));
//		col.getColumn().setResizable(false);
		actionTableViewer.setLabelProvider(new ActionParamLabelProvider());
		//
		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Point size = actionTableViewer.getControl().getSize();
				ScrollBar vBar = actionTableViewer.getTable().getVerticalBar();
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
				String selected = actionTypeCombo.getText();
				updateDefaultValue();
				ActionParam selectParam = new ActionParam(role);
				((List) actionTableViewer.getInput()).add(selectParam);
				actionTableViewer.refresh();
				update();
				actionTableViewer.setSelection(new StructuredSelection(selectParam), true);
				actionTypeCombo.setText(selected);
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		//
		Button deleteButton = toolkit.createButton(buttonComposite, "Delete", SWT.PUSH);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = actionTableViewer.getTable()
						.getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List) actionTableViewer.getInput()).size() >= selectionIndex + 1) {
					((List) actionTableViewer.getInput())
							.remove(selectionIndex);
					actionTableViewer.refresh();
					preSelection = null;
					clearText();
					update();
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
		//
		actionTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setDocumentContents();
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				selectParam = (ActionParam)selection.getFirstElement();
				if( selectParam != null ) {
					StringBuffer portName = new StringBuffer(selectParam.getName());
					if(event.getSource().equals(serverTableViewer)) {
						portName.append(" (Server)");
					} else {
						portName.append(" (Client)");
					}
					actionNameText.setText(portName.toString());
					
					String strType = selectParam.getType();
					int lastSlashIndex = strType.lastIndexOf('/');
					if (lastSlashIndex == -1) {
						actionTypeCombo.setText(selectParam.getType());
						typePackageText.setText("");
					} else {
						typePackageText.setText(strType.substring(0, lastSlashIndex));
						actionTypeCombo.setText(strType.substring(lastSlashIndex + 1));
					}

					callbackNameText.setText(selectParam.getCallbackName());
					descriptionText.setText(StringUtil.getDisplayDocText(selectParam.getDocDescription()));
					goalText.setText(StringUtil.getDisplayDocText(selectParam.getDocGoal()));
					feedbackText.setText(StringUtil.getDisplayDocText(selectParam.getDocFeedback()));
					resultText.setText(StringUtil.getDisplayDocText(selectParam.getDocResult()));
					preSelection = selectParam;
				}
			}
		});

		return actionTableViewer;
	}

	public void update() {
		if (selectParam != null) {
			String typePackage = typePackageText.getText(); 
			if(typePackage == null || typePackage.length() == 0) {
				selectParam.setType(actionTypeCombo.getText());
			} else {
				selectParam.setType(typePackage + "/" + actionTypeCombo.getText());
			}
			selectParam.setCallbackName(callbackNameText.getText());
			
			selectParam.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			selectParam.setDocGoal(StringUtil.getDocText(goalText.getText()));
			selectParam.setDocFeedback(StringUtil.getDocText(feedbackText.getText()));
			selectParam.setDocResult(StringUtil.getDocText(resultText.getText()));
		}
		//
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		((ROSBuilderEditor)editor).updateEMFPorts(
				rosParam.getTopicSubscribes(), rosParam.getTopicPublishes(),
				rosParam.getServiceServers(), rosParam.getServiceClients(),
				rosParam.getActionServers(), rosParam.getActionClients());
		((ROSBuilderEditor)editor).updateDirty();
	}

	public void updateForOutput() {
		update();
		setDocumentContents();
	}

	private void setDocumentContents() {
		if( preSelection != null ) {
			String typePackage = typePackageText.getText(); 
			if(typePackage == null || typePackage.length() == 0) {
				selectParam.setType(actionTypeCombo.getText());
			} else {
				selectParam.setType(typePackage + "/" + actionTypeCombo.getText());
			}
			preSelection.setCallbackName(callbackNameText.getText());
			//
			preSelection.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			preSelection.setDocGoal(StringUtil.getDocText(goalText.getText()));
			preSelection.setDocFeedback(StringUtil.getDocText(feedbackText.getText()));
			preSelection.setDocResult(StringUtil.getDocText(resultText.getText()));
		}
	}

	private void clearText() {
		actionNameText.setText("");
		actionTypeCombo.select(0);
		callbackNameText.setText("");
		
		descriptionText.setText("");
		goalText.setText("");
		feedbackText.setText("");
		resultText.setText("");
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (serverTableViewer == null) return;
		
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		clientTableViewer.setInput(rosParam.getActionClients());
		serverTableViewer.setInput(rosParam.getActionServers());
		//Mac版では列の幅が最小化してしまうため，再度列幅を設定
		clientTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		serverTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		//
		StructuredSelection selection = (StructuredSelection) clientTableViewer
				.getSelection();
		ActionParam outParam = (ActionParam) selection.getFirstElement();
		selection = (StructuredSelection) serverTableViewer.getSelection();
		ActionParam inParam = (ActionParam) selection.getFirstElement();
		if (outParam == null && inParam == null) clearText();
		//
		((ROSBuilderEditor)editor).updateEMFPorts(
				rosParam.getTopicSubscribes(), rosParam.getTopicPublishes(),
				rosParam.getServiceServers(), rosParam.getServiceClients(),
				rosParam.getActionServers(), rosParam.getActionClients());
	}

	public String validateParam() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		return rosParam.validateActionInfo();
	}

	private class ActionParamLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ActionParam == false) return null;
			ActionParam param = (ActionParam) element;
			return param.getName();
		}
	}

	private class ActionEditingSuport extends EditingSupport {
		private ColumnViewer viewer;

		public ActionEditingSuport(ColumnViewer viewer) {
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
			if (element instanceof ActionParam == false) return null;
			ActionParam param = (ActionParam) element;
			return param.getName();
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof ActionParam == false) return;
			ActionParam param = (ActionParam) element;

			param.setName((String) value);
			StringBuffer portName = new StringBuffer(param.getName());
			if( this.getViewer()==serverTableViewer ) {
				portName.append(" (Server)");
			} else {
				portName.append(" (Client)");
			}
			actionNameText.setText(portName.toString());

			getViewer().update(element, null);
			update();
		}
	}
}
