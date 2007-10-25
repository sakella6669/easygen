package org.easygen.core.generators.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.easygen.core.config.DataObject;

/**
 * @author eveno Created on 2 nov. 06
 */
public class HibernateMapping extends DataObject {

//	private String packageName = null;
	private String primaryKeyGeneratorName = null;
	private Map<String, String> primaryKeyGeneratorParams = null;

	/**
	 * 
	 */
	public HibernateMapping(HibernateModuleConfig dataModuleConfig, DataObject objectSource/*, String packageName*/) {
		super();
		setSelected(objectSource.isSelected());
//		setPackageName(packageName);
		setClassName(objectSource.getClassName());
		setTableName(objectSource.getTableName());
		setFields(objectSource.getFields());
		setUseCache(objectSource.isUseCache());
		
		switch (dataModuleConfig.getDefaultIdConfiguration()) {
			case HibernateModuleConfig.NATIVE_ID:
				this.primaryKeyGeneratorName = "native";
				break;
			case HibernateModuleConfig.ASSIGNED_ID:
				this.primaryKeyGeneratorName = "assigned";
				break;
			case HibernateModuleConfig.SEQUENCE_ID:
				this.primaryKeyGeneratorName = "sequence";
				this.primaryKeyGeneratorParams = new HashMap<String, String>();
				primaryKeyGeneratorParams.put("sequence", replaceVars(dataModuleConfig.getSequencePattern()));
				break;
	
			default:
				break;
		}
		
	}
	
	/**
	 * @param sequencePattern
	 * @return
	 */
	private String replaceVars(String sequencePattern) {
		return sequencePattern.replace("{TABLE_NAME}", getTableName().toUpperCase());
	}
//
//	/**
//	 * @return the packageName
//	 */
//	public String getPackageName() {
//		return packageName;
//	}
//
//	/**
//	 * @param pPackageName
//	 *            the packageName to set
//	 */
//	public void setPackageName(String pPackageName) {
//		packageName = pPackageName;
//	}
//
//	/**
//	 * @return the fullClassName
//	 */
//	public String getFullClassName() {
//		return packageName + '.' + getClassName();
//	}

	/**
	 * @return the primaryKeyGeneratorName
	 */
	public String getPrimaryKeyGeneratorName() {
		return primaryKeyGeneratorName;
	}

	/**
	 * @param pPrimaryKeyGeneratorName
	 *            the primaryKeyGeneratorName to set
	 */
	public void setPrimaryKeyGeneratorName(String pPrimaryKeyGeneratorName) {
		primaryKeyGeneratorName = pPrimaryKeyGeneratorName;
	}

	/**
	 * @return the primaryKeyGeneratorParams
	 */
	public Map<String, String> getPrimaryKeyGeneratorParams() {
		return primaryKeyGeneratorParams;
	}

	/**
	 * @param pPrimaryKeyGeneratorParams
	 *            the primaryKeyGeneratorParams to set
	 */
	public void setPrimaryKeyGeneratorParams(Map<String, String> pPrimaryKeyGeneratorParams) {
		primaryKeyGeneratorParams = pPrimaryKeyGeneratorParams;
	}
}
