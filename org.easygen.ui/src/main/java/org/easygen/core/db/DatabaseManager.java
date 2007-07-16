package org.easygen.core.db;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.easygen.core.InitException;
import org.easygen.core.config.DataField;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.DatabaseConfig;
import org.easygen.core.config.ObjectRelation;

public class DatabaseManager {

	public static final String[] DATABASE_TYPES = { "MySql", "Oracle", "SqlServer" };// , "PostGres", "Sybase" };

	private static final String DATABASE_CONFIG_DIR = "database/";
	private static final String LIBRARIES_DIR = "libraries/";

	private static final Map<String, PropertiesConfiguration> databaseProperties = new Hashtable<String, PropertiesConfiguration>();

	protected final static Logger logger = Logger.getLogger(DatabaseManager.class);

	public static String[] getDriverList(String databaseType) {
		String driverNames = getDatabaseProperty(databaseType, "driverNames");
		if (driverNames != null)
			return driverNames.split(";");
		return new String[0];
	}

	public static URL getDriverJarUrl(String databaseType) {
		String jarFilename = getDatabaseProperty(databaseType, "jarFilename");
		String jarPath = DATABASE_CONFIG_DIR + LIBRARIES_DIR + jarFilename;
		URL jarUrl = Thread.currentThread().getContextClassLoader().getResource(
				jarPath
		);
		return jarUrl;
	}

	public static String getDatabaseProperty(String databaseType, String key) {
		Configuration databaseProps = getDatabaseProperties(databaseType);
		if (databaseProps.containsKey(key))
			return databaseProps.getString(key);
		return "";
	}

	protected static Configuration getDatabaseProperties(String databaseType) {
		String type = databaseType.toLowerCase();
		if (!databaseProperties.containsKey(type))
			try {
				initProperties(type);
			} catch (InitException e) {
				return new PropertiesConfiguration();
			}
		return (Configuration) databaseProperties.get(type);
	}
	/**
	 * @param databaseType
	 * @throws InitException
	 */
	protected static void initProperties(String databaseType) throws InitException {
		try {
			URL propertiesURL = Thread.currentThread().getContextClassLoader().getResource(
					DATABASE_CONFIG_DIR + databaseType + ".properties");
			logger.debug("Database Definition file : "+propertiesURL);
			PropertiesConfiguration.setDefaultListDelimiter('|');
			PropertiesConfiguration properties = new PropertiesConfiguration(propertiesURL);
			databaseProperties.put(databaseType, properties);
		} catch (Exception e) {
			throw new InitException("Impossible de charger les propriétés de la base " + databaseType, e);
		}
	}
	/**
	 * Tests connection to the database.<br />
	 * Try to connect to database and to execute a simple query.
	 *
	 * @return true if connectin could be etablished
	 * @throws DatabaseException
	 */
	public static void testConnection(DatabaseConfig dbConfig) throws DatabaseException {
		Connection connection = null;
		try {
			registerDriver(dbConfig);
			connection = createConnection(dbConfig);
			Statement stmt = connection.createStatement();
			String testQuery = getDatabaseProperty(dbConfig.getDatabaseType(), "connection.test");
			ResultSet rs = stmt.executeQuery(testQuery);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			Logger.getLogger(DatabaseManager.class).error("Echec du test de connection à la base", e);
			throw new DatabaseException("Echec du test de connection à la base", e);
		} catch (InitException e) {
			Logger.getLogger(DatabaseManager.class).error("Impossible de charger le driver JDBC", e);
			throw new DatabaseException("Echec du test de connection à la base: Impossible de charger le driver JDBC", e);
		} finally {
			closeConnection(connection);
		}
	}

	private Configuration properties = null;
	private DatabaseConfig databaseConfig;

	/**
	 *
	 * @param pDataBaseConfig
	 * @throws IOException
	 */
	public DatabaseManager(DatabaseConfig pDatabaseConfig) throws InitException {
		databaseConfig = pDatabaseConfig;

		registerDriver(databaseConfig);

		properties = getDatabaseProperties(databaseConfig.getDatabaseType());
	}

	protected static void registerDriver(DatabaseConfig pDatabaseConfig) throws InitException {
		try {
			URL[] urls = { new URL(pDatabaseConfig.getJarPath()) };
			URLClassLoader urlLoader = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
			urlLoader.loadClass(pDatabaseConfig.getDataBaseDriver());

			Object o = Class.forName(pDatabaseConfig.getDataBaseDriver(), true, urlLoader).newInstance();
			DriverManager.registerDriver(new FakeDriver((Driver) o));
		} catch (MalformedURLException e) {
			throw new InitException("Fichier jar introuvable: " + pDatabaseConfig.getJarPath(), e);
		} catch (SQLException e) {
			throw new InitException("Erreur de déclaration du Driver JDBC", e);
		} catch (Exception e) {
			throw new InitException("Impossible de charger le driver JDBC", e);
		}
	}

