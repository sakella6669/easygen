package org.easygen.core.db;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.easygen.core.config.DataField;

/**
 * @author eveno
 * Created on 16 nov. 06
 *
 */
public class SQLDataConverter {
	
	private static final String ROOT_SQL_TYPE_KEY = "sql.type.";
	private static Logger logger = Logger.getLogger(SQLDataConverter.class);
	private static Properties properties = new Properties();

	static {
		try {
			URL propertiesURL = Thread.currentThread().getContextClassLoader().getResource(
					"database/" + "database.properties");
			properties.load(propertiesURL.openStream());
		} catch (IOException e) {
			logger.error("Erreur lors du chargement de propriétés", e);
		}
	}

	public static String getJavaTypeForSqlType(DataField field) {
		String key = ROOT_SQL_TYPE_KEY;
		if (field.getScale() != 0)
			key += "decimal";
		else
			key += field.getSqlType().toLowerCase();
		key = key.replace(' ', '.');
		String javaType = properties.getProperty(key, "");
		if (javaType.length() == 0)
			LogFactory.getLog(SQLDataConverter.class).warn("SQL Type -> Key not found: " + key);
		return javaType;
	}

	public static String getProperty(String key) {
		return properties.getProperty(key, "");
	}

	public static String convertTableNameToJavaClass(String pName) {
		return convertSqlToJava(pName, true);
	}

	public static String convertColumnNameToJavaField(String pName) {
		return convertSqlToJava(pName, false);
	}

	/**
	 * @param pName
	 * @return
	 */
	private static String convertSqlToJava(String pName, boolean upperFirst) {
		StringBuffer buffer = new StringBuffer();
		boolean nextMaj = false;
		char charToWrite = 0;
		for (int i = 0; i < pName.length(); i++) {
			if (i == 0)
				nextMaj = upperFirst;
			char currentChar = pName.charAt(i);
			if (currentChar == '_') {
				nextMaj = true;
				charToWrite = 0;
			} else if (nextMaj) {
				charToWrite = Character.toUpperCase(currentChar);
				nextMaj = false;
			} else
				charToWrite = Character.toLowerCase(currentChar);

			if (charToWrite != 0)
				buffer.append(charToWrite);
		}
		return buffer.toString();
	}
}
