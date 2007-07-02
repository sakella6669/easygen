package org.easygen.ui.wizards.validator;

import org.easygen.ui.wizards.pages.ICommonPage;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Mektoub
 * Created on 20 mars 07
 *
 */
public class ModifyListener implements Listener
{
    private ICommonPage wizardPage;
	/**
     * @param pWizardPage
     */
    public ModifyListener(ICommonPage pWizardPage)
    {
	    super();
	    this.wizardPage = pWizardPage;
    }
	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event pEvent)
	{
		wizardPage.setPageComplete(wizardPage.validatePage());
	}
}
