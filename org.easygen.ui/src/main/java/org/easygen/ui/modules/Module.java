package org.easygen.ui.modules;

import org.easygen.core.InitException;
import org.easygen.core.config.ModuleConfig;
import org.eclipse.jface.wizard.IWizardPage;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public interface Module {
	
	public String getNature();
	/**
	 * See constants in {@link ModuleManager} for possible values
	 * @return the module kind
	 */
	public String getKind();
	public Class<? extends ModuleConfig> getConfigClass();
	public IWizardPage[] getPages() throws InitException;

}
