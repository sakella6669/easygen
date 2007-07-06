package org.easygen.ui.modules.springservice.pages;

import org.easygen.core.config.ModuleConfig;
import org.easygen.ui.wizards.pages.BasicModulePage;

/**
 * 
 */
public class SpringServiceConfigPage extends BasicModulePage {

	/**
	 * @param pageName
	 * @param title
	 * @param pNature
	 */
	public SpringServiceConfigPage(String nature, Class<? extends ModuleConfig> configClass) {
		super("springServiceConfigPage", nature, configClass);
	}
}
