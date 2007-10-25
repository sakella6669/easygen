package org.easygen.ui.modules.struts2;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.generators.struts2.Struts2ModuleConfig;
import org.easygen.ui.modules.Module;
import org.easygen.ui.modules.ModuleManager;
import org.easygen.ui.modules.struts2.pages.Struts2ConfigPage;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * 
 */
public class Struts2ViewModule implements Module {

	/**
	 * @see org.easygen.ui.modules.Module#getConfigClass()
	 */
	public Class<? extends ModuleConfig> getConfigClass() {
		return Struts2ModuleConfig.class;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getKind()
	 */
	public String getKind() {
		return ModuleManager.VIEW_MODULE_KIND;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getNature()
	 */
	public String getNature() {
		return Struts2ModuleConfig.NATURE;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getPages()
	 */
	public IWizardPage[] getPages() throws InitException {
		return new IWizardPage[] { new Struts2ConfigPage(getNature(), getConfigClass()) };
	}

}
