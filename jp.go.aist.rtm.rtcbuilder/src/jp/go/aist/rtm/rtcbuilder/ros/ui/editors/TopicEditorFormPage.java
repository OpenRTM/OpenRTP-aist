package jp.go.aist.rtm.rtcbuilder.ros.ui.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.swt.events.SelectionListener;
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
import jp.go.aist.rtm.rtcbuilder.ros.param.ROSParam;
import jp.go.aist.rtm.rtcbuilder.ros.param.TopicParam;
import jp.go.aist.rtm.rtcbuilder.ui.editors.AbstractEditorFormPage;
import jp.go.aist.rtm.rtcbuilder.ui.editors.IMessageConstants;
import jp.go.aist.rtm.rtcbuilder.util.StringUtil;

/**
 * Topicページ
 */
public class TopicEditorFormPage extends AbstractEditorFormPage {

	private TableViewer subscribeTableViewer;
	private TableViewer publishTableViewer;
	//
	private Text topicNameText;
	private Combo messageTypeCombo;
	private Text typePackageText;
	private Combo reliabilityCombo;
	private Combo historyCombo;
	private Text depthText;
	private Text variableNameText;
	
	private Text descriptionText;
	private Text typeText;
	private Text semanticsText;
	private Text unitText;
	private Text occurrenceText;
	//
	private TopicParam preSelection;
	private TopicParam selectParam;
	//
    private List<String> defaultList = Arrays.asList(
    	    "action_msgs/msg/GoalInfo", "action_msgs/msg/GoalStatus",
    	    "action_msgs/msg/GoalStatusArray", "actuator_msgs/msg/Actuators",
    	    "actuator_msgs/msg/ActuatorsAngularPosition", "actuator_msgs/msg/ActuatorsAngularVelocity",
    	    "actuator_msgs/msg/ActuatorsLinearPosition", "actuator_msgs/msg/ActuatorsLinearVelocity",
    	    "actuator_msgs/msg/ActuatorsNormalized", "actuator_msgs/msg/ActuatorsPosition",
    	    "actuator_msgs/msg/ActuatorsVelocity", "builtin_interfaces/msg/Duration",
    	    "builtin_interfaces/msg/Time", "diagnostic_msgs/msg/DiagnosticArray",
    	    "diagnostic_msgs/msg/DiagnosticStatus", "diagnostic_msgs/msg/KeyValue",
    	    "geometry_msgs/msg/Accel", "geometry_msgs/msg/AccelStamped",
    	    "geometry_msgs/msg/AccelWithCovariance", "geometry_msgs/msg/AccelWithCovarianceStamped",
    	    "geometry_msgs/msg/Inertia", "geometry_msgs/msg/InertiaStamped",
    	    "geometry_msgs/msg/Point", "geometry_msgs/msg/Point32",
    	    "geometry_msgs/msg/PointStamped", "geometry_msgs/msg/Polygon",
    	    "geometry_msgs/msg/PolygonInstance", "geometry_msgs/msg/PolygonInstanceStamped",
    	    "geometry_msgs/msg/PolygonStamped", "geometry_msgs/msg/Pose",
    	    "geometry_msgs/msg/Pose2D", "geometry_msgs/msg/PoseArray",
    	    "geometry_msgs/msg/PoseStamped", "geometry_msgs/msg/PoseWithCovariance",
    	    "geometry_msgs/msg/PoseWithCovarianceStamped", "geometry_msgs/msg/Quaternion",
    	    "geometry_msgs/msg/QuaternionStamped", "geometry_msgs/msg/Transform",
    	    "geometry_msgs/msg/TransformStamped", "geometry_msgs/msg/Twist",
    	    "geometry_msgs/msg/TwistStamped", "geometry_msgs/msg/TwistWithCovariance",
    	    "geometry_msgs/msg/TwistWithCovarianceStamped", "geometry_msgs/msg/Vector3",
    	    "geometry_msgs/msg/Vector3Stamped", "geometry_msgs/msg/VelocityStamped",
    	    "geometry_msgs/msg/Wrench", "geometry_msgs/msg/WrenchStamped",
    	    "gps_msgs/msg/GPSFix", "gps_msgs/msg/GPSStatus",
    	    "lifecycle_msgs/msg/State", "lifecycle_msgs/msg/Transition",
    	    "lifecycle_msgs/msg/TransitionDescription", "lifecycle_msgs/msg/TransitionEvent",
    	    "map_msgs/msg/OccupancyGridUpdate", "map_msgs/msg/PointCloud2Update",
    	    "map_msgs/msg/ProjectedMap", "map_msgs/msg/ProjectedMapInfo",
    	    "nav_msgs/msg/Goals", "nav_msgs/msg/GridCells",
    	    "nav_msgs/msg/MapMetaData", "nav_msgs/msg/OccupancyGrid",
    	    "nav_msgs/msg/Odometry", "nav_msgs/msg/Path",
    	    "nav_msgs/msg/Trajectory", "nav_msgs/msg/TrajectoryPoint",
    	    "pcl_msgs/msg/ModelCoefficients", "pcl_msgs/msg/PointIndices",
    	    "pcl_msgs/msg/PolygonMesh", "pcl_msgs/msg/Vertices",
    	    "rcl_interfaces/msg/FloatingPointRange", "rcl_interfaces/msg/IntegerRange",
    	    "rcl_interfaces/msg/ListParametersResult", "rcl_interfaces/msg/Log",
    	    "rcl_interfaces/msg/LoggerLevel", "rcl_interfaces/msg/Parameter",
    	    "rcl_interfaces/msg/ParameterDescriptor", "rcl_interfaces/msg/ParameterEvent",
    	    "rcl_interfaces/msg/ParameterEventDescriptors", "rcl_interfaces/msg/ParameterType",
    	    "rcl_interfaces/msg/ParameterValue", "rcl_interfaces/msg/SetLoggerLevelsResult",
    	    "rcl_interfaces/msg/SetParametersResult", "rosgraph_msgs/msg/Clock",
    	    "sensor_msgs/msg/BatteryState", "sensor_msgs/msg/CameraInfo",
    	    "sensor_msgs/msg/ChannelFloat32" , "sensor_msgs/msg/CompressedImage",
    	    "sensor_msgs/msg/FluidPressure", "sensor_msgs/msg/Illuminance",
    	    "sensor_msgs/msg/Image", "sensor_msgs/msg/Imu",
    	    "sensor_msgs/msg/JointState", "sensor_msgs/msg/Joy",
    	    "sensor_msgs/msg/JoyFeedback", "sensor_msgs/msg/JoyFeedbackArray",
    	    "sensor_msgs/msg/LaserEcho", "sensor_msgs/msg/LaserScan",
    	    "sensor_msgs/msg/MagneticField", "sensor_msgs/msg/MultiDOFJointState",
    	    "sensor_msgs/msg/MultiEchoLaserScan", "sensor_msgs/msg/NavSatFix",
    	    "sensor_msgs/msg/NavSatStatus", "sensor_msgs/msg/PointCloud",
    	    "sensor_msgs/msg/PointCloud2", "sensor_msgs/msg/PointField",
    	    "sensor_msgs/msg/Range", "sensor_msgs/msg/RegionOfInterest",
    	    "sensor_msgs/msg/RelativeHumidity", "sensor_msgs/msg/Temperature",
    	    "sensor_msgs/msg/TimeReference", "shape_msgs/msg/Mesh",
    	    "shape_msgs/msg/MeshTriangle", "shape_msgs/msg/Plane",
    	    "shape_msgs/msg/SolidPrimitive", "statistics_msgs/msg/MetricsMessage",
    	    "statistics_msgs/msg/StatisticDataPoint", "statistics_msgs/msg/StatisticDataType",
    	    "std_msgs/msg/Bool", "std_msgs/msg/Byte",
    	    "std_msgs/msg/ByteMultiArray", "std_msgs/msg/Char",
    	    "std_msgs/msg/ColorRGBA", "std_msgs/msg/Empty",
    	    "std_msgs/msg/Float32", "std_msgs/msg/Float32MultiArray",
    	    "std_msgs/msg/Float64", "std_msgs/msg/Float64MultiArray",
    	    "std_msgs/msg/Header", "std_msgs/msg/Int16",
    	    "std_msgs/msg/Int16MultiArray", "std_msgs/msg/Int32",
    	    "std_msgs/msg/Int32MultiArray", "std_msgs/msg/Int64",
    	    "std_msgs/msg/Int64MultiArray", "std_msgs/msg/Int8",
    	    "std_msgs/msg/Int8MultiArray", "std_msgs/msg/MultiArrayDimension",
    	    "std_msgs/msg/MultiArrayLayout", "std_msgs/msg/String",
    	    "std_msgs/msg/UInt16", "std_msgs/msg/UInt16MultiArray",
    	    "std_msgs/msg/UInt32", "std_msgs/msg/UInt32MultiArray",
    	    "std_msgs/msg/UInt64", "std_msgs/msg/UInt64MultiArray",
    	    "std_msgs/msg/UInt8", "std_msgs/msg/UInt8MultiArray",
    	    "stereo_msgs/msg/DisparityImage", "tf2_msgs/msg/TF2Error",
    	    "tf2_msgs/msg/TFMessage", "trajectory_msgs/msg/JointTrajectory",
    	    "trajectory_msgs/msg/JointTrajectoryPoint", "trajectory_msgs/msg/MultiDOFJointTrajectory",
    	    "trajectory_msgs/msg/MultiDOFJointTrajectoryPoint", "unique_identifier_msgs/msg/UUID",
    	    "vision_msgs/msg/BoundingBox2D", "vision_msgs/msg/BoundingBox2DArray",
    	    "vision_msgs/msg/BoundingBox3D", "vision_msgs/msg/BoundingBox3DArray",
    	    "vision_msgs/msg/Classification", "vision_msgs/msg/Detection2D",
    	    "vision_msgs/msg/Detection2DArray", "vision_msgs/msg/Detection3D",
    	    "vision_msgs/msg/Detection3DArray", "vision_msgs/msg/LabelInfo",
    	    "vision_msgs/msg/ObjectHypothesis", "vision_msgs/msg/ObjectHypothesisWithPose",
    	    "vision_msgs/msg/Point2D", "vision_msgs/msg/Pose2D",
    	    "vision_msgs/msg/VisionClass", "vision_msgs/msg/VisionInfo",
    	    "visualization_msgs/msg/ImageMarker", "visualization_msgs/msg/InteractiveMarker",
    	    "visualization_msgs/msg/InteractiveMarkerControl", "visualization_msgs/msg/InteractiveMarkerFeedback",
    	    "visualization_msgs/msg/InteractiveMarkerInit", "visualization_msgs/msg/InteractiveMarkerPose",
    	    "visualization_msgs/msg/InteractiveMarkerUpdate", "visualization_msgs/msg/Marker",
    	    "visualization_msgs/msg/MarkerArray", "visualization_msgs/msg/MenuEntry",
    	    "visualization_msgs/msg/MeshFile", "visualization_msgs/msg/UVCoordinate",
    	    "action_msgs/srv/CancelGoal", "diagnostic_msgs/srv/AddDiagnostics",
    	    "diagnostic_msgs/srv/SelfTest", "example_interfaces/srv/AddTwoInts",
    	    "lifecycle_msgs/srv/ChangeState", "lifecycle_msgs/srv/GetAvailableStates",
    	    "lifecycle_msgs/srv/GetAvailableTransitions", "lifecycle_msgs/srv/GetState",
    	    "map_msgs/srv/GetMapROI", "map_msgs/srv/GetPointMap",
    	    "map_msgs/srv/GetPointMapROI", "map_msgs/srv/ProjectedMapsInfo",
    	    "map_msgs/srv/SaveMap", "map_msgs/srv/SetMapProjections",
    	    "nav_msgs/srv/GetMap", "nav_msgs/srv/GetPlan",
    	    "nav_msgs/srv/LoadMap", "nav_msgs/srv/SetMap",
    	    "sensor_msgs/srv/SetCameraInfo", "std_srvs/srv/Empty",
    	    "std_srvs/srv/SetBool", "std_srvs/srv/Trigger",
    	    "tf2_msgs/srv/FrameGraph", "visualization_msgs/srv/GetInteractiveMarkers",
    	    "example_interfaces/action/Fibonacci", "tf2_msgs/action/LookupTransform",
    	    "turtlesim/action/RotateAbsolute");
    private List<String> typeList = new ArrayList<String>();
	private List<String> currentList = new ArrayList<String>();

