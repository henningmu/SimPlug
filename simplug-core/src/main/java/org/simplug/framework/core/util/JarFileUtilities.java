package org.simplug.framework.core.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to assist in accessing Jars.
 * */
public class JarFileUtilities {

	private static final Logger LOG = LoggerFactory
			.getLogger(JarFileUtilities.class);

	/**
	 * This method extracts all jar files (files ending on ".jar") inside
	 * the given directory. If any errors occurs the method logs the error and returns null.
	 * 
	 * @param pathname
	 * 		the directory to scan for jar files
	 * 
	 * @return
	 * 		array with all jar files in the given directory or null if an error occurred
	 * */
	public static File[] getAllJarFilesFromPath(String pathname) {
		File directory;
		try {
			directory = new File(pathname);
			return directory.listFiles(new JarFileFilter());
			
		} catch (NullPointerException npe) {
			LOG.warn("NullPointerException while trying to acquire all Jar files from directory. "
					+ "Probably the pathname is invalid. Passed pathname: {}. Message: {}",
					pathname, npe.getMessage());
			return null;
		} catch(SecurityException se) {
			LOG.warn("SecurityException while trying to access the directory: {}:{}",
					pathname, se.getMessage());
			return null;
		}
	}
	
	/**
	 * This method takes an array of files and converts it into an
	 * array of Urls. This is can be used to instantiate an
	 * @see URLClassLoader for examople. If any error occurrs this method
	 * logs the error and returns null instead of an array.
	 * 
	 * @param files
	 * 		the files to convert to Urls
	 * 
	 * @return
	 *  	array with all Urls belonging to the given files or null if an error occured
	 * */
	public static URL[] convertFilesToUrls(File[] files) {
		URL[] urls = new URL[files.length];
		
		for(int i = 0; i < files.length; i++) {
			try {
				urls[i] = files[i].toURI().toURL();
			} catch (MalformedURLException e) {
				LOG.warn("MalformedURLException: Could not transform file to URL. " 
						+ "Responsible file: {}", files[i].getName());
				return null;
			}
		}
		
		return urls;
	}
}
