package org.easygen.core.generators.springservice;

import java.util.LinkedList;
import java.util.List;

import org.easygen.core.InitException;
import org.easygen.core.config.DataObject;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.config.ServiceModuleConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;
import org.easygen.core.generators.hibernate.HibernateModuleConfig;

/**
 * @author eveno
 * Created on 19 déc. 06
 *
 */
public class SpringServiceGenerator extends AbstractGenerator {

	protected static final String MODULE_NAME = "springservice";
	protected static final String SERVICE_SUFFIX = "Service";
	protected static final String SERVICE_TESTCASE_SUFFIX = "ServiceTest";

	/**
	 * @throws InitException
	 */
	public SpringServiceGenerator() {
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
		if (projectConfig.getDataModuleNature().equals(HibernateModuleConfig.NATURE) == false) {
			throw new GenerationException("Spring Service Module should be used with Hibernate as Data Module");
		}
		logger.info("Génération de la couche service");
		ServiceModuleConfig serviceModuleConfig = projectConfig.getServiceModuleConfig();
		String srcPath = projectConfig.getSrcPath();
		createPackagePath(srcPath, serviceModuleConfig.getPackageName());
		createPackagePath(projectConfig.getTestPath(), serviceModuleConfig.getPackageName());

		delete(srcPath + "hibernate.cfg.xml");
		// Génération des services Java
		List<String> classList = new LinkedList<String>();
		for (DataObject object : projectConfig.getObjects()) {
			if (object.isSelected() == false)
				continue;
			addStackToVelocityContext();
			context.put(OBJECT, object);

			String javaFilename = createJavaFilename(serviceModuleConfig, object.getClassName() + SERVICE_SUFFIX);
			generateFile(getTemplate("class.java.vm"), srcPath + javaFilename);

			javaFilename = createJavaFilename(serviceModuleConfig, object.getClassName() + SERVICE_TESTCASE_SUFFIX);
			generateFile(getTemplate("testCase.java.vm"), projectConfig.getTestPath() + javaFilename);

			context.remove(OBJECT);
			classList.add(object.getClassName());
			removeStackFromVelocityContext();
		}
		context.remove(STACK);

		// Génération de la classe ServiceLocator
		context.put(CLASS_LIST, classList);
		generateFile(getTemplate("applicationContext-service.xml.vm"), srcPath + "applicationContext-service.xml");
		String javaFilename = createJavaFilename(serviceModuleConfig, "ServiceLocator");
		generateFile(getTemplate("ServiceLocator.java.vm"), projectConfig.getSrcPath() + javaFilename);
		context.remove(CLASS_LIST);

		String filename = createJavaFilename(serviceModuleConfig, "Service");
		generateFile(getTemplate("Service.java.vm"), srcPath + filename);

		// Génération de la classe GenericService
		filename = createJavaFilename(serviceModuleConfig, "GenericService");
		generateFile(getTemplate("GenericService.java.vm"), srcPath + filename);

		// Génération des exceptions
		filename = createJavaFilename(serviceModuleConfig, "ServiceException");
		generateFile(getTemplate("ServiceException.java.vm"), srcPath + filename);

		// Génération du generic ServiceTestCase
		filename = createJavaFilename(serviceModuleConfig, "AbstractServiceTest");
		generateFile(getTemplate("AbstractServiceTest.java.vm"), projectConfig.getTestPath() + filename);

		generateSubException(serviceModuleConfig, srcPath, "Database");
	}

	/**
	 * @param serviceModuleConfig
	 * @param exceptionName
	 * @throws GenerationException
	 */
	protected void generateSubException(ServiceModuleConfig serviceModuleConfig, String path, String exceptionName) throws GenerationException {
		String javaFilename;
		context.put("exceptionName", exceptionName);
		javaFilename = createJavaFilename(serviceModuleConfig, exceptionName + "ServiceException");
		generateFile(getTemplate("SubServiceException.java.vm"), path + javaFilename);
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
	}
}
