package org.easygen.ui.wizards.pages;

import org.easygen.core.config.ProjectConfig;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Mektoub
 * Created on 11 mars 07
 *
 */
public interface ICommonPage
{
	public void initControl(Composite mainComposite);

	public void init(ProjectConfig pProjectConfig);// throws Exception;

	public void setPageComplete(boolean status);

	public boolean validatePage();

	public void updateConfig(ProjectConfig pProjectConfig) throws Exception;
}
