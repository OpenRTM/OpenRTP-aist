package jp.go.aist.rtm.rtcbuilder.ui.dialog;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ui.parts.SingleLabelItem;
import jp.go.aist.rtm.rtcbuilder.ui.parts.SingleLabelProvider;

public class RestoreDialog extends Dialog {
	private String targetProject = "";
	private String targetFile = "";
	private String selected = "";
	private TableViewer timeStampViewer;
    private List<SingleLabelItem> timeStampList = new ArrayList<SingleLabelItem>();
    private List<String> timeStampStrList = new ArrayList<String>();
	
    public void setTargetProject(String target) {
    	this.targetProject = target;
    }
    public void setTargetFile(String target) {
    	this.targetFile = target;
    }
	public String getTimeStamp() {
		return selected;
	}

	public RestoreDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.CENTER | SWT.RESIZE);
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Select Restore Target");
	}
	
	@Override
	protected Point getInitialSize() {
		if(timeStampList.size() == 0) {
			getButton(IDialogConstants.OK_ID).setEnabled(false);	
		}
		return new Point(300, 400);
	}
	
	@Override
	/**
	 * {@inheritDoc}
	 */
	protected Control createDialogArea(Composite parent) {
		GridLayout gridLayout = new GridLayout();

		Composite mainComposite = (Composite) super.createDialogArea(parent);
		mainComposite.setLayout(gridLayout);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label label = new Label(mainComposite, SWT.NONE);
		label.setText(Messages.getString("IMC.RESTORE_LABEL")); //$NON-NLS-1$
		GridData labelLayloutData = new GridData(
				GridData.HORIZONTAL_ALIGN_BEGINNING);
		label.setLayoutData(labelLayloutData);
		labelLayloutData.heightHint = 20;
		
		timeStampViewer = new TableViewer(mainComposite, SWT.BORDER | SWT.FULL_SELECTION);
		
		Table table = timeStampViewer.getTable();
        table.setLinesVisible(true);
        GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		table.setLayoutData(gd);
		
		timeStampViewer.setContentProvider(new ArrayContentProvider());
		timeStampViewer.setLabelProvider(new SingleLabelProvider());
		TableColumn nameColumn = new TableColumn(timeStampViewer.getTable(), SWT.NONE);
		nameColumn.setText("TimeStamp"); //$NON-NLS-1$
		nameColumn.setWidth(260);

		timeStampList = checkTimeStamp();
		timeStampViewer.setInput(timeStampList);
		timeStampViewer.refresh();
		
		return mainComposite;
	}
	
	private List<SingleLabelItem> checkTimeStamp() {
		List<SingleLabelItem> result = new ArrayList<SingleLabelItem>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		DateTimeFormatter str_format = DateTimeFormatter.ofPattern(Messages.getString("IMC.RESTORE_TIMESTAMP_FORMATL"));
		
		IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = workspaceHandle.getProject(targetProject);
		File dir = new File(project.getLocation().toOSString());
		File[] files = dir.listFiles();
		for(File target : files) {
			if(target.getName().startsWith(this.targetFile)) {
				String name = target.getName();
				if(name.length() < 14) continue;
				String strTimestamp = name.substring(7);
				timeStampStrList.add(strTimestamp);
				LocalDateTime dateTime = LocalDateTime.parse(strTimestamp, formatter);
				String dispTimestamp = dateTime.format(str_format);
				result.add(new SingleLabelItem(dispTimestamp));
			}
		}
		return result;
	}
	
	@Override
	protected void okPressed() {
		int sel = timeStampViewer.getTable().getSelectionIndex();
		this.selected = timeStampStrList.get(sel);
		super.okPressed();
	}

}
