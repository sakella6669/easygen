package org.easygen.core.generators.springservice;

import org.easygen.core.config.ServiceModuleConfig;
import org.easygen.core.generators.Generator;

public class SpringServiceModuleConfig extends ServiceModuleConfig {

	public static final String NATURE = "Spring";

	private boolean useSimpleServices = false;

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

	public boolean isUseSimpleServices() {
		return this.useSimpleServices;
	}

	public void setUseSimpleServices(boolean useSimpleServices) {
		this.useSimpleServices = useSimpleServices;
	}
}
