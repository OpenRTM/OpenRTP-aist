package jp.go.aist.rtm.rtcbuilder.ros.ui.preference;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPreferencePage;

import jp.go.aist.rtm.rtcbuilder.nl.Messages;
import jp.go.aist.rtm.rtcbuilder.ros.IRtcBuilderConstantsROS;
import jp.go.aist.rtm.rtcbuilder.ui.preference.AbstractPreferencePage;
import jp.go.aist.rtm.rtcbuilder.ui.preference.DocumentPreferenceManager;

public class ROSBuilderPreferencePage extends AbstractPreferencePage implements
		IWorkbenchPreferencePage {
	
	private ArrayList<String> documentArray = new ArrayList<String>();

	private final String ON = "ON";
	private final String OFF = "OFF";
	
	private Group group[] = new Group[IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM];
	private GridLayout layout[] = new GridLayout[IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM];
	private Label label[] = new Label[IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM];
	private Button btnOn[] = new Button[IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM];
	private Button btnOff[] = new Button[IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM];
	
	private Text textMaintainerName;
	private Text textMaintainerAddress;

	public ROSBuilderPreferencePage(){
	}
	public ROSBuilderPreferencePage(String title) {
		super(title);
	}
	public ROSBuilderPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	@Override
	protected Control createContents(Composite parent) {
		GridData gd;
		Composite composite = new Composite(parent,SWT.NULL);
		composite.setLayout(new GridLayout(1,true));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		composite.setLayoutData(gd);
		
		// TextArea
		Group groupText = createGroup(composite, Messages.getString("IMC.ROS_BASIC_NODE_TITLE"));
		GridLayout layoutText = new GridLayout(2,false);
		groupText.setLayout(layoutText);
		
		createLabel(Messages.getString("IMC.ROS_BASIC_LBL_MAINTAINER"), groupText);
		textMaintainerName = createText(groupText);
		createLabel(Messages.getString("IMC.ROS_BASIC_LBL_MAINTAINER_EMAIL"), groupText);
		textMaintainerAddress = createText(groupText);
		
		textMaintainerName.setText(ROSPreferenceManager.getMaintainerNameValue());
		textMaintainerAddress.setText(ROSPreferenceManager.getMaintainerAddressValue());
		
		// RadioButtonArea
		Group group = createGroup(composite, Messages.getString("IMC.ROS_LIFECYCLE_SECTION"));
		GridLayout layout = new GridLayout(2,false);
		group.setLayout(layout);
		
		createRadioArea(group, 3);
		createRadioArea(group, 4);
		createRadioArea(group, 5);
		
		documentArray = new ArrayList<String>();
		documentArray = ROSPreferenceManager.getDocumentValue();
		setButton(documentArray);
		
		return composite;
	}
	private GridLayout createLayout(Group baseGroup) {
		GridLayout layout = new GridLayout(3,false);
		baseGroup.setLayout(layout);
		return layout;
	}
	private Label createLabel(String labelString,Group baseGroup){
		Label label = new Label(baseGroup,SWT.NONE);
		label.setText(labelString);
		GridData gd = new GridData();
		gd.widthHint = 120;
		label.setLayoutData(gd);
		return label;
	}
	private Button createButton(String textString, Group baseGroup) {
		Button button = new Button(baseGroup,SWT.RADIO);
		button.setText(textString);
		return button;
	}
	private Text createText(Group baseGroup) {
		Text text = new Text(baseGroup,SWT.MULTI | SWT.BORDER | SWT.WRAP);
	    GridData gd = new GridData();
	    gd.horizontalAlignment = GridData.FILL;
	    gd.verticalAlignment = GridData.FILL;
	    gd.grabExcessHorizontalSpace = true;
	    gd.grabExcessVerticalSpace = true;
	    gd.heightHint = 40;
	    text.setLayoutData(gd);
		return text;
	}
	
	private void createRadioArea(Group parent,int intIdx) {
		int grpIndex = intIdx - 3;
		group[grpIndex] = createGroup(parent, "");
		layout[grpIndex] = createLayout(group[grpIndex]);
		label[grpIndex] = createLabel(IRtcBuilderConstantsROS.ACTION_TYPE_ITEMS[intIdx],group[grpIndex]);
		btnOn[grpIndex] = createButton(ON, group[grpIndex]);
		btnOff[grpIndex] = createButton(OFF, group[grpIndex]);
		btnOff[grpIndex].setSelection(true);
	}

	@Override
	public boolean performOk() {
		if( !validate() )  return false;
		
		documentArray = new ArrayList<String>();
		
		String setting = null;
		for (int intIdx = 0; intIdx < IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM; intIdx++) {
			if( btnOn[intIdx].getSelection() ) {
				setting = "true";
			} else {
				setting = "false";
			}
			documentArray.add(setting);
		}
		ROSPreferenceManager.getInstance().setDocumentValue(documentArray);
		ROSPreferenceManager.getInstance().setMaintainerNameValue(textMaintainerName.getText());
		ROSPreferenceManager.getInstance().setMaintainerAddressValue(textMaintainerAddress.getText());

		return super.performOk();
	}

	@Override
	protected void performDefaults() {
		ArrayList<String> documentArray = ROSPreferenceManager.getDefaultDocumentValue();
		setButton(documentArray);
		
		textMaintainerName.setText(ROSPreferenceManager.DEFAULT_MAINTAINER_NAME);
		textMaintainerAddress.setText(ROSPreferenceManager.DEFAULT_MAINTAINER_ADDRESS);
		
		super.performDefaults();
	}

	@Override
	protected boolean validate() {
		return true;
	}
	
	private void setButton(ArrayList<String> param) {
		for (int intIdx=0; intIdx < IRtcBuilderConstantsROS.ACTIVITY_CAN_EDIT_NUM; intIdx++) {
			if (param.get(intIdx).toUpperCase().equals("TRUE") ) {
				btnOn[intIdx].setSelection(true);
				btnOff[intIdx].setSelection(false);
			} else {
				btnOn[intIdx].setSelection(false);
				btnOff[intIdx].setSelection(true);
			}
		}
	}
}
