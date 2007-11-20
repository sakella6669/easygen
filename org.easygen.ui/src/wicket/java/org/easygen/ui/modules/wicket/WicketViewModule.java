package org.easygen.ui.modules.wicket;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.generators.wicket.WicketModuleConfig;
import org.easygen.ui.modules.Module;
import org.easygen.ui.modules.ModuleManager;
import org.easygen.ui.modules.wicket.pages.WicketConfigPage;
import org.eclipse.jface.wizard.IWizardPage;

public class WicketViewModule implements Module {

	public Class<? extends ModuleConfig> getConfigClass() {
		return WicketModuleConfig.class;
	}

	public String getKind() {
		return ModuleManager.VIEW_MODULE_KIND;
	}

	public String getNature() {
		return WicketModuleConfig.NATURE;
	}

	public IWizardPage[] getPages() throws InitException {
		return new IWizardPage[] { new WicketConfigPage(getNature(), getConfigClass()) };
	}
}
