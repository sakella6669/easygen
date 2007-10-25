package org.easygen.core.generators.struts2;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;
import org.easygen.core.generators.springservice.SpringServiceModuleConfig;

/**
 * 
 */
public class Struts2Generator extends AbstractGenerator {

	private static final Logger logger = Logger.getLogger(Struts2Generator.class);
	
	protected static final String MODULE_NAME = "struts2";
	private static final String ACTION_SUFFIX = "Action";
	private static final String IS_SPRING_SERVICE_MODULE = "isSpringServiceModule";
	private static final String IS_SITE_MESH_ENGINE = "isSiteMeshEngine";
	private static final String IS_TILES2_ENGINE = "isTiles2Engine";

	private static final String NAVIGATION_CONFIG_KEY = "navigation";

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#getModuleName()
	 */
	@Override
	protected String getModuleName() {
		return MODULE_NAME;
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#generate(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void generate(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Generating view layer files");
		Struts2ModuleConfig viewModuleConfig = (Struts2ModuleConfig) projectConfig.getViewModuleConfig();
		NavigationConfig navigationConfig = viewModuleConfig.getNavigationConfig();
		String srcPath = projectConfig.getSrcPath();
		String cfgPath = projectConfig.getCfgPath();
        String webPath = projectConfig.getWebContentPath();
		createPackagePath(srcPath, viewModuleConfig.getPackageName());
		createPath(webPath, "WEB-INF");
		createPath(webPath, "common");
		createPath(cfgPath, "lang");

		// Variable qui précise si la couche service utilise Spring ou non
		boolean isSpringServiceModule = projectConfig.getServiceModuleNature().equals(SpringServiceModuleConfig.NATURE);
		context.put(IS_SPRING_SERVICE_MODULE, isSpringServiceModule);

		context.put(IS_SITE_MESH_ENGINE, viewModuleConfig.isSiteMeshEngine());
		context.put(IS_TILES2_ENGINE, viewModuleConfig.isTiles2Engine());

		context.put(NAVIGATION_CONFIG_KEY, navigationConfig);

		copyStaticFiles(projectConfig);

        String filename = createJavaFilename(viewModuleConfig, "GenericAction");
        generateFile("src/GenericAction.java.vm", srcPath + filename);

        createPackagePath(srcPath, viewModuleConfig.getPackageName()+".util");
        filename = createJavaFilename(viewModuleConfig, "util/DisplayTagI18nWebwork2Adapter");
        generateFile("src/DisplayTagI18nWebwork2Adapter.java.vm", srcPath + filename);

		List<String> classList = new LinkedList<String>();
		List<DataObject> objectList = new LinkedList<DataObject>();
		for (DataObject object : projectConfig.getObjects()) {
			if (object.isSelected() == false) {
				continue;
			}
			context.put(OBJECT, object);

			filename = createFilename(viewModuleConfig, object.getClassName()+"Action-add-validation", XML_FILE_EXTENSION);
			generateFile("src/Action-add-validation.xml.vm", srcPath + filename);
			filename = createFilename(viewModuleConfig, object.getClassName()+"Action-update-validation", XML_FILE_EXTENSION);
			generateFile("src/Action-update-validation.xml.vm", srcPath + filename);

			filename = createJavaFilename(viewModuleConfig, object.getClassName() + ACTION_SUFFIX);
			generateFile("src/Action.java.vm", srcPath + filename);
			
			String objectClassnameLowerCase = object.getClassName().toLowerCase();
			String jspPath = projectConfig.getWebContentPath() + objectClassnameLowerCase;
			createPath(jspPath);

			generateFile("www/show.jsp.vm", jspPath + "/show.jsp");
			if (navigationConfig.isAddPage()) {
				generateFile("www/add.jsp.vm", jspPath + "/add.jsp");
			}
			if (navigationConfig.isViewPage()) {
				generateFile("www/view.jsp.vm", jspPath + "/view.jsp");
			}
			if (navigationConfig.isEditPage()) {
				generateFile("www/edit.jsp.vm", jspPath + "/edit.jsp");
			}

			generateFile("src/lang/class.properties.vm", cfgPath + "lang/" + objectClassnameLowerCase + ".properties");
			generateFile("src/lang/class_fr.properties.vm", cfgPath + "lang/" + objectClassnameLowerCase + "_fr.properties");

			context.remove(OBJECT);
			classList.add(object.getClassName());
			objectList.add(object);
		}
		
		context.put(CLASS_LIST, classList);
		generateFile("src/applicationContext-view.xml.vm", cfgPath + "applicationContext-view.xml");
		generateFile("src/struts.xml.vm", cfgPath + "struts.xml");
        generateFile("www/common/layout.jsp.vm", webPath + "common/layout.jsp");
		context.remove(CLASS_LIST);

		generateFile("src/displaytag.properties.vm", cfgPath + "displaytag.properties");

		generateFile("src/lang/common.properties.vm", cfgPath + "lang/common.properties");
		generateFile("src/lang/common_fr.properties.vm", cfgPath + "lang/common_fr.properties");
		
		generateFile("WEB-INF/web.xml.vm", webPath + "WEB-INF/web.xml");
        generateFile("www/index.jsp.vm", webPath + "index.jsp");
        generateFile("www/error.jsp.vm", webPath + "error.jsp");
	}
	/**
	 * @throws GenerationException 
	 * 
	 */
	protected void copyStaticFiles(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Génération des pages statiques de la couche view");
		Struts2ModuleConfig viewModuleConfig = (Struts2ModuleConfig) projectConfig.getViewModuleConfig();
		String srcPath = projectConfig.getSrcPath();
		String cfgPath = projectConfig.getCfgPath();

        String filename = createFilename(viewModuleConfig, "GenericAction", PROPERTY_FILE_EXTENSION);
        copyFile("src/GenericAction.properties", srcPath + filename);

        copyFile("src/freemarker.properties", cfgPath + "freemarker.properties");

        String webPath = projectConfig.getWebContentPath();
        copyFile("www/common/styles.css", webPath + "common/styles.css");
		if (viewModuleConfig.isTiles2Engine()) {
	        copyFile("WEB-INF/tiles.xml", webPath + "WEB-INF/tiles.xml");
	        copyFile("WEB-INF/tiles-config_2_0.dtd", webPath + "WEB-INF/tiles-config_2_0.dtd");
		} else if (viewModuleConfig.isSiteMeshEngine()) {
	        copyFile("WEB-INF/decorators.xml", webPath + "WEB-INF/decorators.xml");
		}
	}
	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {

	}
}
