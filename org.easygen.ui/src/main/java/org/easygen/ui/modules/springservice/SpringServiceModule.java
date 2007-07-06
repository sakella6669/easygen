package org.easygen.ui.modules.springservice;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.generators.springservice.SpringServiceModuleConfig;
import org.easygen.ui.modules.Module;
import org.easygen.ui.modules.springservice.pages.SpringServiceConfigPage;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * 
 */
public class SpringServiceModule implements Module {

	/**
	 * @see org.easygen.ui.modules.Module#getConfigClass()
	 */
	public Class<? extends ModuleConfig> getConfigClass() {
		return SpringServiceModuleConfig.class;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getNature()
	 */
	public String getNature() {
		return SpringServiceModuleConfig.NATURE;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getPages()
	 */
	public IWizardPage[] getPages() throws InitException {
		return new IWizardPage[] { new SpringServiceConfigPage(getNature(), getConfigClass()) };
	}
}