	public static String computeURL(String databaseType, String host, int port, String databaseName) {
		Configuration properties = getDatabaseProperties(databaseType);
		String urlSkeleton = properties.getString("url");
		return MessageFormat.format(urlSkeleton, new Object[] { host, String.valueOf(port), databaseName });
	}

	protected static Connection createConnection(DatabaseConfig pDatabaseConfig) throws SQLException {
		String url = computeURL(pDatabaseConfig.getDatabaseType(), pDatabaseConfig.getHost(),
				pDatabaseConfig.getPort(), pDatabaseConfig.getDatabaseName());
		return DriverManager.getConnection(url, pDatabaseConfig.getUsername(), pDatabaseConfig.getPassword());
	}

	/**
	 * @param connection
	 */
	protected static void closeConnection(Connection connection) /* throws DatabaseException */
	{
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
			Logger.getLogger(DatabaseManager.class).warn("Erreur pendalt la fermeture de la connection", e);
		}
	}

	public List<String> getTableNames() throws DatabaseException {
		Connection connection = null;
		try {
			List<String> tableNames = new LinkedList<String>();

			connection = createConnection(databaseConfig);
			ResultSet results = connection.getMetaData().getTables(null, null, "", null);
			
			while (results.next()) {
				tableNames.add(results.getString("TABLE_NAME"));
			}
			results.close();
			Collections.sort(tableNames);
			return tableNames;
		} catch (SQLException e) {
			throw new DatabaseException("Can't retreive table list", e);
		} finally {
			closeConnection(connection);
		}
	}

	protected boolean isNullable(int value) {
		return (value == ResultSetMetaData.columnNullable);
	}

	protected Map<String, List<String>> getPks(Connection connection) throws SQLException {
		String sqlQuery = properties.getString("query.allPk");

		PreparedStatement statement = connection.prepareStatement(sqlQuery);
		statement.setString(1, databaseConfig.getDatabaseName());

		ResultSet results = statement.executeQuery();

		Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
		while (results.next()) {
			String key = results.getString("TABLE_NAME");
			if (!resultMap.containsKey(key))
				resultMap.put(key, new LinkedList<String>());

			List<String> pks = resultMap.get(key);
			pks.add(results.getString("COLUMN_NAME"));
		}
		results.close();
		statement.close();
		return resultMap;
	}

	protected Map<String, Map<String, ObjectRelation>> getFks(Connection connection) throws SQLException {
		String request = properties.getString("query.allFk");
		PreparedStatement statement = connection.prepareStatement(request);
		ResultSet results = statement.executeQuery();
		Map<String, Map<String, ObjectRelation>> resultMap = new Hashtable<String, Map<String, ObjectRelation>>();
		while (results.next()) {
			String tableName = results.getString(1);
			String columnName = results.getString(2);
			String foreignTableName = results.getString(3);
			String foreignColumnName = results.getString(4);
			if (!resultMap.containsKey(tableName))
				resultMap.put(tableName, new Hashtable<String, ObjectRelation>());

			Map<String, ObjectRelation> fks = (Map<String, ObjectRelation>) resultMap.get(tableName);
			ObjectRelation fk = new ObjectRelation();
			fk.setForeignTableName(foreignTableName);
			fk.setForeignColumnName(foreignColumnName);
			fks.put(columnName, fk);
		}
		results.close();
		statement.close();
		return resultMap;
	}

	protected boolean isPrimaryKey(Map<String, List<String>> keysByTables, String tableName, String columnName) {
		if (!keysByTables.containsKey(tableName))
			return false;
		List<String> pks = keysByTables.get(tableName);
		if (pks.contains(columnName))
			return true;
		return false;
	}

	protected ObjectRelation getForeignKey(Map<String, Map<String,ObjectRelation>> keysByTables, String tableName, String columnName) {
		if (!keysByTables.containsKey(tableName))
			return null;
		Map<String,ObjectRelation> pks = keysByTables.get(tableName);
		if (pks.containsKey(columnName))
			return pks.get(columnName);
		return null;
	}

	protected List<DataObject> getTables() throws DatabaseException {
		Connection connection = null;
		try {
			List<String> tableNames = getTableNames();

			List<DataObject> tables = new LinkedList<DataObject>();
			DataField dbf = null;

			connection = createConnection(databaseConfig);

			Map<String, List<String>> pksMap = getPks(connection);
			Map<String, Map<String, ObjectRelation>> fksMap = getFks(connection);

			DatabaseMetaData metaData = connection.getMetaData();

			for (String name : tableNames) {
				DataObject table = new DataObject();
				table.setTableName(name);

				ResultSet results = metaData.getColumns(null, null, table.getTableName(), "");

				while (results.next()) {
					dbf = new DataField();
					dbf.setColumnName(results.getString("COLUMN_NAME"));
					dbf.setNullable( isNullable(results.getInt("NULLABLE")) );
					dbf.setPrecision(results.getInt("COLUMN_SIZE"));
					dbf.setScale(results.getInt("DECIMAL_DIGITS"));
					dbf.setSqlType(
						results.getString("TYPE_NAME")
					);
					ObjectRelation foreignKey = getForeignKey(fksMap, table.getTableName(), dbf.getColumnName());
					if (foreignKey != null) {
						dbf.setForeignKey(true);
						dbf.setForeignColumnName(foreignKey.getForeignColumnName());
						dbf.setForeignTableName(foreignKey.getForeignTableName());
					}
					dbf.setPrimaryKey(isPrimaryKey(pksMap, table.getTableName(), dbf.getColumnName()));
					table.addField(dbf);
				}
				results.close();
				Collections.sort(table.getFields(), new Comparator<DataField>() {
					/**
					 * Order fields according to this order (primary keys, normal fields, foreign keys)
					 * @param dataField1
					 * @param dataField2
					 * @return
					 */
					public int compare(DataField dataField1, DataField dataField2) {
						if (dataField1.isPrimaryKey()) {
							if (dataField2.isPrimaryKey()) {
								return compareNames(dataField1, dataField2);
							}
							return -1;
						}
						if (dataField1.isForeignKey()) {
							if (dataField2.isForeignKey()) {
								return compareNames(dataField1, dataField2);
							}
						}
						if (dataField2.isPrimaryKey()) {
							return 1;
						}
						if (dataField2.isForeignKey()) {
							return -1;
						}
						return compareNames(dataField1, dataField2);
					}

					/**
					 * @param dataField1
					 * @param dataField2
					 * @return
					 */
					protected int compareNames(DataField dataField1, DataField dataField2) {
						return dataField1.getColumnName().compareTo(dataField2.getColumnName());
					}
				});
				tables.add(table);
			}
			return tables;
		} catch (SQLException e) {
			throw new DatabaseException("Can't retreive table information", e);
		} finally {
			closeConnection(connection);
		}
	}

	/**
	 *
	 * @param pConfig
	 * @throws DatabaseException
	 * @throws InitException
	 */
	public List<DataObject> extractData() throws DatabaseException, InitException {
		return extractData(true);
	}

	/**
	 *
	 * @param pConfig
	 * @throws DatabaseException
	 * @throws InitException
	 */
	public List<DataObject> extractData(boolean testConnection) throws DatabaseException, InitException {
		if (testConnection) {
			DatabaseManager.testConnection(databaseConfig);
		}
		List<DataObject> objects = getTables();
		analyseDataSet(objects);
		logger.info("Tables extracted: " + objects.size());
		return objects;
	}

	protected void analyseDataSet(List<DataObject> objects) {
		for (DataObject object : objects) {
			object.setClassName(SQLDataConverter.convertTableNameToJavaClass(object.getTableName()));
			// TODO Si une table ne contient que des FK, c'est une table d'association
			for (DataField field : object.getFields()) {
				if (field.getPropertyName() != null) {
					continue ;
				}
				if (field.isForeignKey()) {
					DataObject refObject = findRefObject(objects, field.getForeignTableName());
					field.setPropertyName(SQLDataConverter.convertColumnNameToJavaField(refObject.getTableName()));
					field.setPropertyType(SQLDataConverter.convertTableNameToJavaClass(refObject.getTableName()));
					refObject.setClassName(SQLDataConverter.convertTableNameToJavaClass(refObject.getTableName()));
					field.setForeignObject(refObject);
					DataField newField = new DataField();
					newField.setForeignKey(true);
					newField.setForeignList(true);
					newField.setColumnName(field.getColumnName());
					newField.setPropertyName( WordUtils.uncapitalize(object.getClassName()+'s') );
					newField.setPropertyType( object.getClassName() );
					refObject.addField(newField);
				} else {
					field.setPropertyName(SQLDataConverter.convertColumnNameToJavaField(field.getColumnName()));
					field.setPropertyType(SQLDataConverter.getJavaTypeForSqlType(field));
				}
			}
		}
	}

    protected DataObject findRefObject(List<DataObject> objects, String tableName)
	{
    	for (DataObject object : objects) {
			if (object.getTableName().equals(tableName))
				return object;
		}
		return null;
	}
}
