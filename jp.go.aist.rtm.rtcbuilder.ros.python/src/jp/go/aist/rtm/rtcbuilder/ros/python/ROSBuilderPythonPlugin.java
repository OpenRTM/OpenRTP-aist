package jp.go.aist.rtm.rtcbuilder.ros.python;

import jp.go.aist.rtm.toolscommon.profiles.util.LoggerUtil;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class ROSBuilderPythonPlugin extends AbstractUIPlugin {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ROSBuilderPythonPlugin.class);

	// The plug-in ID
	public static final String PLUGIN_ID = "jp.go.aist.rtm.rtcbuilder.python";

	// The shared instance
	private static ROSBuilderPythonPlugin plugin;
	
	/**
	 * The constructor
	 */
	public ROSBuilderPythonPlugin() {
		LoggerUtil.setup();
		LOGGER.trace("RtcBuilderPythonPlugin: START");

		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ROSBuilderPythonPlugin getDefault() {
		return plugin;
	}

}
