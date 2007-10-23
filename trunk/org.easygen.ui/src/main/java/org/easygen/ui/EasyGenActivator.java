package org.easygen.ui;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EasyGenActivator extends AbstractUIPlugin /* implements IStartup */{

	// Pour que le plug-in soit chargé au lancement d'Eclipse,
	// on ajoute une extension org.eclipse.ui.startup.
	// Il faut pointer sur une classe qui implémente l'interface IStartup.

	// The plug-in ID
	public static final String PLUGIN_ID = "org.easygen.ui";

	// The shared instance
	private static EasyGenActivator plugin;

	private static final Logger logger = Logger.getLogger(EasyGenActivator.class);

	/**
	 * The constructor
	 */
	public EasyGenActivator() {
		try {
			plugin = this;
			getLog().addLogListener(new ILogListener() {
				public void logging(IStatus status, String plugin) {
					if (status.getSeverity() == IStatus.ERROR) {
						logger.error(status.getMessage(), status.getException());
					} else if (status.getSeverity() == IStatus.WARNING) {
						logger.warn(status.getMessage(), status.getException());
					} else if (status.getSeverity() == IStatus.INFO) {
						logger.info(status.getMessage(), status.getException());
					} else {
						logger.debug(status.getMessage(), status.getException());
					}
					logger.debug(status);
				}
			});
		} catch (RuntimeException e) {
			logger.error("Création du PluginActivator en erreur", e);
		}
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		try {
			super.start(context);
			logInfo("Starting EasyGen Plugin");
		} catch (Throwable e) {
			logError("Can't start plugin", e);
		}
		try {
			Class.forName("org.easygen.ui.wizards.NewProjectWizard");
			logInfo("Classe org.easygen.ui.wizards.NewProjectWizard chargée avec succès");
		} catch (Exception e) {
			logError("Impossible de charger la classe : org.easygen.ui.wizards.NewProjectWizard", e);
		}
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		try {
			plugin = null;
			super.stop(context);
			logInfo("Stopping EasyGen Plugin");
		} catch (Throwable e) {
			logError("Can't stop plugin", e);
		}
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static EasyGenActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	protected void logInfo(String message) {
		logger.info(message);
		Status status = new Status(Status.INFO, PLUGIN_ID, message);
		getLog().log(status);
	}

	protected void logWarn(String message, Throwable t) {
		logger.warn(message);
		Status status = new Status(Status.WARNING, PLUGIN_ID, message, t);
		getLog().log(status);
	}

	protected void logError(String message, Throwable t) {
		logger.error(message);
		Status status = new Status(Status.ERROR, PLUGIN_ID, message, t);
		getLog().log(status);
	}
}
