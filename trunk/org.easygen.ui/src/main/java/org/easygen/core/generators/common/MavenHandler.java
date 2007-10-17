package org.easygen.core.generators.common;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class MavenHandler {
	
	private static final Logger logger = Logger.getLogger(MavenHandler.class);

	private static final String MVN_WIN_CMD = "mvn.bat";
	private static final String MVN_UNIX_CMD = "mvn.bat";

	private static String MVN_CMD = MVN_UNIX_CMD;
	
	static {
		if (logger.isDebugEnabled()) {
			logger.debug("Running on "+System.getProperty("os.name"));
		}
		if (System.getProperty("os.name").indexOf("indows") != -1) {
			MVN_CMD = MVN_WIN_CMD;
		}
	}

	private boolean mavenFound = false;

	public MavenHandler() {
		if (callMaven(".", "--version")) {
			mavenFound = true;
		} else {
			logger.warn("Maven not found in path");
		}
	}

	public boolean isMavenFound() {
		return mavenFound;
	}

	public boolean callMaven(String path, String goal) {
		ProcessBuilder builder = new ProcessBuilder(MVN_CMD, goal);
		builder.directory(new File(path));
		builder.redirectErrorStream(true);
		Process process = null;
		try {
			process = builder.start();
			InputStream inputStream = process.getInputStream();
			String processOutput = IOUtils.toString(inputStream);
			if (logger.isDebugEnabled()) {
				logger.debug("Executing command: "+MVN_CMD+" " + goal + "\n" + processOutput);
			}
			if (process.waitFor() != 0) {
				logger.warn("Can't execute maven goal " + goal + ", maybe "+MVN_CMD+" command is not in the PATH var");
				return false;
			}
			return true;
		} catch (IOException e) {
			logger.warn("Error while starting maven goal", e);
			return false;
		} catch (InterruptedException e) {
			logger.warn("Process interrupted", e);
			return false;
		} finally {
			if (process != null) {
				process.destroy();
				try {
					process.getInputStream().close();
					process.getOutputStream().close();
				} catch (IOException e) {
					logger.warn("Can't close process stream handles", e);
				}
			}
		}
	}
	// mvn --version
	// Maven version: 2.0.7
	// Java version: 1.6.0_01
	// OS name: "windows xp" version: "5.1" arch: "x86"
}
