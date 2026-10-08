package jp.go.aist.rtm.rtcbuilder.ui.editors;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.RtcBuilderPlugin;
import jp.go.aist.rtm.rtcbuilder.generator.IDLParamConverter;
import jp.go.aist.rtm.rtcbuilder.generator.param.DataTypeParam;
import jp.go.aist.rtm.rtcbuilder.generator.param.idl.IdlPathParam;
import jp.go.aist.rtm.rtcbuilder.model.component.BuildView;
import jp.go.aist.rtm.rtcbuilder.util.FileUtil;
import jp.go.aist.rtm.rtcbuilder.util.RTCUtil;

public abstract class AbstractEditorFormPage extends FormPage {

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractEditorFormPage.class);
	protected static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

	protected RtcBuilderEditor editor;
	protected BuildView buildview;
	protected Font titleFont;

	protected static final int EXEC_BUTTON_WIDTH = 70;

	/**
	 * コンストラクタ
	 *
	 */
	public AbstractEditorFormPage(RtcBuilderEditor editor, String id, String name) {
		super(editor, id, name);
		this.editor = editor;
		this.buildview = editor.getEMFmodel();
	}

	protected ScrolledForm createBase(final IManagedForm managedForm, String title) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;

		managedForm.getForm().getBody().setLayout(gl);
		managedForm.getForm().setShowFocusedControl(true);
		FormToolkit toolkit = managedForm.getToolkit();

		ScrolledForm form = toolkit.createScrolledForm(managedForm.getForm().getBody());
		gl = new GridLayout(2, false);
		gl.makeColumnsEqualWidth = true;
		form.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(gd);

		form.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(form.getBody());

		form.getBody().setLayout(gl);
		form.addMouseWheelListener(new MouseWheelListener(){
			@Override
			public void mouseScrolled(MouseEvent e) {
				int delta = e.count * 5;
				Point point =managedForm.getForm().getContent().getLocation();
				int margin = managedForm.getForm().getClientArea().height - managedForm.getForm().getContent().getSize().y;
				int newLocation = point.y + delta;
				if( 0<newLocation ) {
					newLocation = 0;
				} else if( newLocation < margin ) {
					newLocation = margin;
				}

				int selection = managedForm.getForm().getVerticalBar().getSelection();
				int newBarVal = selection - delta;
				point.y = newLocation;
				managedForm.getForm().getContent().setLocation(point);
				managedForm.getForm().getVerticalBar().setSelection(newBarVal);
			}
		});
		//
		Label label = toolkit.createLabel(form.getBody(), title);
		if( titleFont==null ) {
			titleFont = new Font(form.getDisplay(), IMessageConstants.TITLE_FONT, 16, SWT.BOLD);
		}
		label.setFont(titleFont);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		label.setLayoutData(gd);

		return form;
	}

	protected Composite createSectionBaseWithLabel(FormToolkit toolkit, ScrolledForm form,
			String title, String explain, int colnum) {
		return createSectionBaseWithLabel(toolkit, form, title, explain, colnum, 1);
	}
	protected Composite createSectionBaseWithLabel(FormToolkit toolkit, ScrolledForm form,
			String title, String explain, int colnum, int verSpan) {
		Section sctBasic = toolkit.createSection(form.getBody(),
							Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		sctBasic.setText(title);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalSpan = verSpan;
		sctBasic.setLayoutData(gridData);
		//
		Composite composite = toolkit.createComposite(sctBasic, SWT.NULL);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(composite);
		GridLayout gl = new GridLayout(colnum, false);
		composite.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		sctBasic.setClient(composite);
		//
		Label expl = toolkit.createLabel(composite, explain);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = colnum;
		expl.setLayoutData(gd);
		return composite;
	}

	protected Composite createHintSectionBase(FormToolkit toolkit, ScrolledForm form,
			int verSpan) {
		return createHintSectionBase(toolkit, form, IMessageConstants.HINT_TITLE, verSpan);
	}
	protected Composite createHintSectionBase(FormToolkit toolkit, ScrolledForm form,
			String hintTitle, int verSpan) {
		Section sctHint = toolkit.createSection(form.getBody(),
		Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		sctHint.setText(hintTitle);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.verticalSpan = verSpan;
		sctHint.setLayoutData(gd);
		//
		Composite composite = toolkit.createComposite(sctHint, SWT.NULL);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(composite);
		GridLayout gl = new GridLayout(2, false);
		composite.setLayout(gl);
		gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		sctHint.setClient(composite);
		return composite;
	}

	protected void createHintLabel(String desc, FormToolkit toolkit, Composite composite) {
		Label lblDesc = toolkit.createLabel(composite, desc);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		lblDesc.setLayoutData(gd);
	}
	protected void createHintLabel(String title, String desc, FormToolkit toolkit, Composite composite) {
		Label lblt1 = toolkit.createLabel(composite, title);
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		lblt1.setLayoutData(gd);
		toolkit.createLabel(composite, desc);
	}
	protected void createHintSpace(FormToolkit toolkit, Composite composite) {
		Label sep = toolkit.createLabel(composite, "");
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalSpan = 2;
		sep.setLayoutData(gd);
	}
	protected void createSpace(FormToolkit toolkit, Composite composite, int horizontalSpan) {
		Label sep = toolkit.createLabel(composite, "");
		GridData gd = new GridData();
		gd.verticalAlignment = GridData.BEGINNING;
		gd.horizontalSpan = horizontalSpan;
		sep.setLayoutData(gd);
	}

	protected TableViewer createTableViewer(FormToolkit toolkit, Composite composite) {
		return createTableViewer(toolkit, composite, 120);
	}
	protected TableViewer createTableViewer(FormToolkit toolkit, Composite composite, int height) {
		final TableViewer portParamTableViewer = new TableViewer(toolkit.createTable(composite,
						SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = height;
		gd.widthHint = 120;
		gd.grabExcessHorizontalSpace = true;
		portParamTableViewer.getTable().setLayoutData(gd);

		portParamTableViewer.getTable().setHeaderVisible(true);
		portParamTableViewer.getTable().setLinesVisible(true);
		portParamTableViewer.setContentProvider(new ArrayContentProvider());

		return portParamTableViewer;
	}

	protected Label createLabel(FormToolkit toolkit, Composite composite,
			String labelString, int hspan, Color color) {
		Label label = toolkit.createLabel(composite, labelString);
		if(color != null) {
			label.setForeground(color);
		}
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = hspan;
		label.setLayoutData(gridData);
		return label;
	}
	

	protected Text createLabelAndText(FormToolkit toolkit, Composite composite,
			String labelString) {
		return createLabelAndText(toolkit, composite, labelString, SWT.NONE, 0);
	}
	protected Text createLabelAndText(FormToolkit toolkit, Composite composite,
			String labelString, int style) {
		return createLabelAndText(toolkit, composite, labelString, style, 0);
	}
	protected Text createLabelAndText(FormToolkit toolkit, Composite composite,
			String labelString, int style, int color) {
		return createLabelAndText(toolkit, composite, labelString, style, color, 0);
	}
	protected Text createLabelAndText(FormToolkit toolkit, Composite composite,
			String labelString, int style, int color, int hspan) {
		return createLabelAndText(toolkit, composite, labelString, style, color, hspan, 0);
	}
	protected Text createLabelAndText(FormToolkit toolkit, Composite composite,
			String labelString, int style, int color, int hspan, int labelSpan) {
		if( labelString!=null && labelString.length()>0 ) {
			Label label = toolkit.createLabel(composite, labelString);
			if(0<labelSpan) {
				GridData lblData = new GridData();
				lblData.horizontalSpan = labelSpan;
				label.setLayoutData(lblData);
			}

			if(color>0 ) label.setForeground(getSite().getShell().getDisplay().getSystemColor(color));
		}

		final Text text = toolkit.createText(composite, "", style);
		text.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if( e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS ) {
					e.doit = true;
				}
			}
		});
		text.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				update();
			}

			public void keyPressed(KeyEvent e) {
			}
		});
		text.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
			}

			public void mouseUp(MouseEvent e) {
			}

		});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = hspan;
		
		GC gc = new GC(text);
		int fontHeight = gc.getFontMetrics().getHeight();
		gc.dispose();
		gridData.heightHint = fontHeight + 4;
		
		text.setLayoutData(gridData);
		return text;
	}

	protected Text createLabelAndRefText(FormToolkit toolkit, Composite composite,
			String labelString, int style, int hspan) {
		if( labelString!=null && labelString.length()>0 ) {
			toolkit.createLabel(composite, labelString);
		}

		final Text text = toolkit.createText(composite, "", style);
		text.setEditable(false);
		text.setBackground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = hspan;
		
		GC gc = new GC(text);
		int fontHeight = gc.getFontMetrics().getHeight();
		gc.dispose();
		gridData.heightHint = fontHeight + 4;

		text.setLayoutData(gridData);
		return text;
	}
	
	protected Combo createLabelAndCombo(FormToolkit toolkit, Composite composite,
			String labelString, String[] items) {
		return createLabelAndCombo(toolkit, composite, labelString, items, 0);
	}

	protected Combo createLabelAndCombo(FormToolkit toolkit, Composite composite,
			String labelString, String[] items, int color) {
		return createLabelAndCombo(toolkit, composite, labelString, items, color, 1);
	}
	protected Combo createLabelAndCombo(FormToolkit toolkit, Composite composite,
			String labelString, String[] items, int color, int hspan) {
		Label label = toolkit.createLabel(composite, labelString);
		if(color>0 ) label.setForeground(getSite().getShell().getDisplay().getSystemColor(color));
		Combo combo = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		combo.setItems(items);
		combo.select(0);
		combo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) { update(); }
			public void keyPressed(KeyEvent e) { }
		});
		combo.addSelectionListener(new SelectionListener() {
			  public void widgetDefaultSelected(SelectionEvent e){}
			  public void widgetSelected(SelectionEvent e){ update();}
			});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = hspan;
		combo.setLayoutData(gridData);

		return combo;
	}

	protected String getValue(String value) {
		String result = value;
		if (result == null) {
			result = "";
		}

		return result;
	}

	protected String getText(String text) {
		String result = text;
		if ("".equals(result)) {
			result = null;
		}
		return result;
	}

	protected Combo createEditableCombo(FormToolkit toolkit, Composite composite,
			String labelString, String key, String[] defaultValue, int color) {
		return createEditableCombo(toolkit, composite, labelString, key, defaultValue, color, 1);
	}
	protected Combo createEditableCombo(FormToolkit toolkit, Composite composite,
			String labelString, String key, String[] defaultValue, int color, int hspan) {
		Label label = toolkit.createLabel(composite, labelString);
		if(color>0) label.setForeground(getSite().getShell().getDisplay().getSystemColor(color));
		Combo combo = new Combo(composite, SWT.DROP_DOWN);
		loadDefaultComboValue(combo, key);
		combo.add("");
		for(int index=0;index<defaultValue.length;index++) {
			combo.add(defaultValue[index]);
		}

		combo.select(0);
		combo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) { update(); }
			public void keyPressed(KeyEvent e) { }
		});
		combo.addSelectionListener(new SelectionListener() {
			  public void widgetDefaultSelected(SelectionEvent e){}
			  public void widgetSelected(SelectionEvent e){ update(); }
			});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = hspan;
		combo.setLayoutData(gridData);

		return combo;
	}

	protected Combo createCombo(FormToolkit toolkit, Composite composite,
			String labelString, String[] defaultValue) {
		return createCombo(toolkit, composite, labelString, defaultValue,
							SWT.COLOR_BLACK, 1);
	}
	protected Combo createCombo(FormToolkit toolkit, Composite composite,
			String labelString, String[] defaultValue, int color, int hspan) {
		Label label = toolkit.createLabel(composite, labelString);
		if(color>0) label.setForeground(getSite().getShell().getDisplay().getSystemColor(color));
		Combo combo = new Combo(composite, SWT.READ_ONLY);
		for(int index=0;index<defaultValue.length;index++) {
			combo.add(defaultValue[index]);
		}

		combo.select(0);
		combo.addSelectionListener(new SelectionListener() {
			  public void widgetDefaultSelected(SelectionEvent e){}
			  public void widgetSelected(SelectionEvent e){ update(); }
			});
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = hspan;
		combo.setLayoutData(gridData);

		return combo;
	}

	protected Button createRadioCheckButton(FormToolkit toolkit,
			Composite composite, String labelString, int style) {
		Button radio = toolkit.createButton(composite, "", style);
		radio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) { update(); }
		});
		radio.setText(labelString);

		GridData gd = new GridData();
		gd.widthHint = 120;
		radio.setLayoutData(gd);
		
		return radio;
	}

	protected Button createLanguageRadioButton(FormToolkit toolkit,
			Composite composite, String labelString) {
		Button radio = toolkit.createButton(composite, "", SWT.RADIO);
		radio.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) { update(); }
		});
		radio.setText(labelString);

		GridData gd = new GridData();
		gd.widthHint = 80;
		radio.setLayoutData(gd);
		
		return radio;
	}

	protected TableViewerColumn createColumn(TableViewer tv, String title, int width){
		TableViewerColumn col = new TableViewerColumn(tv, SWT.NONE);
		col.getColumn().setText(title);
		col.getColumn().setWidth(width);
		col.getColumn().setResizable(true);
		col.getColumn().setMoveable(false);
		return col;
	}

	protected Text createLabelAndFile(FormToolkit toolkit, Composite composite,
			final String extention, String labelString, int color, int style) {
		Label label = toolkit.createLabel(composite, labelString);
		if(color>0) label.setForeground(getSite().getShell().getDisplay().getSystemColor(color));
		final Text text = toolkit.createText(composite, "", style);
		text.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				update();
			}
			public void keyPressed(KeyEvent e) {
			}
		});
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		GC gc = new GC(text);
		int fontHeight = gc.getFontMetrics().getHeight();
		gc.dispose();
		gd.heightHint = fontHeight + 4;

		text.setLayoutData(gd);

		Button checkButton = toolkit.createButton(composite, "Browse...", SWT.PUSH);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		checkButton.setLayoutData(gd);
		checkButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getEditorSite().getShell());
				dialog.setFilterExtensions(new String[] { "*." + extention });
				if (text.getText().length() > 0)
					dialog.setFileName(text.getText());
				String newPath = dialog.open();
				if (newPath != null) {
					text.setText(newPath);
					update();
				}
			}
		});

		return text;
	}

	/**
	 * ワークスペースの永続情報から、コンボのリストと選択インデックスをロードする
	 *
	 * @param combo
	 */
	private void loadDefaultComboValue(Combo combo, String key) {
		String defaultString = RtcBuilderPlugin.getDefault().getPreferenceStore()
				.getString(key);
		StringTokenizer tokenize = new StringTokenizer(defaultString, ",");
		String[] items = combo.getItems();
		List<String> baseList = Arrays.asList(items);
		List<String> itemList = new ArrayList<>(baseList);
		while (tokenize.hasMoreTokens()) {
			String item = tokenize.nextToken();
			if(itemList.contains(item)) continue;
			combo.add(item);
			itemList.add(item);
		}
	}

	protected String[] extractDataTypes() {
		RTCUtil.getIDLPathes(editor.getRtcParam());
		String FS = System.getProperty("file.separator");
		List<DataTypeParam> sourceContents = new ArrayList<DataTypeParam>();
		
        List<String> exclusionList = Arrays.asList(
        		"componentobserver.idl", "dataport.idl", "manager.idl",
        		"openrtm.idl", "rtc.idl", "sdopackage.idl",
        		"sharedmemory.idl");
		
		for (IdlPathParam source : editor.getRtcParam().getIdlSearchPathList()) {
			try {
				File idlDir = new File(source.getPath());
				String[] list = idlDir.list();
				if (list == null) {
					continue;
				}
				List<String> idlNames = new ArrayList<String>();
				for (String name : list) {
					if (name.toLowerCase().endsWith(".idl")) {
	                	if(source.isDefault() && exclusionList.contains(name.toLowerCase()) ) continue;
						idlNames.add(name);
					}
				}
				Collections.sort(idlNames, new Comparator<String>() {
					public int compare(String a, String b) {
						return a.compareTo(b);
					}
				});
				for (String idlName : idlNames) {
					String idlContent = FileUtil.readFile(source.getPath() + FS + idlName);
					DataTypeParam param = new DataTypeParam();
					param.setContent(idlContent);
					param.setDispPath(source.getDispPath() + FS + idlName);
					param.setFullPath(source.getPath() + FS + idlName);
					param.setDefault(source.isDefault());
					sourceContents.add(param);
				}
			} catch (IOException e) {
				LOGGER.error("Fail to read idl file", e);
			}
		}
		String[] defaultTypeList = new String[0];
		List<String> dataTypes = new ArrayList<String>();
		StringBuilder builder = new StringBuilder();
		if( IDLParamConverter.extractTypeDef(sourceContents, dataTypes, builder)==false ) {
			MessageDialog.openWarning(RtcBuilderPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
					"IDL Parse", IMessageConstants.IDL_PARSE_EROOR + System.lineSeparator() + builder.toString());
		}
		defaultTypeList = new String[dataTypes.size()];
		defaultTypeList = dataTypes.toArray(defaultTypeList);
		//
		editor.getGeneratorParam().getDataTypeParams().clear();
		editor.getGeneratorParam().getDataTypeParams().addAll(sourceContents);
		//
		return defaultTypeList;
	}
	
	protected List<String> extractROSEtcTypes(String projectName, String source) {
		List<String> typeList = new ArrayList<String>();

		IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceHandle.getProject(projectName);
		IFolder srcFolder = project.getFolder(source);
		if (srcFolder.exists()) {
			try {
				srcFolder.refreshLocal(IResource.DEPTH_INFINITE, null);
			    IResource[] members = srcFolder.members();
			    String targetExtension = source;
	
			    for (IResource resource : members) {
			        if (resource.getType() == IResource.FILE && targetExtension.equals(resource.getFileExtension())) {
			        	IFile file = (IFile)resource;
			        	String fullName = file.getName();
			            String extension = file.getFileExtension();
			            String nameWithoutExtension;

			            if (extension != null && !extension.isEmpty()) {
			                nameWithoutExtension = fullName.substring(0, fullName.length() - (extension.length() + 1));
			            } else {
			                nameWithoutExtension = fullName;
			            }			        	
			        	typeList.add(nameWithoutExtension);
			        }
			    }
			} catch(CoreException ex) {
			}
		}
	    return typeList;
	}

	abstract protected void update();

	abstract public void load();

	abstract public String validateParam();

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			load();
		}
	}

	@Override
	public void dispose() {
		if( titleFont!=null ) titleFont.dispose();
		super.dispose();
	}

	public void pageSelected(){
		// ページが選択されたときに処理が必要な場合は、これをオーバーライドする
	}

	/**
	 * CompositeにBackgroundColorを指定する。
	 * Compositeが子を持つ場合には子のBackgroundColorも指定する。
	 * 子がCompositeの場合には再起呼び出しを行う。
	 * 指定したCompositeの下にあるControlすべてが同じBackgroundColorになる。
	 *
	 * @param composit
	 * @param color
	 */
	protected void setEnableBackground(Composite composit,Color color) {
		if(composit == null) return;
		composit.setBackground(color);
		if (composit.getChildren() == null ) return;
		for(Control child : composit.getChildren()) {
			if (child instanceof Composite) {
				setEnableBackground((Composite) child,color);
			} else {
				child.setBackground(color);
			}
		}
	}

	/**
	 * フォーム内の要素を指し示すオブジェクト。
	 */
	public static class WidgetInfo {
		String formName;

		String sectionName;

		String widgetName;

		public WidgetInfo(String formName, String sectionName, String widgetName) {
			this.formName = (formName != null) ? formName : "";
			this.sectionName = (sectionName != null) ? sectionName : "";
			this.widgetName = (widgetName != null) ? widgetName : "";
		}

		public boolean matchForm(String s) {
			if (formName.equals("*") || formName.equals(s)) {
				return true;
			}
			return false;
		}

		public boolean matchSection(String s) {
			if (sectionName.equals("*") || sectionName.equals(s)) {
				return true;
			}
			return false;
		}

		public boolean matchWidget(String s) {
			if (widgetName.equals("*") || widgetName.equals(s)) {
				return true;
			}
			return false;
		}
	}

	/**
	 * フォーム内の要素の有効/無効を設定します。
	 *
	 * @param widgetInfo
	 *            フォーム内の要素のアドレス情報
	 * @param enabled
	 *            有効の場合は true
	 */
	public void setEnabledInfo(WidgetInfo widgetInfo, boolean enabled) {
	}

	/**
	 * 有効/無効時の背景色を取得します。
	 *
	 * @param enabled
	 *            有効の場合は true
	 * @return 有効の場合は SWT.COLOR_LIST_BACKGROUND、無効の場合は
	 *         SWT.COLOR_WIDGET_LIGHT_SHADOW
	 */
	public Color getBackgroundByEnabled(boolean enabled) {
		int c = (enabled) ? SWT.COLOR_LIST_BACKGROUND
				: SWT.COLOR_WIDGET_LIGHT_SHADOW;
		return getSite().getShell().getDisplay().getSystemColor(c);
	}

	/**
	 * Viewerの有効/無効を設定します。
	 *
	 * @param viewer
	 *            Viewerオブジェクト
	 * @param enabled
	 *            有効の場合は true
	 */
	public void setViewerEnabled(Viewer viewer, boolean enabled) {
		if (viewer == null) {
			return;
		}
		Color color = getBackgroundByEnabled(enabled);
		viewer.getControl().getParent().setEnabled(enabled);
		viewer.getControl().setBackground(color);
	}

	/**
	 * Controlの有効/無効を設定します。
	 *
	 * @param control
	 *            Controlオブジェクト
	 * @param enabled
	 *            有効の場合は true
	 */
	public void setControlEnabled(Control control, boolean enabled) {
		if (control == null) {
			return;
		}
		Color color = getBackgroundByEnabled(enabled);
		control.setEnabled(enabled);
		control.setBackground(color);
	}

	/**
	 * Buttonの有効/無効を設定します。
	 *
	 * @param button
	 *            Buttonオブジェクト
	 * @param enabled
	 *            有効の場合は true
	 */
	public void setButtonEnabled(Button button, boolean enabled) {
		if (button == null) {
			return;
		}
		button.setEnabled(enabled);
	}

}
