package org.simplug.framework.core.config;

public interface Configuration {	
	/* Define all possible configuration parameters inside here */
	public static final String CONF_PLUGIN_DIRECTORY = "org.simplug.framework.core.plugindir";
	public static final String CONF_PLUGIN_LOADING_METHOD = "org.simplug.framework.core.pluginloading";
	public static final String CONF_PLUGIN_CONFIG_NAME = "org.simplug.framework.core.pluginconf.name";
	
	/* Define all possible values for configuration parameters here */
	public static final String PLUGIN_LOADING_CONFIG_BASED = "configurations";
	public static final String PLUGIN_LOADING_ANNOTATION_BASED = "annotations";
	
	public String getProperty(String key);
	
	public void loadConfig();
}
