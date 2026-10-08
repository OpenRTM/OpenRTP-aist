	package jp.go.aist.rtm.rtcbuilder.ui.editors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import jp.go.aist.rtm.rtcbuilder.generator.param.RtcParam;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * ドキュメントページ
 */
public class DocumentEditorFormPage extends AbstractEditorFormPage {

	private Text descriptionText;
	private Text inoutText;
	private Text algorithmText;
	private Text referenceText;
	//
	private Text creatorText;
	private Text licenseText;

	/**
	 * コンストラクタ
	 * 
	 * @param editor
	 *            親のエディタ
	 */
	public DocumentEditorFormPage(RtcBuilderEditor editor) {
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
		createEtcSection(toolkit, form);

		load();
	}

	private void createHintSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createHintSectionBase(toolkit, form, 3);
		createHintLabel(IMessageConstants.DOCUMENT_HINT_COMPONENT_TITLE, IMessageConstants.DOCUMENT_HINT_COMPONENT_DESC, toolkit, composite);
		createHintLabel(IMessageConstants.DOCUMENT_HINT_ETC_TITLE, IMessageConstants.DOCUMENT_HINT_ETC_DESC, toolkit, composite);
		createHintLabel(IMessageConstants.DOCUMENT_HINT_CREATOR_TITLE, IMessageConstants.DOCUMENT_HINT_CREATOR_DESC, toolkit, composite);
	}
	
	private void createOverViewSection(FormToolkit toolkit, ScrolledForm form) {
		Section sctOverView = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		sctOverView.setText(IMessageConstants.DOCUMENT_OVERVIEW_TITLE);
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

		descriptionText = createLabelAndText(toolkit, composite,
				IMessageConstants.DOCUMENT_LBL_DESCRIPTION, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData();
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		gridData.widthHint = 100;
		descriptionText.setLayoutData(gridData);
		inoutText = createLabelAndText(toolkit, composite,
				IMessageConstants.DOCUMENT_LBL_INOUT, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		inoutText.setLayoutData(gridData);
		algorithmText = createLabelAndText(toolkit, composite,
				IMessageConstants.DOCUMENT_LBL_ALGORITHM, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		algorithmText.setLayoutData(gridData);
		referenceText = createLabelAndText(toolkit, composite,
				IMessageConstants.DOCUMENT_LBL_REFERENCE, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		referenceText.setLayoutData(gridData);
	}

	private void createEtcSection(FormToolkit toolkit, ScrolledForm form) {
		Section sctEtc = toolkit.createSection(form.getBody(),
				Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);
		sctEtc.setText(IMessageConstants.DOCUMENT_ETC_TITLE);
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalAlignment = GridData.BEGINNING;
		sctEtc.setLayoutData(gridData);
		//
		Composite composite = toolkit.createComposite(sctEtc, SWT.NULL);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		toolkit.paintBordersFor(composite);
		GridLayout gl = new GridLayout(2, false);
		composite.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gd);
		sctEtc.setClient(composite);

		creatorText = createLabelAndText(toolkit, composite,
				IMessageConstants.DOCUMENT_LBL_CREATOR, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = 50;
		gridData.widthHint = 100;
		creatorText.setLayoutData(gridData);
		licenseText = createLabelAndText(toolkit, composite,
				IMessageConstants.DOCUMENT_LBL_LICENSE, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
		licenseText.setLayoutData(gridData);
	}

	public void update() {
		RtcParam rtcParam = editor.getRtcParam();

		if( descriptionText != null ) {
			rtcParam.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			rtcParam.setDocInOut(StringUtil.getDocText(inoutText.getText()));
			rtcParam.setDocAlgorithm(StringUtil.getDocText(algorithmText.getText()));
			//
			rtcParam.setDocCreator(StringUtil.getDocText(creatorText.getText()));
			rtcParam.setDocLicense(StringUtil.getDocText(licenseText.getText()));
			rtcParam.setDocReference(StringUtil.getDocText(referenceText.getText()));
		}

		editor.updateDirty();
	}

	/**
	 * データをロードする
	 */
	public void load() {
		RtcParam rtcParam = editor.getRtcParam();

		if( descriptionText != null ) {
			descriptionText.setText(StringUtil.getDisplayDocText(getValue(rtcParam.getDocDescription())));
			inoutText.setText(StringUtil.getDisplayDocText(getValue(rtcParam.getDocInOut())));
			algorithmText.setText(StringUtil.getDisplayDocText(getValue(rtcParam.getDocAlgorithm())));
			//
			creatorText.setText(StringUtil.getDisplayDocText(getValue(rtcParam.getDocCreator())));
			licenseText.setText(StringUtil.getDisplayDocText(getValue(rtcParam.getDocLicense())));
			
			referenceText.setText(StringUtil.getDisplayDocText(getValue(rtcParam.getDocReference())));
		}
	}

	public String validateParam() {
		//入力パラメータチェックなし
		return null;
	}
}
