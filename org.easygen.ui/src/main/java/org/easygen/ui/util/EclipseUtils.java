package org.easygen.ui.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.easygen.ui.EasyGenActivator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * @author eveno
 * Created on 9 mars 07
 *
 */
public class EclipseUtils {
	protected final static Logger logger = Logger.getLogger(EclipseUtils.class);

	private EclipseUtils() {
	}
    /**
     * Get the defualt location for the provided name.
     * @param nameValue the name
     * @return the location
     */
    public static String getDefaultProjectLocationForName(String nameValue) {
		try {
			return Platform.getLocation().append(nameValue).toOSString();
		} catch (org.eclipse.core.runtime.AssertionFailedException e) {
			return "error/" + nameValue;
		}
	}
	/**
	 * @param projectConfig
	 * @return
	 * @throws CoreException
	 */
    public static IJavaProject createJavaProject(IProgressMonitor progressMonitor, String name) throws CoreException {
		logger.info("Creating Java Project");
		IProject project = createProject(progressMonitor, name);
		addJavaNature(progressMonitor, project);
		IJavaProject javaProject = JavaCore.create(project);
		return javaProject;
	}
	/**
	 * @param projectConfig
	 * @return
	 * @throws CoreException
	 */
    public static IJavaProject getJavaProject(IProgressMonitor progressMonitor, String name) throws CoreException {
		logger.info("Getting Java Project");
		IProject project = getProject(progressMonitor, name);
		IJavaProject javaProject = JavaCore.create(project);
		return javaProject;
	}

