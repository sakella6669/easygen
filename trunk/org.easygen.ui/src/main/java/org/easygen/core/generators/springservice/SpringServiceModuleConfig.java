package org.easygen.core.generators.springservice;

import org.easygen.core.config.ServiceModuleConfig;
import org.easygen.core.generators.Generator;

/**
 * 
 */
public class SpringServiceModuleConfig extends ServiceModuleConfig {

	public static final String NATURE = "Spring";

	/**
	 * 
	 */
	public SpringServiceModuleConfig() {
		super();
		setNature(NATURE);
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGeneratorClass()
	 */
	@Override
	public Generator getGenerator() {
		return new SpringServiceGenerator();
	}
}
