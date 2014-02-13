package org.simplug.framework.core.util;

import java.io.File;
import java.io.FileFilter;

public class JarFileFilter implements FileFilter {

	private static final String JAR_EXTENSION = ".jar";
	
	public boolean accept(File pathname) {
		boolean isJar = pathname.getName().toLowerCase().endsWith(JAR_EXTENSION);
		return isJar;
	}
}
