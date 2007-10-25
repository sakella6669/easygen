package org.easygen.ui.modules.hibernate;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.easygen.core.generators.hibernate.HibernateModuleConfig;
import org.easygen.ui.modules.Module;
import org.easygen.ui.modules.ModuleManager;
import org.easygen.ui.modules.common.pages.DatabaseConfigurationPage;
import org.easygen.ui.modules.hibernate.pages.FieldConfigurationPage;
import org.easygen.ui.modules.hibernate.pages.HibernateConfigPage;
import org.easygen.ui.modules.hibernate.pages.TableSelectorPage;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author Mektoub Created on 23 mars 07
 * 
 */
public class HibernateDataModule implements Module {

	/**
	 * @see org.easygen.ui.modules.Module#getNature()
	 */
	public String getNature() {
		return HibernateModuleConfig.NATURE;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getKind()
	 */
	public String getKind() {
		return ModuleManager.DATA_MODULE_KIND;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getConfigClass()
	 */
	public Class<? extends ModuleConfig> getConfigClass() {
		return HibernateModuleConfig.class;
	}

	/**
	 * @see org.easygen.ui.modules.Module#getPages()
	 */
	public IWizardPage[] getPages() throws InitException {
		return new IWizardPage[] {
			new HibernateConfigPage(getNature(), getConfigClass()),
			new DatabaseConfigurationPage(),
			new TableSelectorPage(),
			new FieldConfigurationPage()
		};
	}
}
