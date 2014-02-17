package org.simplug.framework.core.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is implementing the {@link Configuration} interface
 * and makes it possible to access the configuration of the SimPlug Framework.
 * It is defined as a singleton. So there should not be any problems of various 
 * threads accessing the configuration at the same time.
 * */
public class ConfigurationManager implements Configuration{	
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationManager.class);
	private static final String CONFIGURATION = "simplug.conf";
	
	private static ConfigurationManager instance;
	private static PropertiesConfiguration properties;
	
	private ConfigurationManager() {
	}
	
	/**
	 * The ConfigurationManager is defined as singleton. One is not able to 
	 * call the constructor directly but may access an instance of the manager with this method.
	 * Every call of this method returns the same instance.
	 * 
	 * @return
	 * 		the instance of the ConfigurationManager.
	 * */
	public static ConfigurationManager getInstance() {
		if(instance == null) {
			instance = new ConfigurationManager();
		}
		
		return instance;
	}

	/**
	 * This method reads the configuration and returns the property value for the given key.
	 * 
	 * @param key
	 * 		the property key to access.
	 * @return
	 * 		the property value specified by the key.
	 * */
	public String getProperty(String key) {
		if(properties != null) {
			return "" + properties.getProperty(key);
		}
		return null;
	}

	/**
	 * This methods loads the configuration file and should store it
	 * for later access of properties.
	 * */
	public void loadConfig() {
		try {
			properties = new PropertiesConfiguration(CONFIGURATION);
		} catch (ConfigurationException e) {
			LOG.warn("ConfigurationException while trying to load config: {}. "
					+ "Message: {}", new Object[]{ CONFIGURATION, e.getMessage()});
		}
	}
}
