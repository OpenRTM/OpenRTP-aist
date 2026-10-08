package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.TextViewerUndoManager;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.CompositeRuler;
import org.eclipse.jface.text.source.LineNumberRulerColumn;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.ProfileHandlerROS;
import jp.go.aist.rtm.rtcbuilder.ui.compare.CompareResultDialog;
import jp.go.aist.rtm.rtcbuilder.ui.compare.CompareTarget;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.ui.editors.xmlEditor.ColorManager;
import jp.go.aist.rtm.rtcbuilder.ui.editors.xmlEditor.XMLConfiguration;
import jp.go.aist.rtm.rtcbuilder.ui.editors.xmlEditor.XMLPartitionScanner;

public class ROSXmlEditorFormPage extends AbstractEditorFormPage {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ROSXmlEditorFormPage.class);

	private SourceViewer ROSXmlViewer;
	private Document rosDocument;
	SourceTextListener sourceTextListener;
	private boolean isUpdatedOriginal = true;
	private String originalContent;
	//
	private ColorManager colorManager;
	//
	private final int KEYCODE_A = 97;
	private final int KEYCODE_Y = 121;
	private final int KEYCODE_Z = 122;
	//
	private Font cautionFont;

	/**
	 * コンストラクタ
	 * 
	 * @param editor
	 *            親のエディタ
	 */
	public ROSXmlEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.ROSXML_SECTION"));
		this.editor = editor;
		rosDocument = new Document();
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;

		managedForm.getForm().getBody().setLayout(gl);

		ScrolledForm form = managedForm.getToolkit().createScrolledForm(
				managedForm.getForm().getBody());
		gl = new GridLayout(1, false);
		form.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_BOTH);
		form.setLayoutData(gd);

		form.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		managedForm.getToolkit().paintBordersFor(form.getBody());
		form.getBody().setLayout(gl);
		//
		Label title = managedForm.getToolkit().createLabel(form.getBody(), Messages.getString("IMC.ROSXML_SECTION"));
		if( titleFont==null ) {
			titleFont = new Font(form.getDisplay(), IMessageConstants.TITLE_FONT, 16, SWT.BOLD);
		}
		title.setFont(titleFont);
		//
		cautionFont = new Font(form.getDisplay(), IMessageConstants.TITLE_FONT, 12, SWT.BOLD);
		Label caution = managedForm.getToolkit().createLabel(form.getBody(), IMessageConstantsROS.ROSXML_CAUTION);
		caution.setFont(cautionFont);
		caution.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		//
		createUpdateButton(managedForm, form);
		//
		Composite composite = managedForm.getToolkit().createComposite(form.getBody(), SWT.NULL);
		composite.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		composite.setLayoutData(gd);
		composite.setLayout(new FillLayout(SWT.VERTICAL));
		//
		CompositeRuler ruler = new CompositeRuler();
		LineNumberRulerColumn lineCol = new LineNumberRulerColumn();
		lineCol.setBackground(form.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		lineCol.setForeground(form.getDisplay().getSystemColor(SWT.COLOR_BLACK));
		ruler.addDecorator(0, lineCol);
		
		ROSXmlViewer = new SourceViewer(composite, ruler , SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		ROSXmlViewer.getTextWidget().addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if( (e.stateMask & SWT.CTRL)!=0 && e.keyCode == KEYCODE_A ) {
					ROSXmlViewer.doOperation(ITextOperationTarget.SELECT_ALL);
				} else if( (e.stateMask & SWT.CTRL)!=0 && e.keyCode == KEYCODE_Y ) {
					ROSXmlViewer.doOperation(ITextOperationTarget.REDO);
				} else if( (e.stateMask & SWT.CTRL)!=0 && e.keyCode == KEYCODE_Z ) {
					ROSXmlViewer.doOperation(ITextOperationTarget.UNDO);
				}
			}

			public void keyReleased(KeyEvent e) {
			}
		});
		final TextViewerUndoManager undoMgr = new TextViewerUndoManager(99);
		ROSXmlViewer.setUndoManager(undoMgr);
		undoMgr.connect(ROSXmlViewer);
		ROSXmlViewer.getTextWidget().addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent e) {
				undoMgr.endCompoundChange();
			}
		});
		
		IDocumentPartitioner partitioner = new FastPartitioner(
						new XMLPartitionScanner(),
		    	        new String[] { 
							XMLPartitionScanner.XML_TAG,
							XMLPartitionScanner.XML_COMMENT,
							XMLPartitionScanner.XML_DOCTAG
							});
	    rosDocument.setDocumentPartitioner(partitioner);
	    partitioner.connect(rosDocument);
		colorManager = new ColorManager();
		ROSXmlViewer.configure(new XMLConfiguration(colorManager));
		ROSXmlViewer.setDocument(rosDocument);
		//
		load();
	}

	private void createUpdateButton(IManagedForm managedForm, ScrolledForm form) {
		Button updateButton = managedForm.getToolkit().createButton(form.getBody(), 
										IMessageConstants.COMMON_LABEL_UPDATE, SWT.PUSH);
		updateButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					ProfileHandlerROS handler = new ProfileHandlerROS();
					if( !handler.validateROSXml(ROSXmlViewer.getDocument().get()) ) return;
				} catch (Exception ex) {
					String errMessage = null;
					if( ex.getCause()==null ) {
						errMessage = ex.getMessage();
					} else {
						errMessage = ex.getCause().toString();
					}
					MessageDialog.openError(getSite().getShell(), IMessageConstants.RTCXML_MSG_VALIDATE_ERROR, errMessage);
					return;
				}
				if(!originalContent.equals(ROSXmlViewer.getDocument().get())) {
					try {
						CompareTarget target = new CompareTarget();
						target.setTargetName(IMessageConstants.RTCXML_MSG_COMPARE);
						target.setOriginalSrc(originalContent);
						target.setGenerateSrc(ROSXmlViewer.getDocument().get());
						target.setCanMerge(false);
						//
						CompareResultDialog dialog = new CompareResultDialog(
								getSite().getShell(),
								target,
								CompareResultDialog.MODE_OK_CANCEL,
								"Current");
						
//						ProfileCompareDialog dialog = new ProfileCompareDialog(getSite().getShell());
//						dialog.setOldProfile(handler.restorefromXML(originalContent));
//						dialog.setNewProfile(handler.restorefromXML(RTCXmlViewer.getDocument().get()));
						int intRet = dialog.open();
						if( intRet == IDialogConstants.OK_ID ) {
							((ROSBuilderEditor)editor).updateProfiles(ROSXmlViewer.getDocument().get());
							originalContent = ROSXmlViewer.getDocument().get();
							MessageDialog.openInformation(getSite().getShell(), 
									IMessageConstants.RTCXML_MSG_UPDATE, IMessageConstants.RTCXML_MSG_DONEUPDATE);
						}
					} catch (Exception e1) {
						MessageDialog.openError(getSite().getShell(), IMessageConstants.RTCXML_MSG_UPDATE_ERROR, e1.getMessage());
						return;
					}
					
				} else {
					MessageDialog.openInformation(getSite().getShell(), 
							IMessageConstants.RTCXML_MSG_UPDATE, IMessageConstants.RTCXML_MSG_NOUPDATE);
				}
			}
		});
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		gd.widthHint = 100;
		updateButton.setLayoutData(gd);
	}

	public void update() {
		String newDoc = ROSXmlViewer.getDocument().get();
		//
		((ROSBuilderEditor)editor).getROSParam().setROSXml(newDoc);
		editor.updateDirty();
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (ROSXmlViewer == null) return;
		//
		ProfileHandlerROS handler = new ProfileHandlerROS();
		String xml = "";
		try {
			xml = handler.convert2ROSXML(editor.getGeneratorParam());
		} catch (Exception e) {
			String message = e.getMessage();
			if (message != null && !"".equals(message)) {
				MessageDialog.openError(getSite().getShell(), "Error", message);
			} else {
				LOGGER.error("Fail to convert", e);
			}
		}
		//
		if (sourceTextListener == null) {
			sourceTextListener = new SourceTextListener();
		}
		// XML編集開始前の初期ドキュメント設定時に updateしないようリスナを解除
		ROSXmlViewer.removeTextListener(sourceTextListener);
		//
		originalContent = ((ROSBuilderEditor)editor).getROSParam().getROSXml();
		rosDocument.set(xml);
		//
		ROSXmlViewer.addTextListener(sourceTextListener);
	}

	public String validateParam() {
		return null;
	}

	private class SourceTextListener implements ITextListener {
		public void textChanged(TextEvent event) {
			update();
		}
	}

	@Override
	public void setActive(boolean active) {
		super.setActive(active);
		if (active) {
			// XML編集開始前の dirty設定を保存しておく
			isUpdatedOriginal = ((ROSBuilderEditor)editor).getROSParam().isUpdated();
			load();
		} else {
			// XML編集開始前の dirty設定に戻す
			if (!isUpdatedOriginal) {
				((ROSBuilderEditor)editor).getROSParam().resetUpdated();
				editor.updateDirty();
			}
		}
	}

	@Override
	public void dispose() {
		if( cautionFont!=null ) cautionFont.dispose();
		super.dispose();
	}
}