	/**
	 * コンストラクタ
	 *
	 * @param editor
	 *            親のエディタ
	 */
	public TopicEditorFormPage(ROSBuilderEditor editor) {
		super(editor, "id", Messages.getString("IMC.ROS_TOPIC_SECTION"));
		//
		preSelection = null;
		updateDefaultValue();
	}

	public void updateDefaultValue() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		typeList.clear();
		typeList.addAll(defaultList);
		typeList.addAll(extractROSEtcTypes(rosParam.getOutputProject(), "msg"));
		typeList.sort(null);
	}

	/**
	 * {@inheritDoc}
	 */
	protected void createFormContent(IManagedForm managedForm) {
		ScrolledForm form = super.createBase(managedForm, Messages.getString("IMC.ROS_TOPIC_SECTION"));
		FormToolkit toolkit = managedForm.getToolkit();
		//
		final Composite composite = createSectionBaseWithLabel(toolkit, form,
				Messages.getString("IMC.ROS_TOPIC_TITLE"), Messages.getString("IMC.ROS_TOPIC_EXPL"), 4);
		
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_TOPIC_TBLLBL_INPORTNAME"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		createLabel(toolkit, composite,
				IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_TOPIC_TBLLBL_OUTPORTNAME"),
				2,
				getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));

		subscribeTableViewer = createPortSection(toolkit, composite,"", 0,
				IRtcBuilderConstantsROS.SPEC_TOPIC_SUBSCRIBE);
		publishTableViewer = createPortSection(toolkit, composite, "", 1,
				IRtcBuilderConstantsROS.SPEC_TOPIC_PUBLISH);
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
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_TOPIC_TITLE"), IMessageConstantsROS.TOPIC_HINT_TOPIC_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_SUBSCRIBE_TITLE"), IMessageConstantsROS.TOPIC_HINT_SUBSCRIBE_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_PUBLISH_TITLE"), Messages.getString("IMC.ROS_TOPIC_HINT_PUBLISH_DESC"), toolkit, composite);
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_TOPICNAME"), Messages.getString("IMC.ROS_TOPIC_HINT_TOPIC_NAME_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_DATATYPE"), Messages.getString("IMC.ROS_TOPIC_HINT_MESSAGE_TYPE_DESC"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_PACKAGE_TITLE"), IMessageConstantsROS.TOPIC_HINT_PACKAGE_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_QoS_TITLE"), IMessageConstantsROS.TOPIC_HINT_QoS_DESC, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_RELIABILITY_TITLE"), IMessageConstantsROS.TOPIC_HINT_RELIABILITY_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_HISTORY_TITLE"), IMessageConstantsROS.TOPIC_HINT_HISTORY_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_DEPTH_TITLE"), IMessageConstantsROS.TOPIC_HINT_DEPTH_EXPL, toolkit, composite);
		createHintLabel(IMessageConstantsROS.TOPIC_CALLBACK_LBL, "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_VARNAME_SUBSCRIBE_TITLE"), IMessageConstantsROS.TOPIC_HINT_VARNAME_SUBSCRIBE_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_HINT_VARNAME_PUBLISH_TITLE"), IMessageConstantsROS.TOPIC_HINT_VARNAME_PUBLISH_EXPL, toolkit, composite);
		//
		createHintSpace(toolkit, composite);
		createHintLabel(Messages.getString("IMC.HINT_DOCUMENT_TITLE"), "", toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_DESCRIPTION"), IMessageConstantsROS.TOPIC_HINT_DOC_OVERVIEW_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_DATATYPE"), Messages.getString("IMC.ROS_TOPIC_HINT_DOC_DATATYPE"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_SEMANTICS"), Messages.getString("IMC.ROS_TOPIC_HINT_DOC_DETAIL"), toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_UNIT"), IMessageConstantsROS.TOPIC_HINT_DOC_UNIT_EXPL, toolkit, composite);
		createHintLabel(Messages.getString("IMC.ROS_TOPIC_LBL_OCCUR"), IMessageConstantsROS.TOPIC_HINT_DOC_OCCUR_EXPL, toolkit, composite);
	}

	private void createDetailSection(FormToolkit toolkit, ScrolledForm form) {
		Composite composite = createSectionBaseWithLabel(toolkit, form,
				"Detail", IMessageConstantsROS.TOPIC_DOCUMENT_EXPL, 2, 2);
		//
		topicNameText = createLabelAndRefText(toolkit, composite,
				Messages.getString("IMC.ROS_TOPIC_LBL_TOPICNAME"), SWT.BORDER, 1);
		//
		Group detailGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		detailGroup.setLayout(new GridLayout(5, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		detailGroup.setLayoutData(gd);
		//
		Label label = toolkit.createLabel(detailGroup, IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_TOPIC_LBL_DATATYPE"));
		label.setForeground(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED));
		messageTypeCombo = new Combo(detailGroup, SWT.DROP_DOWN);
		/////
		currentList.clear();
		currentList.addAll(typeList);
		for(String item : currentList) {
			messageTypeCombo.add(item);
		}
		/////
		messageTypeCombo.setText("");
		messageTypeCombo.addKeyListener(new KeyListener() {
			public void keyReleased(KeyEvent e) {
				String target = messageTypeCombo.getText();
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
				messageTypeCombo.removeAll();
				for(String item : currentList) {
					messageTypeCombo.add(item);
				}
				messageTypeCombo.setText(target);
				messageTypeCombo.setSelection(new Point(messageTypeCombo.getText().length(), messageTypeCombo.getText().length()) );
			}
			public void keyPressed(KeyEvent e) { }
		});
		messageTypeCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String selectedText = messageTypeCombo.getText();
                if(defaultList.contains(selectedText) == false) {
                	typePackageText.setText("");
                	typePackageText.setEnabled(true);
                	return;
                }
                
				int lastSlashIndex = selectedText.lastIndexOf('/');
				if (lastSlashIndex == -1) {
                	typePackageText.setText("");
                	typePackageText.setEnabled(true);
					return;
				}
				
				typePackageText.setText(selectedText.substring(0, lastSlashIndex));
            	typePackageText.setEnabled(false);
            }
        });
		GridData gdcombo = new GridData(GridData.FILL_HORIZONTAL);
		gdcombo.horizontalSpan = 2;
		messageTypeCombo.setLayoutData(gdcombo);

		Button selectButton = toolkit.createButton(detailGroup, "Select", SWT.PUSH);
		selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Shell shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		        FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		        fileDialog.setText("Select msg file");
		        fileDialog.setFilterExtensions(new String[] { "*.msg" });
		        fileDialog.setFilterNames(new String[] { "Msg Files (*.msg)" });
		        String selectedPath = fileDialog.open();
		        if (selectedPath == null) return;
		        
	        	File srcFile = new File(selectedPath);
	        	
	    		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
	    		IWorkspaceRoot workspaceHandle = ResourcesPlugin.getWorkspace().getRoot();
	    		IProject project = workspaceHandle.getProject(rosParam.getOutputProject());
	    		IFolder targetFolder = project.getFolder("msg");
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
				messageTypeCombo.removeAll();
				currentList.clear();
				currentList.addAll(typeList);
				for(String item : currentList) {
					messageTypeCombo.add(item);
				}
			}
		});
		
		Button reloadButton = toolkit.createButton(detailGroup, "ReLoad", SWT.PUSH);
		reloadButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
	    		updateDefaultValue();
				messageTypeCombo.removeAll();
				currentList.clear();
				currentList.addAll(typeList);
				for(String item : currentList) {
					messageTypeCombo.add(item);
				}
				
				preSelection = null;
				typePackageText.setText("");
				if(0 < messageTypeCombo.getItemCount()) {
					messageTypeCombo.select(0);
				}
			}
		});
		//
		typePackageText = createLabelAndText(toolkit, detailGroup,
									IMessageConstants.REQUIRED + Messages.getString("IMC.ROS_TOPIC_LBL_PACKAGE_TITLE"),
									SWT.BORDER, SWT.COLOR_RED, 2, 1);
		//
		Group qoSGroup = new Group(detailGroup, SWT.SHADOW_ETCHED_IN);
		qoSGroup.setLayout(new GridLayout(6, false));
		qoSGroup.setText("QoS");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 5;
		qoSGroup.setLayoutData(gd);
		
		reliabilityCombo = createCombo(toolkit, qoSGroup, 
										Messages.getString("IMC.ROS_TOPIC_LBL_RELIABILITY"),
										new String[] {"Reliable", "Best_Effort"});
		historyCombo = createCombo(toolkit, qoSGroup, 
				Messages.getString("IMC.ROS_TOPIC_LBL_HISTORY"),
				new String[] {"KeepLast", "KeepAll"});

		historyCombo.addSelectionListener(new SelectionListener() {
			  public void widgetDefaultSelected(SelectionEvent e){}
			  public void widgetSelected(SelectionEvent e){
				  depthText.setEnabled(historyCombo.getSelectionIndex() == 0);
				  update();
			  }
			});

		depthText = createLabelAndText(toolkit, qoSGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_DEPTH"), SWT.BORDER);
		/////
		variableNameText = createLabelAndText(toolkit, detailGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_VARNAME"), SWT.BORDER, SWT.COLOR_BLACK, 3, 2);

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
		typeText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_DATATYPE"), SWT.BORDER);
		semanticsText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_SEMANTICS"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		semanticsText.setLayoutData(gridData);
		unitText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_UNIT"), SWT.BORDER);
		occurrenceText = createLabelAndText(toolkit, documentGroup,
				Messages.getString("IMC.ROS_TOPIC_LBL_OCCUR"), SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.BORDER);
		occurrenceText.setLayoutData(gridData);
	}

	private TableViewer createPortSection(FormToolkit toolkit, Composite parent,
			String columnLabel, final int initSel, String role) {

		final TableViewer topicTableViewer = createTableViewer(toolkit,	parent, 70);
		topicTableViewer.getTable().setHeaderVisible(false);

		final TableViewerColumn col = super.createColumn(topicTableViewer, columnLabel, IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		col.setEditingSupport(new TopicEditingSuport(topicTableViewer));
//		col.getColumn().setResizable(false);
		topicTableViewer.setLabelProvider(new TopicParamLabelProvider());
		//
		parent.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Point size = topicTableViewer.getControl().getSize();
				ScrollBar vBar = topicTableViewer.getTable().getVerticalBar();
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
				String selected = messageTypeCombo.getText();
				updateDefaultValue();
				TopicParam selectParam = new TopicParam(role);
				((List) topicTableViewer.getInput()).add(selectParam);
				topicTableViewer.refresh();
				update();
				topicTableViewer.setSelection(new StructuredSelection(selectParam), true);
				messageTypeCombo.setText(selected);
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addButton.setLayoutData(gd);
		//
		Button deleteButton = toolkit.createButton(buttonComposite, "Delete", SWT.PUSH);
		deleteButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = topicTableViewer.getTable()
						.getSelectionIndex();
				if (selectionIndex >= 0
						&& ((List) topicTableViewer.getInput()).size() >= selectionIndex + 1) {
					((List) topicTableViewer.getInput())
							.remove(selectionIndex);
					topicTableViewer.refresh();
					preSelection = null;
					clearText();
					update();
				}
			}
		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		deleteButton.setLayoutData(gd);
		//
		topicTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				setDocumentContents();
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				selectParam = (TopicParam)selection.getFirstElement();
				if( selectParam != null ) {
					StringBuffer portName = new StringBuffer(selectParam.getName());
					if(event.getSource().equals(subscribeTableViewer)) {
						portName.append(" (Subscriber)");
					} else {
						portName.append(" (Publisher)");
					}
					topicNameText.setText(portName.toString());
					
					String msgType = selectParam.getMessageType();
					int lastSlashIndex = msgType.lastIndexOf('/');
					typePackageText.setEnabled(true);
					if(defaultList.contains(msgType)) {
						messageTypeCombo.setText(msgType);
						if (lastSlashIndex == -1) {
							typePackageText.setText("");
						} else {
							typePackageText.setText(msgType.substring(0, lastSlashIndex));
			            	typePackageText.setEnabled(false);
						}
					} else {
						if (lastSlashIndex == -1) {
							messageTypeCombo.setText(selectParam.getMessageType());
							typePackageText.setText("");
						} else {
							typePackageText.setText(msgType.substring(0, lastSlashIndex));
							messageTypeCombo.setText(msgType.substring(lastSlashIndex + 1));
						}
					}
					
					reliabilityCombo.setText(selectParam.getReliabilityType());
					historyCombo.setText(selectParam.getHistoryType());
					depthText.setText(selectParam.getDepth().toString());
					depthText.setEnabled(historyCombo.getSelectionIndex() == 0);
					variableNameText.setText(selectParam.getVarCallbackName());
					
					descriptionText.setText(StringUtil.getDisplayDocText(selectParam.getDocDescription()));
					typeText.setText(StringUtil.getDisplayDocText(selectParam.getDocType()));
					semanticsText.setText(StringUtil.getDisplayDocText(selectParam.getDocSemantics()));
					unitText.setText(StringUtil.getDisplayDocText(selectParam.getDocUnit()));
					occurrenceText.setText(StringUtil.getDisplayDocText(selectParam.getDocOccurrence()));
					preSelection = selectParam;
				}
			}
		});
		return topicTableViewer;
	}

	public void update() {
		if (selectParam != null) {
			String typeMsg = messageTypeCombo.getText();
			if(defaultList.contains(typeMsg) == false) {
				String typePackage = typePackageText.getText(); 
				if(typePackage == null || typePackage.length() == 0) {
					selectParam.setMessageType(typeMsg);
				} else {
					selectParam.setMessageType(typePackage + "/" + typeMsg);
				}
			}

			selectParam.setReliabilityType(reliabilityCombo.getText());
			selectParam.setHistoryType(historyCombo.getText());
			try {
				int depth = 0;
				depth = Integer.parseInt(depthText.getText());
				selectParam.setDepth(depth);
			} catch (Exception ex){
			}
			
			selectParam.setVarCallbackName(variableNameText.getText());

			selectParam.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			selectParam.setDocType(StringUtil.getDocText(typeText.getText()));
			selectParam.setDocSemantics(StringUtil.getDocText(semanticsText.getText()));
			selectParam.setDocUnit(StringUtil.getDocText(unitText.getText()));
			selectParam.setDocOccurrence(StringUtil.getDocText(occurrenceText.getText()));
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
			String typeMsg = messageTypeCombo.getText();
			if(defaultList.contains(typeMsg) == false) {
				String typePackage = typePackageText.getText(); 
				if(typePackage == null || typePackage.length() == 0) {
					selectParam.setMessageType(messageTypeCombo.getText());
				} else {
					selectParam.setMessageType(typePackage + "/" + messageTypeCombo.getText());
				}
			} else {
				selectParam.setMessageType(messageTypeCombo.getText());
			}
			
			preSelection.setReliabilityType(reliabilityCombo.getText());
			preSelection.setHistoryType(historyCombo.getText());
			try {
				int depth = 0;
				depth = Integer.parseInt(depthText.getText());
				preSelection.setDepth(depth);
			} catch (Exception ex){
			}
			preSelection.setVarCallbackName(variableNameText.getText());
			//
			preSelection.setDocDescription(StringUtil.getDocText(descriptionText.getText()));
			preSelection.setDocType(StringUtil.getDocText(typeText.getText()));
			preSelection.setDocSemantics(StringUtil.getDocText(semanticsText.getText()));
			preSelection.setDocUnit(StringUtil.getDocText(unitText.getText()));
			preSelection.setDocOccurrence(StringUtil.getDocText(occurrenceText.getText()));
		}
	}

	private void clearText() {
		topicNameText.setText("");
		messageTypeCombo.setText("");
		typePackageText.setText("");
		reliabilityCombo.select(0);
		historyCombo.select(0);
		depthText.setText("");
		variableNameText.setText("");
		
		descriptionText.setText("");
		typeText.setText("");
		semanticsText.setText("");
		unitText.setText("");
		occurrenceText.setText("");
	}

	/**
	 * データをロードする
	 */
	public void load() {
		if (subscribeTableViewer == null) return;
		
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		publishTableViewer.setInput(rosParam.getTopicPublishes());
		subscribeTableViewer.setInput(rosParam.getTopicSubscribes());
		//Mac版では列の幅が最小化してしまうため，再度列幅を設定
		publishTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		subscribeTableViewer.getTable().getColumn(0).setWidth(IRtcBuilderConstants.SINGLE_COLUMN_WIDTH);
		//
		StructuredSelection selection = (StructuredSelection) publishTableViewer
				.getSelection();
		TopicParam outParam = (TopicParam) selection.getFirstElement();
		selection = (StructuredSelection) subscribeTableViewer.getSelection();
		TopicParam inParam = (TopicParam) selection.getFirstElement();
		if (outParam == null && inParam == null) clearText();
		//
		((ROSBuilderEditor)editor).updateEMFPorts(
				rosParam.getTopicSubscribes(), rosParam.getTopicPublishes(),
				rosParam.getServiceServers(), rosParam.getServiceClients(),
				rosParam.getActionServers(), rosParam.getActionClients());
	}

	public String validateParam() {
		ROSParam rosParam = ((ROSBuilderEditor)editor).getROSParam();
		return rosParam.validateTopicInfo();
	}

	private class TopicParamLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof TopicParam == false) return null;
			TopicParam topicParam = (TopicParam) element;
			return topicParam.getName();
		}
	}
	
	

	private class TopicEditingSuport extends EditingSupport {
		private ColumnViewer viewer;

		public TopicEditingSuport(ColumnViewer viewer) {
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
			if (element instanceof TopicParam == false) return null;
			TopicParam topicParam = (TopicParam) element;
			return topicParam.getName();
		}

		@Override
		protected void setValue(Object element, Object value) {
			if (element instanceof TopicParam == false) return;
			TopicParam topicParam = (TopicParam) element;

			topicParam.setName((String) value);
			StringBuffer portName = new StringBuffer(topicParam.getName());
			if( this.getViewer()==subscribeTableViewer ) {
				portName.append(" (Subscriber)");
			} else {
				portName.append(" (Publisher)");
			}
			topicNameText.setText(portName.toString());

			getViewer().update(element, null);
			update();
		}
	}
}
