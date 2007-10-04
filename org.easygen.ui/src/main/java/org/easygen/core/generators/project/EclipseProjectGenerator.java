package org.easygen.core.generators.project;

import java.io.File;

import org.easygen.core.InitException;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;
import org.easygen.core.generators.hibernate.HibernateModuleConfig;
import org.easygen.core.generators.springservice.SpringServiceModuleConfig;
import org.easygen.core.generators.struts2.Struts2ModuleConfig;


/**
 * @author eveno
 * Created on 20 déc. 06
 *
 */
public class EclipseProjectGenerator extends AbstractGenerator
{
	private static final String MODULE_NAME = "eclipseproject";
	private static final String SETTINGS_DIR = ".settings";
	private static final String BUILDER_DIR = ".externalToolBuilders";
	private static final String LIBRARY_LIST = "libraries";

	/**
	 * @throws InitException
	 */
	public EclipseProjectGenerator()
	{
	    super();
	}
	/**
	 * @see org.easygen.core.generators.AbstractGenerator#getModuleName()
	 */
    @Override
    protected String getModuleName()
    {
    	return MODULE_NAME;
    }
//	/**
//     * @see org.easygen.core.generators.AbstractGenerator#init()
//     */
//    @Override
//	public void initTemplates(ProjectConfig pProjectConfig)
//    {
//		logger.info("Initialisation des templates");
//    	try
//        {
//	        // Project Templates
//	        loadTemplate("classpath.vm");
//	        loadTemplate("project.vm");
//
//	        loadTemplate("org.eclipse.wst.common.project.facet.core.xml.vm");
//	        // Settings Templates
//	        loadTemplate("org.eclipse.wst.common.component.vm");
//	        loadTemplate("org.eclipse.wst.validation.prefs.vm");
//
//	        // Builder Template
//	        loadTemplate("org.eclipse.wst.common.project.facet.core.builder.launch.vm");
//        }
//        catch (Exception e)
//        {
//        	throw new InitException("Erreur pendant l'initialisation des templates Velocity", e);
//        }
//	}
	/**
	 * @see org.easygen.core.generators.AbstractGenerator#generate()
	 */
    @Override
	public void generate(ProjectConfig projectConfig) throws GenerationException
	{
		logger.info("Génération de la structure du projet");
    	createPath(projectConfig.getPath());
    	createPath(projectConfig.getSrcPath());

    	context.put("isStruts2ViewModule", projectConfig.getViewModuleNature().equals(Struts2ModuleConfig.NATURE));
    	context.put("isSpringServiceModule", projectConfig.getServiceModuleNature().equals(SpringServiceModuleConfig.NATURE));
    	context.put("isHibernateDataModule", projectConfig.getDataModuleNature().equals(HibernateModuleConfig.NATURE));

    	// Génération du fichier .project
        generateFile(getTemplate("project.vm"), projectConfig.getPath() + ".project");

    	// Génération du fichier pom.xml pour Maven
        generateFile(getTemplate("pom.vm"), projectConfig.getPath() + "pom.xml");

        if (projectConfig.getProjectNature().equals(EclipseProjectConfig.WTP_NATURE))
        {
        	createPath(projectConfig.getPath(), SETTINGS_DIR);
        	String settingsDir = projectConfig.getPath() + SETTINGS_DIR + File.separatorChar;

        	// Génération du fichier org.eclipse.wst.common.component
            String filename = "org.eclipse.wst.common.component";
            generateFile(getTemplate(filename+".vm"), settingsDir + filename);

            // Génération du fichier org.eclipse.wst.common.project.facet.core.xml
            filename = "org.eclipse.wst.common.project.facet.core.xml";
            generateFile(getTemplate(filename+".vm"), settingsDir + filename);

            // Génération du fichier org.eclipse.wst.validation.prefs
            filename = "org.eclipse.wst.validation.prefs";
            generateFile(getTemplate(filename+".vm"), settingsDir + filename);

        	createPath(projectConfig.getPath(), BUILDER_DIR);
        	String builderDir = projectConfig.getPath() + BUILDER_DIR + File.separatorChar;
            // Génération du fichier org.eclipse.wst.common.project.facet.core.builder.launch
            filename = "org.eclipse.wst.common.project.facet.core.builder.launch";
            generateFile(getTemplate(filename+".vm"), builderDir + filename);
        }
	}
	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
	 */
    @Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
    	// Génération du fichier .classpath
        String filename = ".classpath";
		context.put(LIBRARY_LIST, projectConfig.getLibraries());
        generateFile(getTemplate("classpath.vm"), projectConfig.getPath() + filename);
	}
}
