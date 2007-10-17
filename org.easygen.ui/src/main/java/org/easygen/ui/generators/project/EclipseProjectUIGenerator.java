package org.easygen.ui.generators.project;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.generators.AbstractGenerator;
import org.easygen.core.generators.GenerationException;
import org.easygen.ui.util.EclipseUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 
 */
public class EclipseProjectUIGenerator extends AbstractGenerator {

	private static final Logger logger = Logger.getLogger(EclipseProjectUIGenerator.class);

	private static final String MODULE_NAME = "eclipse.uiproject";

	@Override
	protected String getModuleName() {
		return MODULE_NAME;
	}
	/**
	 * @see org.easygen.core.generators.Generator#generate(org.easygen.core.config.ProjectConfig)
	 */
	@Override
	public void generate(ProjectConfig projectConfig) throws GenerationException {
		logger.info("Generating eclipse project structure");
		try {
			NullProgressMonitor progressMonitor = new NullProgressMonitor();
			// TODO Si le projet existe le dire au user
			IJavaProject javaProject = EclipseUtils.createJavaProject(progressMonitor, projectConfig.getName());
			setupBuildPath(progressMonitor, projectConfig, javaProject);
			logger.info("Refreshing project");
			EclipseUtils.refreshLocal(javaProject.getProject(), progressMonitor);
		} catch (CoreException e) {
			throw new GenerationException("Can't create Eclipse project", e);
		}
	}
	/**
	 * @see org.easygen.core.generators.Generator#postProcess(ProjectConfig)
	 */
	@Override
	public void postProcess(ProjectConfig projectConfig) throws GenerationException {
		try {
			NullProgressMonitor progressMonitor = new NullProgressMonitor();
			IProject project = EclipseUtils.createProject(progressMonitor, projectConfig.getName());
			addLibraries(progressMonitor, projectConfig, project);
			EclipseUtils.refreshLocal(project, progressMonitor);
		} catch (JavaModelException e) {
			throw new GenerationException("Can't finalize Eclipse project", e);
		} catch (CoreException e) {
			throw new GenerationException("Can't finalize Eclipse project", e);
		}
	}

	protected void addLibraries(IProgressMonitor progressMonitor, ProjectConfig projectConfig, IProject project) throws JavaModelException {
		logger.info("Configuring libraries");
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] rawClasspath = javaProject.getRawClasspath();
		Collection<IClasspathEntry> classpathEntries = new LinkedList<IClasspathEntry>();
		Collections.addAll(classpathEntries, rawClasspath);
		
		String projectName = projectConfig.getName();
		String libDirname = projectConfig.getLibDirname();
		for (String library : projectConfig.getLibraries()) {
			IClasspathEntry entry = EclipseUtils.newLibraryEntry(projectName, libDirname, library);
			if (classpathEntries.contains(entry) == false) {
				classpathEntries.add(entry);
			}
		}
		// TODO Gérer les différences Eclipse 3.2 et 3.3
		classpathEntries.add(JavaCore.newVariableEntry(new Path("JUNIT4_HOME/junit-4.1.jar"), null, null));
		EclipseUtils.setProjectClasspath(progressMonitor, javaProject, classpathEntries);
	}

	/**
	 * 
	 */
	protected void addLibrary(Collection<IClasspathEntry> classpathEntries, String libraryPath) {
		IClasspathEntry libraryEntry = JavaCore.newLibraryEntry(new Path(libraryPath), null, null);
		classpathEntries.add(libraryEntry);
	}

	/**
	 * @param progressMonitor
	 * @param projectConfig
	 * @param rootPath
	 * @param javaProject
	 * @throws JavaModelException
	 */
	protected void setupBuildPath(IProgressMonitor progressMonitor, ProjectConfig projectConfig, IJavaProject javaProject) throws JavaModelException {
		logger.info("Setting up project build path");
		// Then to create a Java project, add the Java nature to the project:
		EclipseUtils.setOutputLocation(
			progressMonitor,
			javaProject,
			projectConfig.getName(),
			projectConfig.getBuildDirname()
		);
		
		Collection<IClasspathEntry> classpathEntries = new LinkedList<IClasspathEntry>();
		classpathEntries.add(
			EclipseUtils.newSourceFolder(projectConfig.getName(), projectConfig.getSrcDirname())
		);
		classpathEntries.add(
			EclipseUtils.newSourceFolder(projectConfig.getName(), projectConfig.getCfgDirname())
		);
		classpathEntries.add(
			EclipseUtils.newSourceFolder(projectConfig.getName(), projectConfig.getTestDirname())
		);
		classpathEntries.add(
			EclipseUtils.getDefaultJreContainer()
		);
		EclipseUtils.setProjectClasspath(progressMonitor, javaProject, classpathEntries);
	}
}
