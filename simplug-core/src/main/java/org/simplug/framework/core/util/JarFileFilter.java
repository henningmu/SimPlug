package org.simplug.framework.core.util;

import java.io.File;
import java.io.FileFilter;

/**
 * Simple FileFilter checking for jar extension (.jar) at the
 * end of each file. The matching is case-insensitive meaning .JAR,
 * .jar or even .JaR or jAR is matched.
 * */
public class JarFileFilter implements FileFilter {

	private static final String JAR_EXTENSION = ".jar";
	
	/**
	 * Returns <code>true</code> if the passed pathname ends on .jar (case insensitive) otherwise returns <code>false</code>.
	 * 
	 * @return
	 * 		<code>true</code> if the pathname ends on .jar otherwise returns <code>false</code>
	 * */
	public boolean accept(File pathname) {
		boolean isJar = pathname.getName().toLowerCase().endsWith(JAR_EXTENSION);
		return isJar;
	}
}
