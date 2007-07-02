package org.easygen.core.generators.servicejava;

import org.easygen.core.config.ServiceModuleConfig;
import org.easygen.core.generators.Generator;

/**
 * 
 */
public class ServiceJavaModuleConfig extends ServiceModuleConfig {

	public static final String NATURE = "Service Java";

	/**
	 * 
	 */
	public ServiceJavaModuleConfig() {
		super();
		setNature(NATURE);
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGeneratorClass()
	 */
	@Override
	public Generator getGenerator() {
		return new ServiceJavaGenerator();
	}
}
