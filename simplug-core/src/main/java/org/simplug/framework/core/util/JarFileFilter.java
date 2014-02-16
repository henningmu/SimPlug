package org.simplug.framework.core.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Simple FileFilter checking for jar extension (.jar) at the
 * end of each file. Matches are case-insensitive meaning .JAR,
 * .jar or even .JaR or jAR is matched.
 * */
public class JarFileFilter implements FileFilter {

	private static final String JAR_EXTENSION = ".jar";
	
	public boolean accept(File pathname) {
		boolean isJar = pathname.getName().toLowerCase().endsWith(JAR_EXTENSION);
		return isJar;
	}
}
