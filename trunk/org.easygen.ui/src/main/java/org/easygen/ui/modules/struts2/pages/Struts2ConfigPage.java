package org.easygen.ui.modules.struts2.pages;

import org.easygen.core.config.ModuleConfig;
import org.easygen.ui.wizards.pages.BasicModulePage;

/**
 * 
 */
public class Struts2ConfigPage extends BasicModulePage {

	/**
	 * @param pageName
	 * @param title
	 * @param pNature
	 */
	public Struts2ConfigPage(String nature, Class<? extends ModuleConfig> configClass) {
		super("struts2ConfigPage", nature, configClass);
	}
}