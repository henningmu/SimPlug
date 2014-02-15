package org.simplug.framework.core.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarFileUtilities {

	private static final Logger LOG = LoggerFactory
			.getLogger(JarFileUtilities.class);

	public static File[] getAllJarFilesFromPath(String pathname) {
		File directory;
		try {
			directory = new File(pathname);
		} catch (NullPointerException npe) {
			LOG.warn("NullPointerException while trying to acquire all Jar files from directory. "
					+ "Probably the pathname is invalid. Passed pathname: " + pathname);
			return null;
		}

		return directory.listFiles(new JarFileFilter());
	}
	
	public static URL[] convertFilesToUrls(File[] files) {
		URL[] urls = new URL[files.length];
		
		for(int i = 0; i < files.length; i++) {
			try {
				urls[i] = files[i].toURI().toURL();
			} catch (MalformedURLException e) {
				LOG.warn("MalformedURLException: Could not transform file to URL. " 
						+ "Responsible file: " + files[i].getName());
				return null;
			}
		}
		
		return urls;
	}
}
