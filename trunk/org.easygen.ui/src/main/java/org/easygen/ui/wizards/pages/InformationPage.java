package org.easygen.ui.wizards.pages;

import org.easygen.ui.util.WidgetUtils;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Mektoub
 * Created on 10 mars 07
 *
 */
public class InformationPage extends CommonPage
{
	public static final String NAME = "informationPage";

	private String informationMessage  = null;

	/**
	 * @param pName
	 * @param pTitle
	 */
	public InformationPage(String pTitle, String pInformationMessage)
	{
		super(NAME, pTitle);
		informationMessage   = pInformationMessage;
	}

	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite pMainComposite)
	{
		WidgetUtils.createLabel(pMainComposite, informationMessage);
	}
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#validatePage()
	 */
	@Override
	public boolean validatePage()
	{
		return true;
	}
}
