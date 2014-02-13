package org.simplug.framework.core.config;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigurationManager implements Configuration{	
	
	private static final Logger LOG = LoggerFactory.getLogger(ConfigurationManager.class);
	private static final String CONFIGURATION = "simplug.conf";
	
	private static ConfigurationManager instance;
	private static PropertiesConfiguration properties;
	
	private ConfigurationManager() {
	}
	
	public static ConfigurationManager getInstance() {
		if(instance == null) {
			instance = new ConfigurationManager();
		}
		
		return instance;
	}

	public String getProperty(String key) {
		if(properties != null) {
			return "" + properties.getProperty(key);
		}
		return null;
	}

	public void loadConfig() {
		try {
			properties = new PropertiesConfiguration(CONFIGURATION);
		} catch (ConfigurationException e) {
			LOG.warn("ConfigurationException while trying to load config: " + CONFIGURATION + ". "
					+ "Message: " + e.getMessage());
		}
	}
}