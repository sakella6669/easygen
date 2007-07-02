package org.easygen.ui.generators.project;

import org.easygen.core.config.ProjectModuleConfig;
import org.easygen.core.generators.Generator;

/**
 * 
 */
public class EclipseProjectUIConfig extends ProjectModuleConfig {

	public final static String WTP_NATURE = "wtp";
	public final static String JAVA_NATURE = "java";

	/**
	 * 
	 */
	public EclipseProjectUIConfig() {
		super();
		setNature(JAVA_NATURE);
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGenerator()
	 */
	@Override
	public Generator getGenerator() {
		return new EclipseProjectUIGenerator();
	}

}
