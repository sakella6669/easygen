package org.easygen.core.generators.servicejava;

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
public class ServiceJavaGenerator extends AbstractGenerator {

	protected static final String MODULE_NAME = "servicejava";
	protected static final String SERVICE_SUFFIX = "Service";
	protected static final String SERVICE_TESTCASE_SUFFIX = "ServiceTest";
	protected static final String STACK = "stack";

	/**
	 * @throws InitException
	 */
	public ServiceJavaGenerator() {
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
			throw new GenerationException("Service Java Module should be used with Hibernate as Data Module");
		}
		logger.info("Génération de la couche service");
		ServiceModuleConfig serviceModuleConfig = projectConfig.getServiceModuleConfig();
		createPackagePath(projectConfig.getSrcPath(), serviceModuleConfig.getPackageName());
		createPackagePath(projectConfig.getTestPath(), serviceModuleConfig.getPackageName());

		// Génération des services Java
		List<String> classList = new LinkedList<String>();
		for (DataObject object : projectConfig.getObjects()) {
			if (object.isSelected() == false)
				continue;
			addStackToVelocityContext();
			context.put(OBJECT, object);

			String javaFilename = createJavaFilename(serviceModuleConfig, object.getClassName() + SERVICE_SUFFIX);
			generateFile("class.java.vm", projectConfig.getSrcPath() + javaFilename);

			javaFilename = createJavaFilename(serviceModuleConfig, object.getClassName() + SERVICE_TESTCASE_SUFFIX);
			generateFile("testCase.java.vm", projectConfig.getTestPath() + javaFilename);

			classList.add(object.getClassName());
			context.remove(OBJECT);
			removeStackFromVelocityContext();
		}

		// Génération de la classe ServiceLocator
		context.put(CLASS_LIST, classList);
		String javaFilename = createJavaFilename(serviceModuleConfig, "ServiceLocator");
		generateFile("ServiceLocator.java.vm", projectConfig.getSrcPath() + javaFilename);
		context.remove(CLASS_LIST);

		javaFilename = createJavaFilename(serviceModuleConfig, "Service");
		generateFile("Service.java.vm", projectConfig.getSrcPath() + javaFilename);

		// Génération de la classe GenericService
		javaFilename = createJavaFilename(serviceModuleConfig, "GenericService");
		generateFile("GenericService.java.vm", projectConfig.getSrcPath() + javaFilename);

		// Génération des exceptions
		javaFilename = createJavaFilename(serviceModuleConfig, "ServiceException");
		generateFile("ServiceException.java.vm", projectConfig.getSrcPath() + javaFilename);

		// Génération du generic ServiceTestCase
		javaFilename = createJavaFilename(serviceModuleConfig, "AbstractServiceTest");
		generateFile("AbstractServiceTest.java.vm", projectConfig.getTestPath() + javaFilename);

		generateSubException(serviceModuleConfig, projectConfig.getSrcPath(), "Database");
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
		generateFile("SubServiceException.java.vm", path + javaFilename);
	}

	/**
	 * @see org.easygen.core.generators.AbstractGenerator#postProcess(ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
	}
}
