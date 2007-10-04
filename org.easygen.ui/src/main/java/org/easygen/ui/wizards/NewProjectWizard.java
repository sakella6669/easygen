package org.easygen.ui.wizards;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.apache.log4j.Logger;
import org.easygen.core.InitException;
import org.easygen.core.config.ProjectConfig;
import org.easygen.core.config.ProjectConfigSerializer;
import org.easygen.core.generators.GenerationException;
import org.easygen.core.generators.GeneratorManager;
import org.easygen.core.generators.common.MavenHandler;
import org.easygen.ui.localization.Localization;
import org.easygen.ui.modules.Module;
import org.easygen.ui.modules.ModuleManager;
import org.easygen.ui.modules.ModuleManagerFactory;
import org.easygen.ui.modules.ModuleNotFoundException;
import org.easygen.ui.util.EclipseUtils;
import org.easygen.ui.wizards.pages.CommonPage;
import org.easygen.ui.wizards.pages.ConfigureModulePage;
import org.easygen.ui.wizards.pages.ICommonPage;
import org.easygen.ui.wizards.pages.InformationPage;
import org.easygen.ui.wizards.pages.NewProjectPage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;

public class NewProjectWizard extends Wizard implements INewWizard, IPageChangedListener {

	private static final String EASYGEN_XML = "easygen.xml";
	
	protected Logger logger = Logger.getLogger(getClass());
	protected ProjectConfig projectConfig = null;

	protected boolean commonPageDone = false;
	protected boolean modulePagesAdded = false;

	protected ModuleManager moduleManager;

