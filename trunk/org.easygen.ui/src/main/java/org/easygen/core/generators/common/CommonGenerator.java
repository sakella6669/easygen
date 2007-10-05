package org.easygen.core.generators.common;

import org.easygen.core.InitException;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;
import org.easygen.core.generators.hibernate.HibernateModuleConfig;
import org.easygen.core.generators.springservice.SpringServiceModuleConfig;
import org.easygen.core.generators.struts2.Struts2ModuleConfig;


/**
 * @author eveno
 * Created on 19 déc. 06
 *
 */
public class CommonGenerator extends AbstractGenerator
{
	private static final String MODULE_NAME = "common";

	/**
     * @throws InitException
     */
    public CommonGenerator() {
	    super();
    }
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

    	context.put("isStruts2ViewModule", projectConfig.getViewModuleNature().equals(Struts2ModuleConfig.NATURE));
    	context.put("isSpringServiceModule", projectConfig.getServiceModuleNature().equals(SpringServiceModuleConfig.NATURE));
    	context.put("isHibernateDataModule", projectConfig.getDataModuleNature().equals(HibernateModuleConfig.NATURE));

    	// Génération du fichier pom.xml pour Maven
        generateFile(getTemplate("pom.vm"), projectConfig.getPath() + "pom.xml");

        createPath(projectConfig.getCfgPath());
		generateFile(getTemplate("log4j.xml.vm"), projectConfig.getCfgPath()+"log4j.xml");
	}
	/**
     * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
     */
    @Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
    }
}
