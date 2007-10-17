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
		String srcPath = projectConfig.getSrcPath();
        String webPath = projectConfig.getWebContentPath();
		createPackagePath(srcPath, viewModuleConfig.getPackageName());
		createPath(webPath, "WEB-INF");
		createPath(webPath, "common");

		// Variable qui précise si la couche service utilise Spring ou non
		boolean isSpringServiceModule = projectConfig.getServiceModuleNature().equals(SpringServiceModuleConfig.NATURE);
		context.put(IS_SPRING_SERVICE_MODULE, isSpringServiceModule);

		context.put(IS_SITE_MESH_ENGINE, viewModuleConfig.isSiteMeshEngine());
		context.put(IS_TILES2_ENGINE, viewModuleConfig.isTiles2Engine());

		copyStaticFiles(projectConfig);

        String filename = createJavaFilename(viewModuleConfig, "GenericAction");
        generateFile("src/GenericAction.java.vm", srcPath + filename);

		List<String> classList = new LinkedList<String>();
		List<DataObject> objectList = new LinkedList<DataObject>();
		for (DataObject object : projectConfig.getObjects()) {
			if (object.isSelected() == false)
				continue;
			context.put(OBJECT, object);

			filename = createFilename(viewModuleConfig, object.getClassName()+"Action-add-validation", XML_FILE_EXTENSION);
			generateFile("src/Action-add-validation.xml.vm", srcPath + filename);
			filename = createFilename(viewModuleConfig, object.getClassName()+"Action-update-validation", XML_FILE_EXTENSION);
			generateFile("src/Action-update-validation.xml.vm", srcPath + filename);

			filename = createJavaFilename(viewModuleConfig, object.getClassName() + ACTION_SUFFIX);
			generateFile("src/Action.java.vm", srcPath + filename);
			
			String jspPath = projectConfig.getWebContentPath() + object.getClassName().toLowerCase();
			createPath(jspPath);

			generateFile("www/add.jsp.vm", jspPath + "/add.jsp");
			// TODO Finir d'ajouter la page view plus complète que dans le show
			// (i.e. avec les FK)
			generateFile("www/view.jsp.vm", jspPath + "/view.jsp");
			generateFile("www/edit.jsp.vm", jspPath + "/edit.jsp");
			generateFile("www/show.jsp.vm", jspPath + "/show.jsp");

			context.remove(OBJECT);
			classList.add(object.getClassName());
			objectList.add(object);
		}
		
		context.put(CLASS_LIST, classList);
		generateFile("src/applicationContext-view.xml.vm", projectConfig.getCfgPath() + "applicationContext-view.xml");
		generateFile("src/struts.xml.vm", projectConfig.getCfgPath() + "struts.xml");
        generateFile("www/common/layout.jsp.vm", webPath + "common/layout.jsp");
		context.remove(CLASS_LIST);

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

        String filename = createFilename(viewModuleConfig, "GenericAction", PROPERTY_FILE_EXTENSION);
        copyFile("src/GenericAction.properties", srcPath + filename);

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
