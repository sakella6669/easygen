package org.easygen.core.generators.project;

import org.easygen.core.config.ProjectModuleConfig;
import org.easygen.core.generators.Generator;

/**
 * 
 */
public class EclipseProjectConfig extends ProjectModuleConfig {

	public final static String WTP_NATURE = "wtp";
	public final static String JAVA_NATURE = "java";

	/**
	 * 
	 */
	public EclipseProjectConfig() {
		super();
		setNature(JAVA_NATURE);
	}
	
	/**
	 * @see org.easygen.core.config.ModuleConfig#getGeneratorClass()
	 */
	@Override
	public Generator getGenerator() {
		return new EclipseProjectGenerator();
	}
}
