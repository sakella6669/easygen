package org.easygen.ui.modules;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public interface Module
{
	public String getNature();
	public Class<? extends ModuleConfig> getConfigClass();
	public IWizardPage[] getPages() throws InitException;
}
