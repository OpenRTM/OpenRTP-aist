	package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * ドキュメントページ
 */
public class ROSDocumentEditorFormPage extends AbstractEditorFormPage {

	private Text algorithmText;
	private Text inoutText;
	private Text creatorText;
	private Text referenceText;

	/**
	 * コンストラクタ
	 * 
	 * @param editor
	 *            親のエディタ
	 */
	public ROSDocumentEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", IMessageConstants.DOCUMENT_SECTION);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, IMessageConstants.DOCUMENT_SECTION);
		FormToolkit toolkit = managedForm.getToolkit();
		//
		createOverViewSection(toolkit, form);
		createHintSection(toolkit, form);

		load();
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 3);
		createHintLabel(Messages.getString("IMC.ROS_DOCUMENT_OVERVIEW_TITLE"),
				Messages.getString("IMC.ROS_DOCUMENT_HINT_COMPONENT_DESC"), toolkit, composite);
	}
	
	private void createOverViewSection(FormToolkit toolkit, ScrolledForm form) {
		Section sctOverView = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		sctOverView.setText(Messages.getString("IMC.ROS_DOCUMENT_OVERVIEW_TITLE"));
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		sctOverView.setLayoutData(gridData);
		//
		Composite composite = toolkit.createComposite(sctOverView, SWT.NULL);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(composite);
		GridLayout gl = new GridLayout(2, false);
		composite.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		sctOverView.setClient(composite);

		algorithmText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_DOCUMENT_LBL_ALGORITHM"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData();
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		gridData.widthHint = 100;
		algorithmText.setLayoutData(gridData);
		inoutText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_DOCUMENT_LBL_INOUT"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		inoutText.setLayoutData(gridData);
		creatorText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_DOCUMENT_LBL_CREATOR"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		creatorText.setLayoutData(gridData);
		referenceText = createLabelAndText(toolkit, composite,
				Messages.getString("IMC.ROS_DOCUMENT_LBL_REFERENCE"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		referenceText.setLayoutData(gridData);
	}

	public void update() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();

		if( algorithmText != null ) {
			rosParam.setDocAlgorithm(StringUtil.getDocText(algorithmText.getText()));
			rosParam.setDocInOut(StringUtil.getDocText(inoutText.getText()));
			rosParam.setDocCreator(StringUtil.getDocText(creatorText.getText()));
			rosParam.setDocReference(StringUtil.getDocText(referenceText.getText()));
		}

		editor.updateDirty();
	}

	/**
	 * データをロードする
	 */
	public void load() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();

		if( algorithmText != null ) {
			algorithmText.setText(StringUtil.getDisplayDocText(getValue(rosParam.getDocAlgorithm())));
			inoutText.setText(StringUtil.getDisplayDocText(getValue(rosParam.getDocInOut())));
			creatorText.setText(StringUtil.getDisplayDocText(getValue(rosParam.getDocCreator())));
			referenceText.setText(StringUtil.getDisplayDocText(getValue(rosParam.getDocReference())));
		}
	}

	public String validateParam() {
		//入力パラメータチェックなし
		return null;
	}
}
