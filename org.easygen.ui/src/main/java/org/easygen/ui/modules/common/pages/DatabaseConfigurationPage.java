package org.easygen.ui.modules.common.pages;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.easygen.core.config.DatabaseConfig;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.db.DatabaseException;
import org.easygen.core.db.DatabaseManager;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.util.SWTUtils;
import org.easygen.ui.util.WidgetUtils;
import org.easygen.ui.wizards.pages.CommonPage;
import org.easygen.ui.wizards.validator.Validator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Mektoub
 * Created on 23 mars 07
 */
public class DatabaseConfigurationPage extends CommonPage implements SelectionListener
{
	public static final String NAME = "databaseConfigurationPage";

	protected static final String[] JAR_FILTER = { "*.jar", "*.zip" };

	private Combo databaseType;
	private Combo databaseDriver;

	private Text databaseUrl;
	private Text databasePort;
	private Text databaseInstance;
	private Text databaseUser;
	private Text databasePassword;

	private Button libraryChooserButton;
	private Text chosenLibraryField;

	private Button testConnectionButton;
	private Label connectionResultLabel;

	private boolean connectionTestSuccessfull = false;

	private Label connectionResultDetailLabel;

	/**
	 * @param pName
	 * @param pTitle
	 */
	public DatabaseConfigurationPage()
	{
		super(NAME, Localization.get("easygen.title.page.databaseConfiguration"));
	}
	/**
	 * @see org.easygen.ui.wizards.pages.CommonPage#initControl(org.eclipse.swt.widgets.Composite)
	 */
    @Override
	public void initControl(Composite pMainComposite)
	{
		createPropertiesGroup(pMainComposite);

		createLibraryGroup(pMainComposite);

		createTestConnectionGroup(pMainComposite);

		setDatabaseDefaults(DatabaseManager.DATABASE_TYPES[0]);
	}
	/**
     * @param pMainComposite
     */
    protected void createPropertiesGroup(Composite pMainComposite)
    {
	    Group propertiesGroup = WidgetUtils.createGroup(pMainComposite, Localization.get("easygen.title.page.databaseConfiguration.connectionoptions"), 2);

		databaseType = WidgetUtils.createLabelComboPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.type"), DatabaseManager.DATABASE_TYPES);
		addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.type"), databaseType, Validator.FREE);

		databaseDriver = WidgetUtils.createLabelComboPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.driver"), new String[0]);
		addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.driver"), databaseDriver, Validator.FREE);

		databaseType.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent pEvent)
			{
				String type = databaseType.getText();
				setDatabaseDefaults(type);
			}
		});

		databaseUrl = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.url"), null);
		addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.url"), databaseUrl, Validator.FREE);

		databasePort = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.port"), null);
		addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.port"), databasePort, Validator.NUMERIC);

		databaseInstance = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.instance"), null);
		addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.instance"), databaseInstance, Validator.FREE);

		databaseUser = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.user.name"), null);
		addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.user.name"), databaseUser, Validator.FREE);

		databasePassword = WidgetUtils.createLabelTextPair(propertiesGroup, Localization.get("easygen.label.page.databaseConfiguration.user.password"), null);
    }
	/**
     * @param pMainComposite
     */
    protected void createLibraryGroup(Composite pMainComposite)
    {
    	Group libraryChooserGroup = WidgetUtils.createGroup(pMainComposite, Localization.get("easygen.title.page.databaseConfiguration.libraryChooser"), 2);

    	chosenLibraryField = WidgetUtils.createLabelTextPair(libraryChooserGroup, Localization.get("easygen.label.page.databaseConfiguration.library.jar"), "");
    	chosenLibraryField.setEnabled(false);
    	addFieldToValidate(Localization.get("easygen.label.page.databaseConfiguration.library.jar"), chosenLibraryField, Validator.FREE);

    	libraryChooserButton = WidgetUtils.createSimpleButton(libraryChooserGroup, Localization.get("easygen.label.page.databaseConfiguration.library.choose.jar"));
    	libraryChooserButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				String filePath = SWTUtils.createFileDialog(getShell(), JAR_FILTER);
				if (filePath == null)
					return;
				try {
					chosenLibraryField.setText(
						new File(filePath).toURI().toURL().toString()
					);
				} catch (MalformedURLException e) {
					logger.warn("Can't convert file to url: "+filePath);
					chosenLibraryField.setText(filePath);
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {}
		});
    	GridData gridData = new GridData();
    	gridData.horizontalSpan = 2;
    	gridData.horizontalAlignment = SWT.RIGHT;
    	libraryChooserButton.setLayoutData(gridData);
    }
	/**
     * @param pMainComposite
     */
    protected void createTestConnectionGroup(Composite pMainComposite)
    {
    	Group testConnectionGroup = WidgetUtils.createGroup(pMainComposite, Localization.get("easygen.title.page.databaseConfiguration.connexiontest"), 2);

    	WidgetUtils.createLabel(testConnectionGroup, Localization.get("easygen.label.page.databaseConfiguration.connectiontest.result"));
    	connectionResultLabel = WidgetUtils.createLabel(testConnectionGroup, Localization.get("easygen.message.page.databaseConfiguration.connectiontest.noresult"));
    	connectionResultLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

    	connectionResultDetailLabel = WidgetUtils.createLabel(testConnectionGroup, "");
    	connectionResultDetailLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
    	WidgetUtils.createHorizontalSpan(connectionResultDetailLabel, 2);

    	testConnectionButton = WidgetUtils.createSimpleButton(testConnectionGroup, Localization.get("easygen.label.page.databaseConfiguration.connectiontest"));
    	testConnectionButton.addSelectionListener(this);
    	GridData gridData = new GridData();
    	gridData.horizontalSpan = 2;
    	gridData.horizontalAlignment = SWT.RIGHT;
    	testConnectionButton.setLayoutData(gridData);
    	testConnectionButton.setEnabled(false);
    }
	/**
     * @param pString
     */
    protected void setDatabaseDefaults(String pDatabaseType)
    {
		String[] drivers = DatabaseManager.getDriverList(pDatabaseType);
		databaseDriver.setItems(drivers);
		databaseDriver.select(0);

		URL jarUrl = DatabaseManager.getDriverJarUrl(pDatabaseType);
		if (jarUrl != null) {
			chosenLibraryField.setText(jarUrl.toString());
		} else {
			logger.warn("Driver Jar file not found: "+jarUrl.getPath());
		}
		databaseUrl.setText(DatabaseManager.getDatabaseProperty(pDatabaseType, "host.default"));
		databasePort.setText(DatabaseManager.getDatabaseProperty(pDatabaseType, "port.default"));
		databaseUser.setText(DatabaseManager.getDatabaseProperty(pDatabaseType, "user.default"));
		databaseInstance.setText(DatabaseManager.getDatabaseProperty(pDatabaseType, "instance.default"));
    }
	/**
     * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetSelected(SelectionEvent pEvent)
    {
        connectionResultLabel.setText(Localization.get("easygen.message.page.databaseConfiguration.connectiontest.running"));

        try {
			DatabaseManager.testConnection(getDbConfig());
            connectionResultLabel.setText(Localization.get("easygen.message.page.databaseConfiguration.connectiontest.success"));
            logger.debug("Test successfull !!");
            connectionResultDetailLabel.setText("");
            connectionTestSuccessfull = true;
		} catch (DatabaseException e) {
        	connectionResultLabel.setText(Localization.get("easygen.message.page.databaseConfiguration.connectiontest.failure"));
        	connectionResultDetailLabel.setText(e.getMessage());
	        logger.error("Test de connexion à la base impossible");
	        connectionTestSuccessfull = false;
		}
        setPageComplete(validatePage());
    }
	/**
     * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
     */
    public void widgetDefaultSelected(SelectionEvent pEvent) {}
	/**
     * @see org.easygen.ui.wizards.pages.CommonPage#updateConfig(org.easygen.core.config.ProjectConfig)
     */
	@Override
    public void updateConfig(ProjectConfig pProjectConfig) throws Exception
    {
	    super.updateConfig(pProjectConfig);
	    DatabaseConfig dbConfig = getDbConfig();
		pProjectConfig.setDatabaseConfig(dbConfig);
	    DatabaseManager databaseManager = new DatabaseManager(dbConfig);
		pProjectConfig.setObjects(databaseManager.extractData(false));
    }
	/**
     * @return
     */
	protected DatabaseConfig getDbConfig()
    {
	    DatabaseConfig dbConfig = new DatabaseConfig();
	    dbConfig.setDatabaseType(databaseType.getText());
	    dbConfig.setDataBaseDriver(databaseDriver.getText());
	    dbConfig.setHost(databaseUrl.getText());
	    dbConfig.setPort(databasePort.getText());
	    dbConfig.setDatabaseName(databaseInstance.getText());
	    dbConfig.setUsername(databaseUser.getText());
	    dbConfig.setPassword(databasePassword.getText());
	    dbConfig.setJarPath(chosenLibraryField.getText());
	    return dbConfig;
    }
	/**
     * @see org.easygen.ui.wizards.pages.CommonPage#validatePage()
     */
    @Override
    public boolean validatePage()
    {
	    boolean fieldsValidated = super.validatePage();
	    fieldsValidated = (fieldsValidated && SWTUtils.isNotEmpty(chosenLibraryField));

	    if (!fieldsValidated)
	    {
	    	if (testConnectionButton != null)
	    		testConnectionButton.setEnabled(false);
	    	return false;
	    }

	    testConnectionButton.setEnabled(true);

	    if (connectionTestSuccessfull)
	    	return true;
	    return false;
    }
}
