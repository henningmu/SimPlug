package org.simplug.framework.core.config;

/**
 * Interface describing the configuration of the SimPlug Framework.
 * It holds all the possible properties of the configuration and 
 * for some properties the possible values for easier access.
 * It also defines the methods to access the configuration
 * */
public interface Configuration {	
	/* Define all possible configuration parameters inside here */
	public static final String CONF_PLUGIN_DIRECTORY = "org.simplug.framework.core.plugindir";
	public static final String CONF_PLUGIN_LOADING_METHOD = "org.simplug.framework.core.pluginloading";
	public static final String CONF_PLUGIN_CONFIG_NAME = "org.simplug.framework.core.pluginconf.name";
	
	/* Define all possible values for configuration parameters here */
	public static final String PLUGIN_LOADING_CONFIG_BASED = "configurations";
	public static final String PLUGIN_LOADING_ANNOTATION_BASED = "annotations";
	
	/**
	 * This method reads the configuration and returns the property value for the given key.
	 * 
	 * @param key
	 * 		the property key to access.
	 * @return
	 * 		the property value specified by the key.
	 * */
	public String getProperty(String key);
	
	/**
	 * This methods loads the configuration file and should store it
	 * for later access of properties.
	 * */
	public void loadConfig();
}
