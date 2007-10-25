package org.easygen.core;

import junit.framework.TestCase;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.easygen.core.config.CommonConfig;
import org.easygen.core.config.DatabaseConfig;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.db.DatabaseException;
import org.easygen.core.db.DatabaseManager;
import org.easygen.core.generators.GenerationException;
import org.easygen.core.generators.GeneratorManager;
import org.easygen.core.generators.hibernate.HibernateModuleConfig;
import org.easygen.core.generators.project.EclipseProjectConfig;
import org.easygen.core.generators.springservice.SpringServiceModuleConfig;

/**
 * @author eveno
 * Created on 16 nov. 06
 *
 */
public class GenerationTestCase extends TestCase {

	static {
		// Date Pattern : [%d]
		ConsoleAppender appender = new ConsoleAppender(new PatternLayout("%-5p [%C{1}.%M:%L] - %m%n"));
		BasicConfigurator.configure(appender);
		Logger.getRootLogger().setLevel(Level.INFO);
		Logger.getLogger(DatabaseManager.class).setLevel(Level.INFO);
	}

	private ProjectConfig projectConfig;

	/**
	 * @throws InitException
	 *
	 */
	public GenerationTestCase() throws InitException {
		super();
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if (projectConfig == null)
			initProjectConfig();
	}

	protected void initProjectConfig() throws InitException, DatabaseException {
		projectConfig = new ProjectConfig();
		projectConfig.setName("Test");
		projectConfig.setPackageName("test");
		projectConfig.setPath("C:/temp/Test");
		projectConfig.setSrcDirname("src");
		projectConfig.setBuildDirname("classes");
		projectConfig.setLibDirname("lib");
		projectConfig.setTestDirname("test");

		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setDatabaseDriver(DatabaseManager.getDriverList("mysql")[0]);
		dbConfig.setDatabaseType("mysql");
		dbConfig.setDatabaseName("teachertools");
		dbConfig.setHost("localhost");
		dbConfig.setPort(3306);
		dbConfig.setUsername("root");
		dbConfig.setJarPath(DatabaseManager.getDriverJarUrl("mysql").toString());
		//dbConfig.setJarPath("file:///D:/eclipse/libraries/mysql-connector-java-5.0.3-bin.jar");
		projectConfig.setDatabaseConfig(dbConfig);

		projectConfig.setProjectNature(EclipseProjectConfig.WTP_NATURE);
		EclipseProjectConfig projectModuleConfig = new EclipseProjectConfig();
		projectConfig.setProjectModuleConfig(projectModuleConfig);

		CommonConfig commonConfig = new CommonConfig();
		commonConfig.setNature(CommonConfig.NATURE);
		commonConfig.setPackageName("test.utils");
		projectConfig.setCommonConfig(commonConfig);

		projectConfig.setDataModuleNature(HibernateModuleConfig.NATURE);
		HibernateModuleConfig dataModuleConfig = new HibernateModuleConfig();
		dataModuleConfig.setPackageName("test.data");
		projectConfig.setDataModuleConfig(dataModuleConfig);

		projectConfig.setObjects(new DatabaseManager(dbConfig).extractData(true));

		projectConfig.setServiceModuleNature(SpringServiceModuleConfig.SPRING_NATURE);
		SpringServiceModuleConfig serviceConfig = new SpringServiceModuleConfig();
		serviceConfig.setPackageName("test.services");
		projectConfig.setServiceModuleConfig(serviceConfig);
	}

	public void testGenerate() throws GenerationException {
		new GeneratorManager().generate(projectConfig);
	}
}
