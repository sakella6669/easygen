package org.easygen.ui.wizards.pages;

import org.easygen.core.config.ProjectConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.SWTUtils;
import org.easygen.ui.util.WidgetUtils;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * @author eveno
 * Created on 2 mars 07
 */
public class ConfigureModulePage extends CommonPage
{
	public static final String NAME = "configureModulePage";

	protected Combo dataLayerTypeField;
	protected Button serviceLayerButton;
	protected Combo serviceLayerTypeField;
	protected Button viewLayerButton;
	protected Combo viewLayerTypeField;

	private String[] dataLayers;
	private String[] serviceLayers;
	private String[] viewLayers;

	/**
	 * @param parent
	 * @param pStyle
	 */
	public ConfigureModulePage(String[] pDataLayers, String[] pServiceLayers, String[] pViewLayers)
	{
		super(NAME, Localization.get("easygen.title.page.configureModule"));
		dataLayers = pDataLayers;
		serviceLayers = pServiceLayers;
		viewLayers = pViewLayers;
	}
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
    public void initControl(Composite mainComposite)
    {
		Control[] dataLayer = createChooseLayerGroup(
			mainComposite,
			Localization.get("easygen.title.page.configureModule.layer.data"),
			dataLayers,
			false
		);
		dataLayerTypeField = (Combo) dataLayer[0];

		Control[] serviceLayer = createChooseLayerGroup(
			mainComposite,
			Localization.get("easygen.title.page.configureModule.layer.service"),
			serviceLayers,
			true
		);
		serviceLayerButton = (Button) serviceLayer[0];
		serviceLayerTypeField = (Combo) serviceLayer[1];
		if (serviceLayers.length == 0)
		{
			serviceLayerButton.setEnabled(false);
			serviceLayerButton.setSelection(false);
			serviceLayerTypeField.setEnabled(false);
		}

		Control[] viewLayer = createChooseLayerGroup(
			mainComposite,
			Localization.get("easygen.title.page.configureModule.layer.view"),
			viewLayers,
			true
		);
		viewLayerButton = (Button) viewLayer[0];
		viewLayerTypeField = (Combo) viewLayer[1];
		if (viewLayers.length == 0)
		{
			viewLayerButton.setEnabled(false);
			viewLayerButton.setSelection(false);
			viewLayerTypeField.setEnabled(false);
		}

		((Button)serviceLayer[0]).addSelectionListener(
			new CheckSelectedListener(new Control[] {
					serviceLayerTypeField, viewLayerButton, viewLayerTypeField
				},
			true)
		);

		((Button)viewLayer[0]).addSelectionListener(
			new CheckSelectedListener(new Control[] {
					viewLayerTypeField
				},
			true)
		);
	}

	/**
     * This method initializes chosenDataLayer
     *
     */
    protected Control[] createChooseLayerGroup(Composite pParent, String moduleName, String[] layers, boolean canBeEnabled)    {
    	Group layerGroup = WidgetUtils.createGroup(pParent, moduleName, 2);

    	Button layerButton = null;
    	if (canBeEnabled)
    	{
			layerButton = WidgetUtils.createCheckBox(
				layerGroup,
				Localization.get("easygen.label.page.configureModule.layer.enabled"),
				true
			);
			WidgetUtils.createHorizontalSpan(layerButton, 2);
    	}

		final Combo combo = WidgetUtils.createLabelComboPair(
			layerGroup,
			Localization.get("easygen.label.page.configureModule.layer.type"),
			layers
		);

		if (layerButton == null)
			return new Control[] { combo };
		return new Control[] { layerButton, combo };
    }
	/**
     * @see org.easygen.ui.wizards.pages.CommonPage#validatePage()
     */
    @Override
	public boolean validatePage()
    {
    	if (SWTUtils.isEmpty(dataLayerTypeField))
    			return false;
    	if (serviceLayerButton.isEnabled() && SWTUtils.isSelected(serviceLayerButton) && SWTUtils.isEmpty(serviceLayerTypeField))
			return false;
    	if (viewLayerButton.isEnabled() && SWTUtils.isSelected(viewLayerButton) && SWTUtils.isEmpty(viewLayerTypeField))
			return false;

	    return true;
    }
    /**
     * @see org.easygen.ui.wizards.pages.CommonPage#updateConfig(org.easygen.core.config.ProjectConfig)
     */
	@Override
	public void updateConfig(ProjectConfig pProjectConfig)
    {
	    String dataModuleName = dataLayerTypeField.getText();
	    pProjectConfig.setDataModuleNature(dataModuleName);
	    if (SWTUtils.isSelected(serviceLayerButton))
	    {
		    String serviceModuleName = serviceLayerTypeField.getText();
		    pProjectConfig.setServiceModuleNature(serviceModuleName);
	    }
	    if (SWTUtils.isSelected(viewLayerButton))
	    {
		    String viewModuleName = viewLayerTypeField.getText();
		    pProjectConfig.setViewModuleNature(viewModuleName);
	    }
    }
}
