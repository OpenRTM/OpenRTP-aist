package jp.go.aist.rtm.systemeditor.ui.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EventObject;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.tools.MarqueeDragTracker;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.parts.GraphicalEditor;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.openrtp.namespaces.rts.version02.RtsProfileExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;

import jp.go.aist.rtm.systemeditor.RTSystemEditorPlugin;
import jp.go.aist.rtm.systemeditor.extension.SaveProfileExtension;
import jp.go.aist.rtm.systemeditor.factory.ProfileSaver;
import jp.go.aist.rtm.systemeditor.nl.Messages;
import jp.go.aist.rtm.systemeditor.ui.action.OpenCompositeComponentAction;
import jp.go.aist.rtm.systemeditor.ui.dialog.ProfileInformationDialog;
import jp.go.aist.rtm.systemeditor.ui.editor.action.MoveComponentAction;
import jp.go.aist.rtm.systemeditor.ui.editor.action.OpenAction;
import jp.go.aist.rtm.systemeditor.ui.editor.dnd.SystemDiagramDropTargetListener;
import jp.go.aist.rtm.systemeditor.ui.editor.editpart.AutoScrollAutoexposeHelper;
import jp.go.aist.rtm.systemeditor.ui.editor.editpart.SystemDiagramEditPart;
import jp.go.aist.rtm.systemeditor.ui.editor.editpart.factory.SystemDiagramEditPartFactory;
import jp.go.aist.rtm.systemeditor.ui.util.ComponentUtil;
import jp.go.aist.rtm.toolscommon.model.component.Component;
import jp.go.aist.rtm.toolscommon.model.component.ComponentFactory;
import jp.go.aist.rtm.toolscommon.model.component.CorbaComponent;
import jp.go.aist.rtm.toolscommon.model.component.SystemDiagram;
import jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler;
import jp.go.aist.rtm.toolscommon.synchronizationframework.SynchronizationSupport;
import jp.go.aist.rtm.toolscommon.ui.views.propertysheetview.RtcPropertySheetPage;
import jp.go.aist.rtm.toolscommon.util.RtsProfileHandler;
import jp.go.aist.rtm.toolscommon.validation.ValidateException;
import jp.go.aist.rtm.toolscommon.validation.Validator;

