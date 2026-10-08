package jp.go.aist.rtm.rtcbuilder.ros.ui;

import static jp.go.aist.rtm.toolscommon.profiles.util.XmlHandler.createXMLGregorianCalendar;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.go.aist.rtm.rtcbuilder.IRtcBuilderConstants;
import jp.go.aist.rtm.rtcbuilder.generator.ProfileHandler;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ros.ProfileHandlerROS;
import jp.go.aist.rtm.rtcbuilder.ros.ui.editors.ROSBuilderEditor;
import jp.go.aist.rtm.rtcbuilder.ui.editors.RtcBuilderEditor;

public class NewWizard extends Wizard implements INewWizard, IExecutableExtension {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(NewWizard.class);

	private WizardNewProjectCreationPage newProjectPage;
	private IConfigurationElement configElement;

	public NewWizard() {
		super();
		setWindowTitle("ROSBuilder Project");
	}

	public boolean performFinish() {
		IProject projectHandle = newProjectPage.getProjectHandle();
		IFile rtcxml = null;
		try {
			if( !newProjectPage.useDefaults() ) {
				IPath newPath = newProjectPage.getLocationPath();
				IWorkspace workspace = projectHandle.getWorkspace();

		        final IProjectDescription description =
		        			workspace.newProjectDescription(projectHandle.getName());
		        description.setLocation(newPath);
				projectHandle.create(description, null);
			} else {
				projectHandle.create(null);
			}
			projectHandle.open(null);
			//
			ProfileHandlerROS handler = new ProfileHandlerROS();
			String xmlFile = handler.createInitialROSXml();
			//
			rtcxml = projectHandle.getFile(IRtcBuilderConstantsROS.DEFAULT_ROS_XML);
			rtcxml.create(new ByteArrayInputStream(xmlFile.getBytes("UTF-8")), false, null);
		} catch (CoreException ex) {
			System.out.println(ex);
			return false;
		} catch (UnsupportedEncodingException e) {
			System.out.println(e);
			return false;
		}
		// パースペクティブを切り替え
		BasicNewProjectResourceWizard.updatePerspective(configElement);

		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		try {
			window.getActivePage().openEditor(new FileEditorInput(rtcxml),
					ROSBuilderEditor.ROS_BUILDER_EDITOR_ID);
		} catch (PartInitException e) {
			LOGGER.error("Fail to open editor", e);
		}

		return true;
	}

    public void setInitializationData(
		IConfigurationElement cfig, String propertyName, Object data) {
        configElement = cfig;
    }

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		//
	}

	public void addPages() {
		newProjectPage = new WizardNewProjectCreationPage("ProjectCreation");
		addPage(newProjectPage);
	}
}
