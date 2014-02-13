package org.simplug.framework.core.config;

public interface Configuration {
	/* Define all possible configuration parameters inside here */
	public static final String CONF_PLUGIN_DIRECTORY = "org.simplug.framework.core.plugindir";
	public static final String CONF_PLUGIN_CONFIG_NAME = "org.simplug.framework.core.pluginconf.name";
	
	public String getProperty(String key);
	
	public void loadConfig();
}