public abstract class AbstractSystemDiagramEditor extends GraphicalEditor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractSystemDiagramEditor.class);

	static final String DIALOG_TITLE_CONFIRM = Messages
			.getString("Common.dialog.confirm_title");
	static final String DIALOG_TITLE_ERROR = Messages
			.getString("Common.dialog.error_title");

	public static final String MSG_TITLE_VALIDATION_ERROR = Messages.getString("AbstractSystemDiagramEditor.33");

	/**
	 * システムダイアグラムの拡張子
	 */
	public static final String FILE_EXTENTION = "xml"; //$NON-NLS-1$

	/**
	 * RTCLinkのプロジェクト名
	 */
	public static String RTCLINK_PROJECT_NAME = "RTSE_Files"; //$NON-NLS-1$

	private String title = null;

	private SystemDiagram systemDiagram;

	private SystemDiagramPropertyChangeListener systemDiagramPropertyChangeListener;

	protected String diagramName;

	public AbstractSystemDiagramEditor() {
		setEditDomain(new DefaultEditDomain(this));
		getEditDomain().setCommandStack(new ConnectCancelCommandStack());
	}

	/**
	 * @return openEditorの引数として渡される（オンラインまたはオフラインの）エディタID
	 */
	abstract public String getEditorId();
	
	private String FS = System.getProperty("file.separator");

	@SuppressWarnings("unchecked")
	@Override
	protected void createActions() {
		super.createActions();

		IAction action;

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_UP_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_DOWN_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_RIGHT_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_LEFT_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_UP_L_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_DOWN_L_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_RIGHT_L_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MoveComponentAction(this,
				MoveComponentAction.MOVE_LEFT_L_ACTION_ID);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new OpenCompositeComponentAction(this);
		getActionRegistry().registerAction(action);
		getSelectionActions().add(action.getId());

		action = new SaveAction(this) {
			@Override
			protected void init() {
				setId(ActionFactory.SAVE.getId());
				setText("Save System");
				setToolTipText("Save System");
			}

			@Override
			public void run() {
				doSave(null);
			}
		};
		getActionRegistry().registerAction(action);
		getPropertyActions().add(action.getId());

		action = new SaveAction(this) {
			@Override
			protected boolean calculateEnabled() {
				return true;
			}

			@Override
			protected void init() {
				setId(ActionFactory.SAVE_AS.getId());
				setText("Save As System");
				setToolTipText("Save As System");
			}

			@Override
			public void run() {
				doSaveAs();
			}
		};
		getActionRegistry().registerAction(action);
		getPropertyActions().add(action.getId());

		action = new OpenAction(this);
		getActionRegistry().registerAction(action);
		getPropertyActions().add(action.getId());

	}

	@Override
	protected void createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new ScrollingGraphicalViewer() {

			@Override
			public EditPart findObjectAtExcluding(Point pt, Collection exclude,
					Conditional condition) {
				EditPart result = super.findObjectAtExcluding(pt, exclude,
						condition);
				if (condition instanceof AutoexposeHelper.Search
						&& ((AutoexposeHelper.Search) condition).result == null) {
					((AutoexposeHelper.Search) condition).result = (AutoexposeHelper) getRootEditPart()
							.getAdapter(AutoexposeHelper.class);
				}
				return result;
			}
		};
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
	}

	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();

		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setRootEditPart(new ScalableRootEditPart() {
			@Override
			public Object getAdapter(Class adapter) {
				if (adapter == AutoexposeHelper.class) {
					return new AutoScrollAutoexposeHelper(this);
				}

				return super.getAdapter(adapter);
			}
			@Override
			public DragTracker getDragTracker(Request req) {
				return new MarqueeDragTracker(){

					@Override
					protected boolean handleButtonDown(int button) {
						boolean handleButtonDown = super.handleButtonDown(button);
						if (button == 3) { // right click
							deselectAll();
						}
						return handleButtonDown;
					}

				};
			}
		});
		viewer.setEditPartFactory(new SystemDiagramEditPartFactory(
				getActionRegistry()));
	}


	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();
		viewer
				.addDropTargetListener((TransferDropTargetListener) new SystemDiagramDropTargetListener(
						viewer));

		ContextMenuProvider provider = new AbstractSystemDiagramContextMenuProvider(
				viewer, getActionRegistry());
		viewer.setContextMenu(provider);
		((IEditorSite) getSite()).registerContextMenu(provider, viewer, false);

		buildKeyHandler(viewer);

		systemDiagramPropertyChangeListener = new SystemDiagramPropertyChangeListener(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage());
		systemDiagram
				.addPropertyChangeListener(systemDiagramPropertyChangeListener);
		viewer.setContents(systemDiagram);
	}

	private void buildKeyHandler(GraphicalViewer viewer) {
		KeyHandler keyHandler = new KeyHandler();
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_UP, 0),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_UP_ACTION_ID));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_DOWN, 0),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_DOWN_ACTION_ID));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_RIGHT, 0),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_RIGHT_ACTION_ID));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_LEFT, 0),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_LEFT_ACTION_ID));

		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_UP, SWT.SHIFT),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_UP_L_ACTION_ID));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_DOWN, SWT.SHIFT),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_DOWN_L_ACTION_ID));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_RIGHT, SWT.SHIFT),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_RIGHT_L_ACTION_ID));
		keyHandler.put(KeyStroke.getPressed(SWT.ARROW_LEFT, SWT.SHIFT),
				getActionRegistry().getAction(
						MoveComponentAction.MOVE_LEFT_L_ACTION_ID));

		viewer.setKeyHandler(keyHandler);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		IEditorInput input = getInput();
		if (input instanceof NullEditorInput) { // 新規エディタ
			doSaveAs();
			return;
		}
		IFile file = ((IFileEditorInput) input).getFile();

		// オンラインでも毎回ダイアログを出すようにしておく 2008.12.11
		ProfileInformationDialog dialog = new ProfileInformationDialog(
				getSite().getShell());
		dialog.setSystemId(getSystemDiagram().getSystemId());
		dialog.setOverWrite(true);
		
		java.nio.file.Path rawFile = Paths.get(file.getLocation().toOSString());
		dialog.setInputPath(rawFile.getParent().getParent().toString());
		dialog.setSystemDiagram(getSystemDiagram());
		if (dialog.open() != IDialogConstants.OK_ID) {
			return;
		}

		String systemName = dialog.getInputSystemName();
		boolean isReplace = dialog.isReplace();

		getSystemDiagram().setSystemId(dialog.getSystemId());
		XMLGregorianCalendar calendar = new XMLGregorianCalendarImpl(new GregorianCalendar());
		calendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		getSystemDiagram().setUpdateDate(calendar.toString());

		// TODO バージョンアップログへの対応

		// ダイアグラムのバリデーション
		if (!validateDiagram()) {
			return;
		}

		try {
			save(file, systemName, isReplace, monitor);
		} catch (CoreException e) {
			LOGGER.error("Fail to save. file=" + file, e);
			ErrorDialog.openError(getSite().getShell(), "Error During Save",
					"The current model could not be saved.", e.getStatus());
		}
	}

	private IEditorInput getInput() {
		IEditorInput input = getEditorInput();
		SystemDiagram diagram = systemDiagram.getParentSystemDiagram();
		while (diagram != null) {
			AbstractSystemDiagramEditor editor = ComponentUtil.findEditor(diagram);
			input = editor.getEditorInput();
			diagram = diagram.getParentSystemDiagram();
		}
		return input;
	}

	@Override
	public void doSaveAs() {
		ProfileInformationDialog dialog = new ProfileInformationDialog(
				getSite().getShell());
		if (getSystemDiagram().getSystemId() != null) {
			dialog.setSystemId(getSystemDiagram().getSystemId());
		}
		dialog.setSystemDiagram(getSystemDiagram());

		if (dialog.open() != IDialogConstants.OK_ID) {
			return;
		}
		
		String systemName = dialog.getInputSystemName();
		boolean isReplace = dialog.isReplace();

		getSystemDiagram().setSystemId(dialog.getSystemId());
		XMLGregorianCalendar calendar = new XMLGregorianCalendarImpl(new GregorianCalendar());
		calendar.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		getSystemDiagram().setCreationDate(calendar.toString());
		getSystemDiagram().setUpdateDate(calendar.toString());

		// TODO バージョンアップログへの対応

		// ダイアグラムのバリデーション
		if (!validateDiagram()) {
			return;
		}

		java.nio.file.Path resultDir = Paths.get(dialog.getInputPath() + FS + systemName);
		if(resultDir.toFile().exists()) {
			MessageBox message = new MessageBox(getSite().getShell(), SWT.ICON_QUESTION | SWT.YES | SWT.NO);
			message.setText(Messages.getString("AbstractSystemDiagramEditor.34"));
			message.setMessage(Messages.getString("AbstractSystemDiagramEditor.35"));
			if( message.open() != SWT.YES) return;
		} else {
			try {
				Files.createDirectories(resultDir);
			} catch (IOException e) {
				MessageDialog.openError(getSite().getShell(), Messages.getString("AbstractSystemDiagramEditor.21"), //$NON-NLS-1$
						Messages.getString("AbstractSystemDiagramEditor.25") + resultDir.toString()); //$NON-NLS-1$
			}
		}
		
		IPath result = new Path(dialog.getInputPath() + FS + systemName + FS + systemName + ".xml");
		final IFile newFile = createNewFile(result);
		if (newFile == null)
			return;

		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(
				getSite().getShell());
		try {
			progressMonitorDialog.run(false, false,
				new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						try {
							save(newFile, systemName, isReplace, monitor);
						} catch (CoreException e) {
							LOGGER.error("Fail to save. file=" + newFile, e);
							MessageDialog.openError(getSite().getShell(), Messages.getString("AbstractSystemDiagramEditor.21"), //$NON-NLS-1$
									Messages.getString("AbstractSystemDiagramEditor.22") + newFile.getName()); //$NON-NLS-1$
						}
					}
				});
		} catch (Exception e) {
			throw new RuntimeException(e); // SystemError
		}
	}

	/**
	 * ダイアグラムの整合性をチェックします。
	 *
	 * 拡張ポイント: jp.go.aist.rtm.toolscommon.validations
	 *
	 * @return 不整合の場合はfalse
	 */
	public boolean validateDiagram() {
		try {
			Validator.validate(systemDiagram.getRootDiagram());
		} catch (ValidateException e) {
			MessageDialog.openError(getSite().getShell(),
					MSG_TITLE_VALIDATION_ERROR, e.getMessage() + "\n"
							+ e.getDetail());
			return false;
		}
		return true;
	}

	private IFile createNewFilebySelection(IFile oldFile, int style) {
		final IPath newPath = getFilePathByDialog(oldFile, style);
		return createNewFile(newPath);
	}

	private IFile createNewFile(IPath newPath) {
		if (newPath == null) return null;

		if (newPath.toFile().exists() == false) {
			try {
				newPath.toFile().createNewFile();
			} catch (IOException e) {
				MessageDialog.openError(getSite().getShell(), Messages.getString("AbstractSystemDiagramEditor.21"), //$NON-NLS-1$
						Messages.getString("AbstractSystemDiagramEditor.24") + newPath.toOSString()); //$NON-NLS-1$
			}
		}

		if (newPath.toFile().exists()) {
			return getOutsideIFileLink(newPath);
		}
		return null;
	}

	private IPath getFilePathByDialog(IFile defaultFile, int style) {
		FileDialog dialog = new FileDialog(getSite().getShell(), style);

		if (defaultFile != null) {
			dialog.setFileName(defaultFile.toString());
		}
		dialog.setFilterExtensions(new String[] { "*." + FILE_EXTENTION }); //$NON-NLS-1$

		String pathString = dialog.open();

		if (pathString == null) {
			return null;
		}

		IPath result = new Path(pathString);
		if (result.getFileExtension() == null) {
			result = result.addFileExtension(FILE_EXTENTION);
		}

		return result;
	}

	protected void save(IFile file, String systemName, boolean isReplace, IProgressMonitor progressMonitor)
			throws CoreException {

		List<AbstractSystemDiagramEditor> editors = new ArrayList<AbstractSystemDiagramEditor>();
		editors.add(this);

		// 子エディタを取得
		for (Iterator iterator = systemDiagram.getComponents().iterator(); iterator
				.hasNext();) {
			Component compnent = (Component) iterator.next();
			if (compnent.isCompositeComponent()
					&& compnent.getChildSystemDiagram() != null) {
				AbstractSystemDiagramEditor editor = ComponentUtil.findEditor(compnent.getChildSystemDiagram());
				if (editor != null)
					editors.add(editor);
			}
		}
		// 親エディタを取得
		SystemDiagram parentSystemDiagram = systemDiagram.getParentSystemDiagram();
		while (parentSystemDiagram != null) {
			AbstractSystemDiagramEditor editor = ComponentUtil.findEditor(parentSystemDiagram);
			if (editor != null)
				editors.add(editor);

			parentSystemDiagram.setSystemId(systemDiagram.getSystemId());
			parentSystemDiagram.setCreationDate(systemDiagram.getCreationDate());
			parentSystemDiagram.setUpdateDate(systemDiagram.getUpdateDate());
			for (Iterator iterator = systemDiagram.getComponents()
					.iterator(); iterator.hasNext();) {
				Component component = (Component) iterator
						.next();
				Component localObject = getLocalObject(parentSystemDiagram,
						component);

				// Constraintを変更
				if (localObject != null) {
					localObject.getConstraint().setX(
							component.getConstraint().getX());
					localObject.getConstraint().setY(
							component.getConstraint().getY());
					localObject.getConstraint().setHeight(
							component.getConstraint().getHeight());
					localObject.getConstraint().setWidth(
							component.getConstraint().getWidth());
				}
			}

			parentSystemDiagram = parentSystemDiagram.getParentSystemDiagram();
		}

		if (progressMonitor == null) {
			progressMonitor = new NullProgressMonitor();
		}
		progressMonitor.beginTask("Saving" + file.getLocation().toOSString(), 6);

		try {
			// STEP1: リソースを作成
			progressMonitor.worked(1);

			Resource resource = null;
			ResourceSet resourceSet = new ResourceSetImpl();
			resource = resourceSet.createResource(URI.createFileURI(file
					.getLocation().toOSString()));

			// STEP2: ダイアグラムからRTSプロファイルオブジェクト生成
			progressMonitor.worked(2);

			RtsProfileHandler handler = new RtsProfileHandler();
			SystemDiagram diagram = systemDiagram.getRootDiagram();
			RtsProfileExt profile = handler.save(diagram);
			diagram.setProfile(profile);
			
			String targetFileName = resource.getURI().devicePath();
			if(getEditorId()==OfflineSystemDiagramEditor.OFFLINE_SYSTEM_DIAGRAM_EDITOR_ID) {
				File targetFile = new File(targetFileName);
				String rtcPath = targetFile.getParent() + File.separator + "RTCs";
				File targetRtcs = new File(rtcPath);
				if(targetRtcs.exists()==false) {
					targetRtcs.mkdir();
				}
				for(org.openrtp.namespaces.rts.version02.Component each : profile.getComponents()) {
					URI pathUri = URI.createURI(each.getPathUri());
					java.nio.file.Path srcFile = Paths.get(pathUri.device() + File.separator + pathUri.path());
					java.nio.file.Path dstFile = Paths.get(rtcPath + File.separator + each.getInstanceName() + ".xml");
					Files.copy(srcFile, dstFile, StandardCopyOption.REPLACE_EXISTING);
					URI newURI = URI.createFileURI("RTCs" + File.separator + each.getInstanceName() + ".xml");
					each.setPathUri(newURI.toString());
				 }
			}

			// STEP3: 拡張ポイント (RTSプロファイル保存前)
			progressMonitor.worked(3);

			ProfileSaver creator = new ProfileSaver();
			for (SaveProfileExtension.ErrorInfo info : creator.preSave(
					diagram, profile)) {
				if (info.isError()) {
					openError(DIALOG_TITLE_ERROR, info.getMessage());
					progressMonitor.done();
					return;
				} else {
					if (!openConfirm(DIALOG_TITLE_CONFIRM, info.getMessage())) {
						progressMonitor.done();
						return;
					}
				}
			}

			// STEP4: RTSプロファイルオブジェクトをファイルへ保存
			progressMonitor.worked(4);

			XmlHandler xmlHandler = new XmlHandler();
			xmlHandler.saveXmlRts(profile, URLDecoder.decode(targetFileName,
					"UTF-8"));
			file.refreshLocal(IResource.DEPTH_ZERO, null);
			for (AbstractSystemDiagramEditor editor : editors) {
				editor.changeFile(file);
			}
			
			saveScript(file, profile, systemName, isReplace);

			// STEP5: 拡張ポイント (RTSプロファイル保存後)
			progressMonitor.worked(5);

			for (SaveProfileExtension.ErrorInfo info : creator.postSave(
					diagram, file)) {
				if (info.isError()) {
					openError(DIALOG_TITLE_ERROR, info.getMessage());
					progressMonitor.done();
					return;
				} else {
					if (!openConfirm(DIALOG_TITLE_CONFIRM, info.getMessage())) {
						progressMonitor.done();
						return;
					}
				}
			}
			
			progressMonitor.worked(6);
			progressMonitor.done();

		} catch (FileNotFoundException e) {
			progressMonitor.done();
			IStatus status = new Status(IStatus.ERROR, RTSystemEditorPlugin.getDefault()
					.getClass().getName(), 0, "Error writing file.", e);
			throw new CoreException(status);

		} catch (Exception e) {
			progressMonitor.done();
			IStatus status = new Status(IStatus.ERROR, RTSystemEditorPlugin.getDefault()
					.getClass().getName(), 0, "Error writing file.", e);
			throw new CoreException(status);
		}
	}

	private void saveScript(IFile file, RtsProfileExt profile, String systemName, boolean isReplace) {
		java.nio.file.Path rawFile = Paths.get(file.getLocation().toOSString());
		java.nio.file.Path targetDir = rawFile.getParent();
		
		saveExitScript(profile, systemName, isReplace, targetDir);
		saveResurrectScript(systemName, targetDir);
		saveActivateScript(systemName, targetDir);
		saveRunScript(systemName, targetDir);
		saveDeactivateScript(systemName, targetDir);
		saveTeardownScript(systemName, targetDir);
	}

	private void saveTeardownScript(String systemName, java.nio.file.Path targetDir) {
		StringBuilder builder = new StringBuilder();
		builder.append("rtteardown ").append(systemName).append(".xml").append("\r\n");
		String teardownBat = targetDir.toString() + FS + "Teardown_" + systemName + ".bat";
		writeFile(teardownBat, builder.toString());

		StringBuilder builderSh = new StringBuilder();
		builderSh.append("#!/usr/bin/env bash​​");
		builderSh.append("\n").append("\n");
		builderSh.append("rtteardown ").append(systemName).append(".xml").append("\n");
		String teardownSh = targetDir.toString() + FS + "Teardown_" + systemName + ".sh";
		writeFile(teardownSh, builderSh.toString());
	}

	private void saveDeactivateScript(String systemName, java.nio.file.Path targetDir) {
		StringBuilder builder = new StringBuilder();
		builder.append("rtstop ").append(systemName).append(".xml").append("\r\n");
		String deactivateBat = targetDir.toString() + FS + "Deactivate_" + systemName + ".bat";
		writeFile(deactivateBat, builder.toString());

		StringBuilder builderSh = new StringBuilder();
		builderSh.append("#!/usr/bin/env bash​​");
		builderSh.append("\n").append("\n");
		builderSh.append("rtstop ").append(systemName).append(".xml").append("\n");
		String deactivateSh = targetDir.toString() + FS + "Deactivate_" + systemName + ".sh";
		writeFile(deactivateSh, builderSh.toString());
	}

	private void saveRunScript(String systemName, java.nio.file.Path targetDir) {
		StringBuilder builder = new StringBuilder();
		builder.append("cmd /c Resurrect_").append(systemName).append(".bat").append("\r\n");
		builder.append("cmd /c Activate_").append(systemName).append(".bat").append("\r\n");
		String runBat = targetDir.toString() + FS + "Run_" + systemName + ".bat";
		writeFile(runBat, builder.toString());
		
		StringBuilder builderSh = new StringBuilder();
		builderSh.append("#!/usr/bin/env bash​​");
		builderSh.append("\n").append("\n");
		builderSh.append("Resurrect_").append(systemName).append(".sh").append("\n");
		builderSh.append("Activate_").append(systemName).append(".sh").append("\n");
		String runSh = targetDir.toString() + FS + "Run_" + systemName + ".sh";
		writeFile(runSh, builderSh.toString());
	}

	private void saveActivateScript(String systemName, java.nio.file.Path targetDir) {
		StringBuilder builder = new StringBuilder();
		builder.append("rtstart ").append(systemName).append(".xml").append("\r\n");
		String activateBat = targetDir.toString() + FS + "Activate_" + systemName + ".bat";
		writeFile(activateBat, builder.toString());

		StringBuilder builderSh = new StringBuilder();
		builderSh.append("#!/usr/bin/env bash​​");
		builderSh.append("\n").append("\n");
		builderSh.append("rtstart ").append(systemName).append(".xml").append("\n");
		String activateSh = targetDir.toString() + FS + "Activate_" + systemName + ".sh";
		writeFile(activateSh, builderSh.toString());
	}

	private void saveResurrectScript(String systemName, java.nio.file.Path targetDir) {
		StringBuilder builder = new StringBuilder();
		builder.append("rtresurrect ").append(systemName).append(".xml").append("\r\n");
		String resurrectBat = targetDir.toString() + FS + "Resurrect_" + systemName + ".bat";
		writeFile(resurrectBat, builder.toString());
		
		StringBuilder builderSh = new StringBuilder();
		builderSh.append("#!/usr/bin/env bash​​");
		builderSh.append("\n").append("\n");
		builderSh.append("rtresurrect ").append(systemName).append(".xml").append("\n");
		String resurrectSh = targetDir.toString() + FS + "Resurrect_" + systemName + ".sh";
		writeFile(resurrectSh, builderSh.toString());
	}

	private void saveExitScript(RtsProfileExt profile, String systemName, boolean isReplace, java.nio.file.Path targetDir) {
		StringBuilder builder = new StringBuilder();
		for(org.openrtp.namespaces.rts.version02.Component each : profile.getComponents()) {
			builder.append("rtexit ");
			if(isReplace) {
				builder.append(replaceHostName(each.getPathUri()));
			} else {
				builder.append(each.getPathUri());
			}
			builder.append("\r\n");
		}
		String exitBat = targetDir.toString() + FS + "Exit_" + systemName + ".bat";
		writeFile(exitBat, builder.toString());

		StringBuilder builderSh = new StringBuilder();
		builderSh.append("#!/usr/bin/env bash​​");
		builderSh.append("\n").append("\n");
		builderSh.append(builder);
		String exitSh = targetDir.toString() + FS + "Exit_" + systemName + ".sh";
		writeFile(exitSh, builderSh.toString().replace("\r\n", "\n"));
	}
	
	private void writeFile(String fileName, String contents) {
		try( FileOutputStream fos = new FileOutputStream(fileName) ) {
			OutputStreamWriter osw = new OutputStreamWriter( fos , "UTF-8");
			BufferedWriter fp = new BufferedWriter( osw );
			fp.write (contents);
			fp.flush();
		} catch (Exception e) {
			LOGGER.error("Fail to create file", e);
		}
	}
	
	private String replaceHostName(String source) {
		StringBuilder builder = new StringBuilder();
		
		String[] elems = source.split("/");
		for(String each : elems) {
			if(0 < builder.length()) {
				builder.append("/");
			}
			if(each.endsWith(".host_cxt")) {
				builder.append("%COMPUTERNAME%.host_cxt");
			} else {
				builder.append(each);
			}
		}
		
		return builder.toString();
	}

	private Component getLocalObject(SystemDiagram systemDiagram,
			Component component) {
		if (component instanceof CorbaComponent) {
			org.omg.CORBA.Object obj = ((CorbaComponent)component).getCorbaObject();
			if (obj != null) {
				return (Component) SynchronizationSupport
				.findLocalObjectByRemoteObject(
						new Object[] { obj },
						systemDiagram);
			}
		}
		return findComponentByPathId(
				component, systemDiagram);
	}

	/**
	 * componentをdiagramから探す（PathIdで探す)
	 * @param component
	 * @param diagram
	 * @return
	 */
	private Component findComponentByPathId(
			Component component, SystemDiagram diagram) {
		if (component == null || diagram == null) {
			return null;
		}
		for (Component tempComponent : diagram.getRegisteredComponents()) {
			if (tempComponent.getPathId() != null
					&& tempComponent.getPathId().equals(component.getPathId())) {
				return tempComponent;
			}
		}
		return null;
	}

	public void changeFile(IFile file) {
		if (this.systemDiagram.getParentSystemDiagram() != null) {
			title = systemDiagram.getCompositeComponent().getInstanceNameL();
		} else {
			setInput(new FileEditorInput(file));
			title = file.getLocation().lastSegment();
		}
		getCommandStack().markSaveLocation();
		firePropertyChange(IEditorPart.PROP_TITLE);
	}

	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	// ConfigSetが変更されたので、強制的にdirtyとする
	public void setDirty() {
		Command cannnotUndoCommand = new Command(){
			@Override
			public boolean canUndo() {
				return false;
			}};
		getCommandStack().execute(cannnotUndoCommand);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		IEditorInput newInput;
		try {
			newInput = load(input, site);
		} catch (Throwable t) {
			// 起動時にファイルオープンエラーが発生した時はエディタの中身を空にする 2009.11.06
			newInput = load(new NullEditorInput(), site);
		}
		super.init(site, newInput);
	}

	protected abstract IEditorInput load(IEditorInput input,
			final IEditorSite site)
			throws PartInitException;

	private SimpleDateFormat formater = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss_SSS");

	protected IEditorInput getTargetInput(IEditorInput input, String windowName) {
		if (input instanceof NullEditorInput) {
			setSystemDiagram(ComponentFactory.eINSTANCE.createSystemDiagram());
			diagramName = windowName;
			setInput(input);
			return input;
		} else  {
			FileEditorInput editorInput = createEditorInput(input);
			diagramName = editorInput.getPath().lastSegment();
			setInput(editorInput);
			return editorInput;
		}
	}

	protected FileEditorInput createEditorInput(IEditorInput input) {
		if (input instanceof FileEditorInput) return (FileEditorInput) input;

		IPath path = ((IPathEditorInput) input).getPath();
		IFile file = getOutsideIFileLink(path);
		return new FileEditorInput(file);
	}

	public void postLoad() {
		GraphicalViewer graphicalViewer2 = getGraphicalViewer();
		if (graphicalViewer2 != null) { // 初期ロードの場合には存在しない。別途後でロードする
			graphicalViewer2.setContents(getSystemDiagram());
		}

		getCommandStack().markSaveLocation();
		firePropertyChange(IEditorPart.PROP_TITLE);
	}

	protected IFile getOutsideIFileLink(IPath path) {
		IProject project = ensureProjectOpen();

		IFile fileLink = getFileLinkByRawLocation(path, project);

		if (fileLink != null) return fileLink;


		fileLink = project.getFile(formater.format(new Date()) + "__" //$NON-NLS-1$
				+ path.lastSegment());
		try {
			project.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e1) {
			throw new RuntimeException(e1); // systemError
		}
		if (fileLink.exists() == false) {
			try {
				fileLink.createLink(path, IResource.NONE, null);
			} catch (CoreException e) {
				throw new RuntimeException(e); // systemError
			}
		}

		return fileLink;
	}

	private IFile getFileLinkByRawLocation(IPath path, IProject project) {
		try {
			for (IResource r : project.members()) {
				if (r.getType() == IResource.FILE && r.isLinked()
						&& path.equals(r.getRawLocation())) {
					return (IFile) r;
				}
			}
		} catch (CoreException e2) {
			// void
		}
		return null;
	}

	private IProject ensureProjectOpen() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		IProject project = ws.getRoot().getProject(RTCLINK_PROJECT_NAME);
		if (!project.exists())
			try {
				project.create(null);
			} catch (CoreException e) {
				throw new RuntimeException(e); // systemError
			}
		if (!project.isOpen())
			try {
				project.open(null);
			} catch (CoreException e) {
				throw new RuntimeException(e); // systemError
			}
		return project;
	}

	@Override
	public void dispose() {
		try {
			super.dispose();
		} catch (NullPointerException e) {
			// do nothing
		}

		if (getSystemDiagramPropertyChangeListener() != null) {
			getSystemDiagramPropertyChangeListener().dispose();
		}

		// 複合RTCエディタが開かれていたら閉じる
		if (getSystemDiagram() == null)
			return;
		if (getSystemDiagram().getComponents() != null) {
			for (Component ac : getSystemDiagram().getComponents()) {
				ComponentUtil.closeCompositeComponent(ac);
			}
		}
		getSystemDiagram().clearComponents();
		getSystemDiagram().dispose();
		systemDiagram = null;
	}

	@Override
	public Object getAdapter(Class type) {
		if (type.equals(IPropertySheetPage.class)) {
			return new RtcPropertySheetPage();
		}

		return super.getAdapter(type);
	}

	@Override
	public String getTitle() {
		return (title == null) ? diagramName : title;
	}

	public void open() {
		boolean save = false;
		if (isDirty()) {
			save = MessageDialog.openQuestion(getSite().getShell(), "", //$NON-NLS-1$
					Messages.getString("AbstractSystemDiagramEditor.32")); //$NON-NLS-1$
		}
		if (save) {
			doSave(null);
		}
		title = null;
		IFile createNewFile = createNewFilebySelection(null, SWT.OPEN);
		if (createNewFile != null) {
			try {
				load(new FileEditorInput(createNewFile), getEditorSite());
			} catch (PartInitException e) {
				LOGGER.error("Fail to load file. file=" + createNewFile, e);
				if (e.getStatus().getException() != null)
					MessageDialog.openError(getSite().getShell(), "",
							e.getStatus().getException().getMessage());
			}
		}
	}

	/**
	 * SystemDiagramを取得する
	 *
	 * @return
	 */
	public SystemDiagram getSystemDiagram() {
		return systemDiagram;
	}

	/**
	 *
	 */
	protected final class SystemDiagramPropertyChangeListener implements
			PropertyChangeListener {
		private IWorkbenchPage page;

		private SystemDiagramPropertyChangeListener(IWorkbenchPage page) {
			super();
			this.page = page;
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals("SYSTEM_DIAGRAM_COMPONENTS")) { //$NON-NLS-1$
				final PropertyChangeEvent changeEvent = evt;
				if (evt.getOldValue() instanceof Component
						&& ((Component) evt.getOldValue())
								.isCompositeComponent()) {
					page.getWorkbenchWindow().getShell().getDisplay()
							.asyncExec(new Runnable() {
								@Override
								public void run() {
									List<Component> components = new ArrayList<Component>();
									components
											.add((Component) changeEvent
													.getOldValue());
									components
											.addAll(((Component) changeEvent
													.getOldValue())
													.getAllComponents());
									for (Component tmpComponent : components) {
										SystemDiagram childDiagram = tmpComponent.getChildSystemDiagram();
										if (childDiagram != null) {
											AbstractSystemDiagramEditor editor = ComponentUtil.findEditor(childDiagram);
											if (editor != null)	page.closeEditor(editor, false);
											tmpComponent.setChildSystemDiagram(null);
										}
									}
								}
							});
				}
			}
		}

		public void dispose() {
			if (systemDiagram != null) {
				systemDiagram.removePropertyChangeListener(this);
			}
		}
	}

	abstract public boolean isOnline();

	public SystemDiagramPropertyChangeListener getSystemDiagramPropertyChangeListener() {
		return systemDiagramPropertyChangeListener;
	}

	public void setSystemDiagram(SystemDiagram systemDiagram) {
		this.systemDiagram = systemDiagram;
	}

	public void deselectAll() {
		getGraphicalViewer().deselectAll();
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		if (getSystemDiagram().getParentSystemDiagram() != null) {
			// 複合RTCのエディタの場合は保存しない
			return false;
		}
		return super.isSaveOnCloseNeeded();
	}

	public SystemDiagramEditPart getSystemDiagramEditPart() {
		EditPart part = getGraphicalViewer().getContents();
		if (part == null || !(part instanceof SystemDiagramEditPart)) {
			return null;
		}
		return (SystemDiagramEditPart) part;
	}

	public EditPart findEditPart(Object model) {
		SystemDiagramEditPart part = getSystemDiagramEditPart();
		if (part == null) {
			return null;
		}
		for (Object o : part.getChildren()) {
			EditPart child = (EditPart) o;
			if (model.equals(child.getModel())) {
				return child;
			}
		}
		return null;
	}

	/**
	 * エディタ内のコンポーネントを再描画する
	 */
	public void refresh() {
		SystemDiagramEditPart part = getSystemDiagramEditPart();
		if (part == null) {
			return;
		}
		LOGGER.debug("refresh: part=<{}>", part);
		part.refreshSystemDiagram();
		AbstractSystemDiagramEditor parent = ComponentUtil.findEditor(getSystemDiagram().getParentSystemDiagram());
		if (parent != null) {
			parent.refresh();
		}
	}

	public void openError(String title, String message) {
		MessageDialog.openError(getSite().getShell(), title, message);
	}

	public boolean openConfirm(String title, String message) {
		return MessageDialog.openConfirm(getSite().getShell(), title, message);
	}

//	private void debugPrint(EditPart part, int size) {
//		Object model = part.getModel();
//		if (model instanceof Port) debugPrint((Port)model);
//		if (model instanceof Component) debugPrint((Component)model, size, part);
//	}
//	private void debugPrint(Component component, int size, EditPart part) {
//		if (component.getInstanceNameL().equals("test3"))  {
//			System.out.println(component.getPorts().size() + ":" + size);
//			for (Object obj :part.getChildren()) {
//				System.out.println(obj);
//			}
//		}
//	}
//
//	private static void debugPrint(Port port) {
//		Component comp = (Component) port.eContainer();
//		if (comp.getInstanceNameL().equals("test3"))
//			System.out.println(port.getNameL() + ":" + comp.getPorts().size());
//	}

}
