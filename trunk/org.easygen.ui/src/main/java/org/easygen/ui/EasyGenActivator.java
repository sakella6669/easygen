package org.easygen.ui;

import java.net.URL;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class EasyGenActivator extends AbstractUIPlugin /*implements IStartup*/ {
	
//	Pour que le plug-in soit chargé au lancement d'Eclipse,
//  on ajoute une extension org.eclipse.ui.startup.
//	Il faut pointer sur une classe qui implémente l'interface IStartup.

	// The plug-in ID
	public static final String PLUGIN_ID = "org.easygen.ui";

	// The shared instance
	private static EasyGenActivator plugin;

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * The constructor
	 */
	public EasyGenActivator() {
		plugin = this;
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception
	{
		URL log4jConfigFile = Thread.currentThread().getContextClassLoader().getResource("log4j.xml");
		if (log4jConfigFile != null)
			DOMConfigurator.configure(log4jConfigFile);
		else
			BasicConfigurator.configure();
		try {
			super.start(context);
			logger.info("Starting EasyGen Plugin");
		} catch (Throwable e) {
			logger.error("Can't start plugin", e);
			e.printStackTrace();
		}
		try {
			Class.forName("org.easygen.ui.wizards.NewProjectWizard");
			logger.info("Classe org.easygen.ui.wizards.NewProjectWizard chargée avec succès");
		} catch (Exception e) {
			logger.error("Impossible de charger la classe : org.easygen.ui.wizards.NewProjectWizard", e);
		}
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception
	{
		try {
			plugin = null;
			super.stop(context);
			logger.info("Stopping EasyGen Plugin");
		} catch (Throwable e) {
			logger.error("Can't stop plugin", e);
			e.printStackTrace();
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
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
