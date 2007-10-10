package org.easygen.ui.localization;

import java.io.File;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

// TODO Refactorer le nommage des clés pour les labels
public class Localization
{
	private static Logger logger = Logger.getLogger(Localization.class);
	private static Map<String, ResourceBundle> bundles = new Hashtable<String, ResourceBundle>();

	static
	{
        Locale currentLocale = Locale.getDefault();
        logger.info("Initialisation de la Localisation pour la locale ["+currentLocale.toString()+"]");
        loadBundle("easygen");
		logger.debug(Thread.currentThread().getContextClassLoader().getResource("."));
		logger.debug(new File(".").getAbsolutePath());
	}

	/**
     * @param pString
     * @param pString2
     */
    public static void loadBundle(String pKey)
    {
    	String fileName = "easygen-"+pKey;
    	logger.debug("Chargement du fichier "+fileName);
    	try
        {
	        ResourceBundle bundle = ResourceBundle.getBundle(fileName);
	        bundles.put(pKey, bundle);
        }
        catch (MissingResourceException e)
        {
	        logger.warn("Fichier de ressources introuvable: "+fileName, e);
        }
        catch (RuntimeException e)
        {
	        logger.warn("Impossible de charger le fichier de ressources: "+fileName, e);
        }
    }

	public static String get(String key)
	{
		int prefixEnd = key.indexOf('.');
		String msg = null;
		if (prefixEnd > 0)
		{
			String bundleKey = key.substring(0, prefixEnd);
			String realKey = key.substring(prefixEnd+1);
			msg = get(bundleKey, realKey);
		}
		else
		{
			msg = get("easygen", key);
		}
		return msg;
	}

	protected static String get(String bundleKey, String key)
	{
		String value;
        try {
        	if (bundles.containsKey(bundleKey) == false)
        	{
        		loadBundle(bundleKey);
            	if (bundles.containsKey(bundleKey) == false)
            		return key;
        	}
        	ResourceBundle bundle = (ResourceBundle) bundles.get(bundleKey);
	        value = bundle.getString(key);
        } catch (MissingResourceException e) {
        	value = key;
        	logger.debug("Clé introuvable ["+key+"] dans le bundle: "+bundleKey);
        }
		return value;
	}

	public static String get(String key, Object[] params)
	{
		String pattern = get(key);
		String value = MessageFormat.format(pattern, params);
		return value;
	}
}
