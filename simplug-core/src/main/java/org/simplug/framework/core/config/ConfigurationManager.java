package org.simplug.framework.core.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is implementing the configuration interface
 * and makes it possible to access the configuration of the SimPlug Framework.
 * It is defined as a singleton. So there shouldnt be any hussle of various 
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
	 * Since the ConfigurationManager is defined as singleton one is not able to 
	 * call the constructor directly but may access an instance of the manager with this method.
	 * Every call of this method returns the same instance.
	 * 
	 * @return
	 * 		the instance of the ConfigurationManager
	 * */
	public static ConfigurationManager getInstance() {
		if(instance == null) {
			instance = new ConfigurationManager();
		}
		
		return instance;
	}

	/**
	 * @see Configuration.getProperty(String key)
	 * */
	public String getProperty(String key) {
		if(properties != null) {
			return "" + properties.getProperty(key);
		}
		return null;
	}

	/**
	 * @see Configuration.loadConfig()
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
