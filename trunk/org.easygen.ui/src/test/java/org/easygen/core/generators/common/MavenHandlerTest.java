package org.easygen.core.generators.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.sun.java_cup.internal.production;

public class MavenHandlerTest {

	private static final Logger logger = Logger.getLogger(MavenHandlerTest.class);

	@Test
	public void testProcessBuilder() {
		ProcessBuilder builder = new ProcessBuilder("mvn.bat");
		builder.directory(new File("."));
		try {
			Process process = builder.start();
			logger.debug(IOUtils.toString(process.getInputStream()));
		} catch (IOException e) {
			logger.error("Can't start process", e);
		}
	}
//
//	@Test
//	public void testIsMavenFound() {
//		MavenHandler handler = new MavenHandler();
//		logger.debug("isMavenFound: "+handler.isMavenFound());
//	}
}