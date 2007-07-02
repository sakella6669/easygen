package org.easygen.core.generators.hibernate;

import org.easygen.core.config.DataModuleConfig;
import org.easygen.core.generators.Generator;

/**
 * @author Manuel
 */
public class HibernateModuleConfig extends DataModuleConfig {

	public static final String NATURE = "Hibernate";
	public static final short NATIVE_ID = 0;
	public static final short ASSIGNED_ID = 1;
	public static final short SEQUENCE_ID = 2;
	public static final short SOURCE_DIR = 0;
	public static final short OTHER_DIR = 0;

	private boolean useAnnotations = true;
	private short defaultIdConfiguration = NATIVE_ID;
	private String sequencePattern = null;
	private short hbmDir = SOURCE_DIR;
	private String hbmCustomDir = null;

	/**
	 * 
	 */
	public HibernateModuleConfig() {
		super();
		setNature(NATURE);
	}

	/**
	 * @return the useAnnotations
	 */
	public boolean isUseAnnotations() {
		return useAnnotations;
	}

	/**
	 * @param useAnnotations the useAnnotations to set
	 */
	public void setUseAnnotations(boolean useAnnotations) {
		this.useAnnotations = useAnnotations;
	}

	/**
	 * @return the defaultIdConfiguration
	 */
	public short getDefaultIdConfiguration() {
		return defaultIdConfiguration;
	}

	/**
	 * @param defaultIdConfiguration
	 *            the defaultIdConfiguration to set
	 */
	public void setDefaultIdConfiguration(short defaultIdConfiguration) {
		this.defaultIdConfiguration = defaultIdConfiguration;
	}

	/**
	 * @return the defaultSequencePattern
	 */
	public String getSequencePattern() {
		return sequencePattern;
	}

	/**
	 * @param defaultSequencePattern
	 *            the defaultSequencePattern to set
	 */
	public void setSequencePattern(String defaultSequencePattern) {
		this.sequencePattern = defaultSequencePattern.trim();
	}

	/**
	 * @return the hbmDir
	 */
	public short getHbmDir() {
		return hbmDir;
	}

	/**
	 * @param hbmDir
	 *            the hbmDir to set
	 */
	public void setHbmDir(short hbmDir) {
		this.hbmDir = hbmDir;
	}

	/**
	 * @return the hbmCustomDir
	 */
	public String getHbmCustomDir() {
		return hbmCustomDir;
	}

	/**
	 * @param hbmCustomDir
	 *            the hbmCustomDir to set
	 */
	public void setHbmCustomDir(String hbmCustomDir) {
		this.hbmCustomDir = hbmCustomDir.trim();
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGeneratorClass()
	 */
	@Override
	public Generator getGenerator() {
		return new HibernateGenerator();
	}
}
