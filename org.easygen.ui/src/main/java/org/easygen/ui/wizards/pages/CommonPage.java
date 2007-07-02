package org.easygen.ui.wizards.pages;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.easygen.core.config.ProjectConfig;
import org.easygen.ui.wizards.validator.Validator;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Abstract implementation for each wizard page.
 */
public abstract class CommonPage extends WizardPage implements ICommonPage
{
	protected Log logger = LogFactory.getLog(getClass());
	protected Validator validator = null;

    /**
     * @param pName
     * @param pTitle
     */
    protected CommonPage(String pName, String pTitle)
    {
        super(pName, pTitle, null);
        validator = new Validator(this);
    }

	/**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite pParent)
    {
		try
        {
	        Composite mainComposite = createMainComposite(pParent);
	        initControl(mainComposite);
	        setControl(mainComposite);
	        setPageComplete(validatePage());
        }
        catch (RuntimeException e)
        {
	        logger.error("Création du contenu de la page impossible", e);
        }
    }
	/**
     * @param pParent
     * @return
     */
    protected Composite createMainComposite(Composite pParent)
    {
	    Composite mainComposite = new Composite(pParent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	    return mainComposite;
    }
    /**
     * @see org.easygen.ui.wizards.pages.ICommonPage#initControl(org.eclipse.swt.widgets.Composite)
     */
    public abstract void initControl(Composite mainComposite);
    /**
	 * @see org.easygen.ui.wizards.pages.ICommonPage#init(org.easygen.core.config.ProjectConfig)
	 */
	public void init(ProjectConfig pProjectConfig)
	{

	}
	/**
     * @see org.easygen.ui.wizards.pages.ICommonPage#updateConfig(ProjectConfig)
     */
    public void updateConfig(ProjectConfig pProjectConfig) throws Exception
    {
    }
    /**
     * @param c
     */
    protected void addFieldToValidate(String pKeyName, Control pControl, short pFieldType)
    {
    	validator.addField(pKeyName, pControl, pFieldType);
    }
	/**
     * @see org.easygen.ui.wizards.pages.ICommonPage#validatePage()
     */
    public boolean validatePage()
    {
	    return validator.validateFields();
    }
}
