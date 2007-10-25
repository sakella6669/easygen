package org.easygen.core.generators;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.easygen.core.config.Dependency;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.struts2.Struts2ModuleConfig;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DependenciesLoaderTest {

	@SuppressWarnings("unchecked")
	@Test
	public void testLoadDependencies() throws Exception {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		Properties properties = new Properties();
		InputStream inStream = contextClassLoader.getResourceAsStream("cfg/velocity.properties");
		if (inStream == null)
			throw new GenerationException("Velocity initialization failed");
		properties.load(inStream);
		Velocity.init(properties);

		ProjectConfig projectConfig = new ProjectConfig();
		Struts2ModuleConfig struts2ModuleConfig = new Struts2ModuleConfig();
		struts2ModuleConfig.setTemplateEngine(Struts2ModuleConfig.SITE_MESH);
		projectConfig.setViewModuleConfig(struts2ModuleConfig);

		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("project", projectConfig);

		String xmlFile = "org/easygen/core/generators/dependencies.xml";
		Template template = Velocity.getTemplate(xmlFile);
		
		StringWriter stringWriter = new StringWriter();
		template.merge(velocityContext, stringWriter);
		
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("dependencies", LinkedList.class);
		xstream.alias("dependency", Dependency.class);
		List<Dependency> dependencies = (List<Dependency>) xstream.fromXML(stringWriter.toString());
		for (Dependency dependency : dependencies) {
			System.out.println(dependency);
		}
	}
	
}
