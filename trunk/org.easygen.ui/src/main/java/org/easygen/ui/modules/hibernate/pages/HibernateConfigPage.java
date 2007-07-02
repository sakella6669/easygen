package org.easygen.ui.modules.hibernate.pages;

import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.hibernate.HibernateModuleConfig;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.SWTUtils;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.BasicModulePage;
import org.easygen.ui.wizards.pages.CheckSelectedListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */
public class HibernateConfigPage extends BasicModulePage {

	private Button useAnnotations;
	
	private Button nativeIdRadio;

	private Button assignedIdRadio;

	private Button sequenceIdRadio;

	private Text sequenceFormatField;

//	private Button sourceLocationRadio;
//
//	private Button specifiedLocationRadio;
//
//	private Text customHbmLocationField;
//
//	private Button chooseLocationButton;

	/**
	 * @param pageName
	 * @param nature
	 * @param configClass
	 */
	public HibernateConfigPage(String nature, Class configClass) {
		super("hibernateConfigPage", nature, configClass);
	}

	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#initControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void initControl(Composite mainComposite) {
		super.initControl(mainComposite);

		createGeneralPropertiesGroup(mainComposite);
		createIdConfigurationGroup(mainComposite);
//		createHbmLocationGroup(mainComposite);

	}

	/**
	 * @param mainComposite
	 */
	protected void createGeneralPropertiesGroup(Composite mainComposite) {
		Group group = WidgetUtils.createGroup(mainComposite, Localization.get("hibernate.title.page.hibernateConfigPage.group"), 2);
		useAnnotations = WidgetUtils.createCheckBox(group, Localization.get("hibernate.label.page.hibernateConfigPage.useAnnotations"), true);
	}

	/**
	 * This method initializes idConfigurationGroup
	 */
	protected void createIdConfigurationGroup(Composite pParent) {
		Group idConfigurationGroup = WidgetUtils.createGroup(pParent, Localization
				.get("hibernate.title.page.tableSelector.idConfiguration"), 2);

		nativeIdRadio = WidgetUtils.createRadioButton(idConfigurationGroup, Localization
				.get("hibernate.label.page.tableSelector.nativeId"));
		WidgetUtils.createHorizontalSpan(nativeIdRadio, 2);
		nativeIdRadio.setSelection(true);

		assignedIdRadio = WidgetUtils.createRadioButton(idConfigurationGroup, Localization
				.get("hibernate.label.page.tableSelector.assignedId"));
		WidgetUtils.createHorizontalSpan(assignedIdRadio, 2);

		sequenceIdRadio = WidgetUtils.createRadioButton(idConfigurationGroup, Localization
				.get("hibernate.label.page.tableSelector.sequenceId"));
		WidgetUtils.createHorizontalSpan(sequenceIdRadio, 2);

		sequenceFormatField = WidgetUtils.createLabelTextPair(idConfigurationGroup, Localization
				.get("hibernate.label.page.tableSelector.sequenceFormat"), Localization
				.get("hibernate.default.page.tableSelector.sequenceFormat"));
		sequenceFormatField.setEnabled(false);

		sequenceIdRadio.addSelectionListener(new CheckSelectedListener(sequenceFormatField, true));
	}

	/**
	 * This method initializes hbmLocationGroup
	 */
	protected void createHbmLocationGroup(Composite pParent) {
//		Group hbmLocationGroup = WidgetUtils.createGroup(pParent, Localization
//				.get("hibernate.title.page.tableSelector.hbmLocation"), 3);
//
//		GridData gridData1 = new GridData();
//		gridData1.horizontalSpan = 3;
//
//		sourceLocationRadio = WidgetUtils.createRadioButton(hbmLocationGroup, Localization
//				.get("hibernate.label.page.tableSelector.srcDir"));
//		WidgetUtils.createHorizontalSpan(sourceLocationRadio, 3);
//		sourceLocationRadio.setSelection(true);
//
//		specifiedLocationRadio = WidgetUtils.createRadioButton(hbmLocationGroup, Localization
//				.get("hibernate.label.page.tableSelector.custom"));
//		WidgetUtils.createHorizontalSpan(specifiedLocationRadio, 3);
//
//		customHbmLocationField = WidgetUtils.createLabelTextPair(hbmLocationGroup, Localization
//				.get("hibernate.label.page.tableSelector.location"), null);
//		customHbmLocationField.setEnabled(false);
//
//		chooseLocationButton = WidgetUtils.createSimpleButton(hbmLocationGroup, Localization
//		        .get("hibernate.label.page.tableSelector.browse"));
//		chooseLocationButton.setEnabled(false);
//
//		specifiedLocationRadio.addSelectionListener(new CheckSelectedListener(new Control[] { customHbmLocationField,
//		        chooseLocationButton }, true));
	}

	/**
	 * @see org.easygen.ui.wizards.pages.BasicModulePage#updateConfig(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void updateConfig(ProjectConfig pProjectConfig) throws Exception {
		super.updateConfig(pProjectConfig);

		HibernateModuleConfig hibernateConfig = (HibernateModuleConfig) pProjectConfig.getDataModuleConfig();
		hibernateConfig.setUseAnnotations(useAnnotations.getSelection());

		if (nativeIdRadio.getSelection())
			hibernateConfig.setDefaultIdConfiguration(HibernateModuleConfig.NATIVE_ID);
		else if (assignedIdRadio.getSelection())
			hibernateConfig.setDefaultIdConfiguration(HibernateModuleConfig.ASSIGNED_ID);
		else if (sequenceIdRadio.getSelection()) {
			hibernateConfig.setDefaultIdConfiguration(HibernateModuleConfig.SEQUENCE_ID);
			hibernateConfig.setSequencePattern(sequenceFormatField.getText());
		}

//		if (sourceLocationRadio.getSelection())
//			hibernateConfig.setHbmDir(HibernateModuleConfig.SOURCE_DIR);
//		else if (specifiedLocationRadio.getSelection()) {
//			hibernateConfig.setHbmDir(HibernateModuleConfig.OTHER_DIR);
//			hibernateConfig.setHbmCustomDir(customHbmLocationField.getText());
//		}
	}

	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#validatePage()
	 */
	@Override
	public boolean validatePage() {
		if (!super.validatePage())
			return false;
		if (sequenceIdRadio.getSelection() && SWTUtils.isEmpty(sequenceFormatField))
			return false;
//		if (specifiedLocationRadio.getSelection() && SWTUtils.isEmpty(customHbmLocationField))
//			return false;
		return true;
	}
}
