package org.easygen.core.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;

/**
 * 
 */
public class ProjectConfigSerializer {
	
	public void serialize(ProjectConfig projectConfig, File outputFile) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
		getXStream().toXML(projectConfig, fileOutputStream);
	}

	public ProjectConfig unserialize(File inputFile) throws IOException {
		return (ProjectConfig) getXStream().fromXML(new FileInputStream(inputFile));
	}

	/**
	 * @return
	 */
	protected XStream getXStream() {
		XStream xstream = new XStream();
		xstream.alias("projectConfig", ProjectConfig.class);
		return xstream;
	}
}
