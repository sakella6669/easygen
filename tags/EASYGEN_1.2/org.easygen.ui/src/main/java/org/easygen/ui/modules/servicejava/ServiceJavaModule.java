package org.easygen.ui.modules.servicejava;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.generators.servicejava.ServiceJavaModuleConfig;
import org.easygen.ui.modules.Module;
import org.easygen.ui.modules.servicejava.pages.ServiceJavaConfigPage;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * 
 */
public class ServiceJavaModule implements Module {

	/**
	 * @see org.easygen.ui.modules.Module#getConfigClass()
	 */
	public Class<? extends ModuleConfig> getConfigClass() {
		return ServiceJavaModuleConfig.class;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getNature()
	 */
	public String getNature() {
		return ServiceJavaModuleConfig.NATURE;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getPages()
	 */
	public IWizardPage[] getPages() throws InitException {
		return new IWizardPage[] { new ServiceJavaConfigPage(getNature(), getConfigClass()) };
	}
}
