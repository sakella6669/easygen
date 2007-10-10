package org.easygen.core.generators.struts2;

import org.easygen.core.config.ViewModuleConfig;
import org.easygen.core.generators.Generator;

/**
 * 
 */
public class Struts2ModuleConfig extends ViewModuleConfig {

	public static final String NATURE = "Struts2";
	
	/**
	 * 
	 */
	public Struts2ModuleConfig() {
		super();
		setNature(NATURE);
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGenerator()
	 */
	@Override
	public Generator getGenerator() {
		return new Struts2Generator();
	}

}