	/**
	 * @param progressMonitor
	 * @param project
	 * @throws CoreException
	 */
	public static void addJavaNature(IProgressMonitor progressMonitor, IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		String[] newNatures = new String[natures.length + 1];
		System.arraycopy(natures, 0, newNatures, 0, natures.length);
		newNatures[natures.length] = JavaCore.NATURE_ID;
		description.setNatureIds(newNatures);
		project.setDescription(description, progressMonitor);
	}
	/**
	 * @param progressMonitor
	 * @param projectConfig
	 * @return
	 * @throws CoreException
	 */
	public static IProject createProject(IProgressMonitor progressMonitor, String name) throws CoreException {
		return getOrCreateProject(progressMonitor, name, true);
	}
	/**
	 * @param progressMonitor
	 * @param projectConfig
	 * @return
	 * @throws CoreException
	 */
	public static IProject getProject(IProgressMonitor progressMonitor, String name) throws CoreException {
		return getOrCreateProject(progressMonitor, name, false);
	}
	/**
	 * @param progressMonitor
	 * @param projectConfig
	 * @return
	 * @throws CoreException
	 */
	protected static IProject getOrCreateProject(IProgressMonitor progressMonitor, String name, boolean create) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);
		if (project.exists() == false && create == true) {
			project.create(progressMonitor);
		}
		project.open(progressMonitor);
		return project;
	}

	public static void deleteProject(IProgressMonitor progressMonitor, String name) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);
		if (project.exists()) {
			project.delete(true, true, progressMonitor);
		}
	}

	public static boolean isExistingProject(String name) {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(name);
		return project.exists();
	}
	/**
	 * @param javaProject
	 * @param classpathEntries
	 * @throws JavaModelException 
	 */
    public static void setProjectClasspath(IProgressMonitor progressMonitor, IJavaProject javaProject, Collection<IClasspathEntry> classpathEntries) throws JavaModelException {
		IClasspathEntry[] newClasspath = new IClasspathEntry[classpathEntries.size()];
		classpathEntries.toArray(newClasspath);
		setProjectClasspath(progressMonitor, javaProject, newClasspath);
	}

	/**
	 * @param progressMonitor
	 * @param javaProject
	 * @param newClasspath
	 * @throws JavaModelException
	 */
	public static void setProjectClasspath(IProgressMonitor progressMonitor, IJavaProject javaProject, IClasspathEntry[] newClasspath) throws JavaModelException {
		javaProject.setRawClasspath(newClasspath, progressMonitor);
	}

	/**
	 * @param classpathEntries
	 */
	public static IClasspathEntry newUserLibraryEntry(String libraryName) {
		Path path = new Path("org.eclipse.jdt.USER_LIBRARY/"+ libraryName);
		return JavaCore.newContainerEntry(path);
	}
	
	/**
	 * @param classpathEntries
	 */
	public static IClasspathEntry newLibraryEntry(String projectName, String libraryDir, String libraryName) {
		Path path = new Path(getPath(projectName, libraryDir) + libraryName);
		return JavaCore.newLibraryEntry(path, null, null);
	}

	/**
	 * @param classpathEntries
	 */
	public static IClasspathEntry newSourceFolder(String projectName, String srcDir) {
		IClasspathEntry srcEntry = JavaCore.newSourceEntry(
				new Path(getPath(projectName, srcDir))
			);
		return srcEntry;
	}
	/**
	 * @param projectName
	 * @param srcDir
	 * @return
	 */
	public static String getPath(String projectName, String subDir) {
		return getRootPath(projectName)+subDir+'/';
	}
	/**
	 * @param javaProject
	 * @param projectName
	 * @param buildDirname
	 * @throws JavaModelException 
	 */
	public static void setOutputLocation(IProgressMonitor progressMonitor, IJavaProject javaProject, String projectName, String buildDirname) throws JavaModelException {
		IPath outputPath = new Path(getPath(projectName, buildDirname));
		javaProject.setOutputLocation(outputPath, progressMonitor);
	}
	/**
	 * @param projectConfig
	 * @return
	 */
	public static String getRootPath(String name) {
		String rootPath = '/'+name+'/';
		return rootPath;
	}

	/**
	 * @param classpathEntries
	 */
	public static IClasspathEntry getDefaultJreContainer() {
		IClasspathEntry defaultJREContainerEntry = JavaRuntime.getDefaultJREContainerEntry();
		return defaultJREContainerEntry;
	}
	/**
	 * Opens a IFile in an editor 
	 * @param shell
	 * @param file
	 */
    public static void openFileInEditor(Shell shell, final IFile file) {
		shell.getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchPage _page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				try {
					IDE.openEditor(_page, file, true);
				}
				catch (PartInitException e) {

				}
			}
		});
	}
	/**
	 * Creates a file in the container (i.e. project) with the given name and stream source
	 * @param monitor
	 * @param containerName
	 * @param fileName
	 * @param stream
	 * @throws CoreException
	 * @throws IOException
	 */
	public static void createFile(IProgressMonitor monitor, String containerName, String fileName, InputStream stream) throws CoreException, IOException {

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException("Container \"" + containerName + "\" does not exist.");
		}
		IContainer container = (IContainer) resource;
		final IFile file = container.getFile(new Path(fileName));
		if (file.exists()) {
			file.setContents(stream, true, true, monitor);
		} else {
			file.create(stream, true, monitor);
		}
		stream.close();
	}

	public static void throwCoreException(String message) throws CoreException {
		throwCoreException(message, null);
	}

	public static void throwCoreException(String message, Throwable cause) throws CoreException {
		if (cause != null)
			message += " : "+cause.toString();
		IStatus status = new Status(IStatus.ERROR, EasyGenActivator.PLUGIN_ID, IStatus.OK, message, cause);
		throw new CoreException(status);
	}
	/**
	 * @throws CoreException 
	 */
	public static void dumpProjectClasspath(String projectName) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		project.open(null);
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
		for (IClasspathEntry entry : classpathEntries) {
			System.out.println("class: "+entry.getClass());
			System.out.println("entryKind: "+entry.getEntryKind());
			System.out.println("contentKind: "+entry.getContentKind());
			System.out.println("ouputLocation: "+entry.getOutputLocation());
			System.out.println("path: "+entry.getPath());
			System.out.println("isExported: "+entry.isExported());
			System.out.println("extra:");
			IClasspathAttribute[] extraAttributes = entry.getExtraAttributes();
			for (IClasspathAttribute attribute : extraAttributes) {
				System.out.println(attribute.getName()+"="+attribute.getValue());
			}
		}
	}
	/**
	 * @param project
	 * @throws CoreException 
	 */
	public static void refreshLocal(String projectName, IProgressMonitor progressMonitor) throws CoreException {
		IProject project = getProject(progressMonitor, projectName);
		project.refreshLocal(IResource.DEPTH_INFINITE, progressMonitor);
	}
	/**
	 * @param project
	 * @throws CoreException 
	 */
	public static void refreshLocal(IProject project, IProgressMonitor progressMonitor) throws CoreException {
		project.refreshLocal(IResource.DEPTH_INFINITE, progressMonitor);
	}
}