	/**
	 * Constructor for HibernateMappingNewWizard.
	 */
	public NewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
		logger.info("EasyGenNewWizard loaded");
	}
	/**
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection pSelection) {
        logger.info("Initialisation du wizard "+getClass().getName());
        setWindowTitle(Localization.get("easygen.title.plugin"));
        projectConfig = new ProjectConfig();

        moduleManager = ModuleManagerFactory.newInstance();
	}
	/**
	 * Adding the pages to the wizard.
	 */
	@Override
	public void addPages()
	{
        try
        {
	        addPage(new NewProjectPage());
	        addPage(new ConfigureModulePage(
        		moduleManager.getModuleNatures(ModuleManager.DATA_MODULE_KIND),
        		moduleManager.getModuleNatures(ModuleManager.SERVICE_MODULE_KIND),
        		moduleManager.getModuleNatures(ModuleManager.VIEW_MODULE_KIND)
	        ));
	        addPage(new InformationPage(
	        		Localization.get("easygen.title.beforeModuleConfiguration"),
	        		Localization.get("easygen.label.beforeModuleConfiguration")
	        ));
        }
        catch (RuntimeException e)
        {
        	logger.error("Error while adding pages", e);
        }
	}
	/**
     * @see org.eclipse.jface.wizard.Wizard#setContainer(org.eclipse.jface.wizard.IWizardContainer)
     */
    @Override
	public void setContainer(IWizardContainer pWizardContainer)
    {
	    super.setContainer(pWizardContainer);
	    if (pWizardContainer != null && pWizardContainer instanceof WizardDialog) {
	    	((WizardDialog) getContainer()).addPageChangedListener(this);
	    }
    }
	/**
     * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
     */
    @Override
	public void createPageControls(Composite pageContainer) {
	    try {
	        super.createPageControls(pageContainer);
        } catch (RuntimeException e) {
	        logger.error("createPageControls", e);
        }
    }
	/**
	 * @see org.eclipse.jface.wizard.Wizard#getStartingPage()
	 */
	@Override
	public IWizardPage getStartingPage() {
		CommonPage startingPage = (CommonPage) super.getStartingPage();
		logger.debug("Initializing page: "+startingPage.getName());
        startingPage.init(projectConfig);
		return startingPage;
	}
	/**
     * @see org.eclipse.jface.dialogs.IPageChangedListener#pageChanged(org.eclipse.jface.dialogs.PageChangedEvent)
     */
    public void pageChanged(PageChangedEvent pEvent) {
		IWizardPage currentPage = (IWizardPage) pEvent.getSelectedPage();
		logger.debug("Page changed: "+currentPage.getName());
    	try {
    		previousPageChanged(super.getPreviousPage(currentPage));
    		currentPageChanged(currentPage);
        } catch (RuntimeException e) {
        	logger.error("Error while finishing page", e);
        } catch (Exception e) {
        	logger.error("Error while finishing page", e);
		}
    }
	/**
	 * @param currentPage
	 */
	protected void currentPageChanged(IWizardPage currentPage) {
		if (currentPage == null) {
			return ;
		}
		initPage(currentPage);
		if (modulePagesAdded == false && currentPage.getName().equals(InformationPage.NAME))
		{
			updatePages(ModuleManager.DATA_MODULE_KIND, projectConfig.getDataModuleNature());
			updatePages(ModuleManager.SERVICE_MODULE_KIND, projectConfig.getServiceModuleNature());
			updatePages(ModuleManager.VIEW_MODULE_KIND, projectConfig.getViewModuleNature());
			getContainer().updateButtons();
			modulePagesAdded = true;
		}
	}
	/**
	 * @param currentPage
	 */
	protected void initPage(IWizardPage currentPage) {
		logger.debug("Initializing page: "+currentPage.getName());
		((ICommonPage)currentPage).init(projectConfig);
	}
	/**
	 * @param currentPage
	 * @throws Exception
	 */
	protected void previousPageChanged(IWizardPage newPreviousPage) throws Exception {
		if (newPreviousPage != null)
		{
			finishPage(newPreviousPage);
		    if (newPreviousPage.getName().equals(InformationPage.NAME))
				commonPageDone = true;
		}
	}
	/**
	 * @param newPreviousPage
	 * @throws CoreException 
	 * @throws Exception
	 */
	protected void finishPage(IWizardPage newPreviousPage) throws CoreException {
		try {
			logger.debug("Finishing page: "+newPreviousPage.getName());
			((ICommonPage)newPreviousPage).updateConfig(projectConfig);
		} catch (Exception e) {
			logger.error("Can't finish page", e);
			EclipseUtils.throwCoreException("Can't finish page", e);
		}
	}
	/**
     * @param pDataModuleManager
     * @param pDataModuleNature
     */
    protected void updatePages(String moduleKind, String pModuleNature)
    {
		if (pModuleNature == null)
			return ;
		try
        {
	        Module module = moduleManager.getModule(moduleKind, pModuleNature);
	        if (module == null) {
	        	return ;
	        }
	        IWizardPage[] modulePages = module.getPages();
	        logger.debug("Adding "+modulePages.length+" module pages");
	        for (IWizardPage modulePage : modulePages) {
	        	addPage(modulePage);
	        }
        }
        catch (ModuleNotFoundException e)
        {
	        logger.error("Selected Module does not exists", e);
        }
        catch (InitException e) {
	        logger.error("Can't initialize module", e);
		}
    }
	/**
	 * @see org.eclipse.jface.wizard.Wizard#getPreviousPage(org.eclipse.jface.wizard.IWizardPage)
	 */
	@Override
	public IWizardPage getPreviousPage(IWizardPage page)
	{
		logger.debug("getPreviousPage()->commonPageDone: "+commonPageDone);
		IWizardPage previousPage = super.getPreviousPage(page);
		if (commonPageDone)
			return null;
		return previousPage;
	}
	/**
     * @see org.eclipse.jface.wizard.Wizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
     */
    @Override
	public IWizardPage getNextPage(IWizardPage page)
    {
    	IWizardPage nextWizardPage = super.getNextPage(page);
    	if (nextWizardPage == null)
    		logger.debug("No next page");
    	else
    		logger.debug("Next page: "+nextWizardPage.getName());

	    return nextWizardPage;
    }
	/**
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish()
    {
		IWizardPage currentPage = getContainer().getCurrentPage();
		IWizardPage lastPage = getPages()[getPageCount()-1];
		if (currentPage.equals(lastPage) && lastPage.isPageComplete())
			return true;
		return false;
    }
	/**
	 * This method is called when 'Finish' button is pressed in
	 * the wizard. We will create an operation and run it
	 * using wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		try {
			finishPage(getContainer().getCurrentPage());
		} catch (CoreException e) {
			MessageDialog.openError(getShell(), "Error", e.getMessage());
		}
		
		IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor);
				} catch (CoreException e) {
					logger.error("IRunnableWithProgress Exception", e);
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};
		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			logger.error("InterruptedException", e);
			return false;
		} catch (InvocationTargetException e) {
			logger.error("InvocationTargetException", e);
			Throwable realException = e.getTargetException();
			MessageDialog.openError(getShell(), "Error", realException.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * The worker method. It will find the container, create the
	 * file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */
	protected void doFinish(IProgressMonitor progressMonitor) throws CoreException {
		int numTask = 4;
		int totalWork = 1;
		progressMonitor.beginTask("Génération des modules", numTask);
		GeneratorManager generatorManager = new GeneratorManager();
		try {
			generatorManager.generate(projectConfig);
		} catch (GenerationException e) {
			EclipseUtils.throwCoreException("Module Generation failed", e);
		}
		progressMonitor.worked(totalWork++);
		progressMonitor.setTaskName("Writing EasyGen config file");
		try {
        	File outputFile = new File(projectConfig.getPath(), EASYGEN_XML);
        	new ProjectConfigSerializer().serialize(projectConfig, outputFile);
		} catch (IOException e) {
			MessageDialog.openError(getShell(), "Generation Error", "Impossible d'écrire la configuration EasyGen: "+e.toString());
			logger.warn("Impossible d'écrire la configuration EasyGen", e);
		}
		progressMonitor.worked(totalWork++);
		MavenHandler mavenHandler = new MavenHandler();
		mavenHandler.callMaven(projectConfig.getPath(), "eclipse:eclipse");
		progressMonitor.worked(totalWork++);
		EclipseUtils.refreshLocal(projectConfig.getName(), progressMonitor);
		progressMonitor.worked(totalWork++);
	}
	/**
	 * @see org.eclipse.jface.wizard.Wizard#performCancel()
	 */
	@Override
	public boolean performCancel() {
		// TODO May be we should ask if the user wants to delete all generated files
		return super.performCancel();
	}
}
