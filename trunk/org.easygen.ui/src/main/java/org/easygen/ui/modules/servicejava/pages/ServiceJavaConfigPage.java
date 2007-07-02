package org.easygen.ui.modules.servicejava.pages;

import org.easygen.ui.wizards.pages.BasicModulePage;

/**
 * 
 */
public class ServiceJavaConfigPage extends BasicModulePage {

	/**
	 * @param pageName
	 * @param title
	 * @param pNature
	 */
	public ServiceJavaConfigPage(String nature, Class configClass) {
		super("serviceJavaConfigPage", nature, configClass);
	}
}
