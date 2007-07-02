package org.easygen.core.db;

import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.DatabaseConfig;

import junit.framework.TestCase;

/**
 * 
 */
public class DatabaseManagerTest extends TestCase {

	protected DatabaseConfig dbConfig;
	
//	protected static final String[] expectedTableNames = { "columns_priv", "db", "func", "help_category", "help_keyword", "help_relation",
//			"help_topic", "host", "proc", "procs_priv", "tables_priv", "time_zone", "time_zone_leap_second",
//			"time_zone_name", "time_zone_transition", "time_zone_transition_type", "user" };

	protected static final String[] expectedTableNames = { "categorie", "competence", "eleve", "livret", "livret_contenu", "matiere", "note" };

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		if (dbConfig == null) {
			dbConfig = new DatabaseConfig();
			dbConfig.setDataBaseDriver( DatabaseManager.getDriverList("mysql")[0] );
			dbConfig.setDatabaseType("mysql");
			dbConfig.setDatabaseName("teachertools");
			dbConfig.setHost("localhost");
			dbConfig.setPort(3306);
			dbConfig.setUsername("root");
			dbConfig.setJarPath("file:///D:/eclipse/libraries/mysql-connector-java-5.0.3-bin.jar");
		}
	}
	/**
	 * Test method for {@link org.easygen.core.db.DatabaseManager#getDriverList(java.lang.String)}.
	 */
	public void testGetDriverList() {
		String[] actualDriverList = DatabaseManager.getDriverList("mysql");
		String[] expectedDriverList = { "com.mysql.jdbc.Driver" };
		
		// Every driver present in expected should be in actual
		for (String driverClassname : expectedDriverList) {
			assertTrue(ArrayUtils.contains(actualDriverList, driverClassname));
		}
	}
	/**
	 * 
	 */
	public void testComputeURL() {
		String actual = DatabaseManager.computeURL("mysql", "localhost", 3306, "mysql");
		String expected = "jdbc:mysql://localhost:3306/mysql";
		assertEquals(expected, actual);
	}
	/**
	 * Test method for {@link org.easygen.core.db.DatabaseManager#testConnection(org.easygen.core.config.DatabaseConfig)}.
	 * @throws DatabaseException 
	 */
	public void testTestConnection() throws DatabaseException {
		DatabaseManager.testConnection(dbConfig);
	}
	/**
	 * Test method for {@link org.easygen.core.db.DatabaseManager#extractData(boolean)}.
	 */
	public void testGetTableNames() throws DatabaseException {
		List<String> actualTableNames = new DatabaseManager(dbConfig).getTableNames();
		assertNotNull(actualTableNames);
		assertTrue(actualTableNames.size() != 0);
		System.out.println(actualTableNames);
		for (String tableName : actualTableNames) {
			assertTrue(ArrayUtils.contains(expectedTableNames, tableName));
		}
	}
	/**
	 * Test method for {@link org.easygen.core.db.DatabaseManager#extractData(boolean)}.
	 */
	public void testExtractData() throws DatabaseException {
		List<DataObject> actualObjects = new DatabaseManager(dbConfig).extractData();
		assertNotNull(actualObjects);
		assertTrue(actualObjects.size() != 0);

		for (DataObject dataObject : actualObjects) {
			assertTrue(ArrayUtils.contains(expectedTableNames, dataObject.getTableName()));
		}
		System.out.println(actualObjects.toString());
	}
}
