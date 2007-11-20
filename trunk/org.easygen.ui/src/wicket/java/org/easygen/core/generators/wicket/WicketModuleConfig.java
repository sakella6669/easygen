package org.easygen.core.generators.wicket;

import org.easygen.core.config.ViewModuleConfig;
import org.easygen.core.generators.Generator;

public class WicketModuleConfig extends ViewModuleConfig {

	public static final String NATURE = "Wicket";

	private NavigationConfig navigationConfig;

	public WicketModuleConfig() {
		super();
		setNature(NATURE);
	}

	public NavigationConfig getNavigationConfig() {
		return this.navigationConfig;
	}

	public void setNavigationConfig(NavigationConfig navigationConfig) {
		this.navigationConfig = navigationConfig;
	}

	/**
	 * @see org.easygen.core.config.ModuleConfig#getGenerator()
	 */
	@Override
	public Generator getGenerator() {
		return new WicketGenerator();
	}
}
