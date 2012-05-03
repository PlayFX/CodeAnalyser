package cz.jfx.CodeAnalyser.Config;

import cz.jfx.CodeAnalyser.Control.AnalyserController;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.LogManager;

/**
 *
 * @author Felix
 */
public class Config {

    private static final Config instance = new Config();
    private ResourceBundle application = ResourceBundle.getBundle("cz/jfx/CodeAnalyser/Config/application");
    private ResourceBundle defaults = ResourceBundle.getBundle("cz/jfx/CodeAnalyser/Config/defaults");
    private Properties properties = new Properties();

    /**
     * Gets a application config value
     * @param key
     * @return 
     */
    public static String getValue(String key) {
        if (instance.application.containsKey(key)) {
            return instance.application.getString(key);
        }
        AnalyserController.logger.log(Level.CONFIG, "Key {0} not found in bundle", key);
        return null;
    }

    /**
     * Gets a custom(defaul) setting value
     * @param key
     * @return 
     */
    public static String getProperty(String key) {
        if (instance.properties.containsKey(key)) {
            return instance.properties.getProperty(key);
        }
        AnalyserController.logger.log(Level.CONFIG, "Key {0} not found in properties", key);
        return null;
    }

    /**
     * Sets a Object o to String key
     * @param key
     * @param o 
     */
    public static void saveProperty(String key, Object o) {
        instance.properties.put(key, o);
    }

    /**
     * Loads config from properly file
     * @param filename 
     */
    public void load(String filename) {
        try {
            // Check if config file exists
            File configFile = new File(filename);
            if (configFile.exists()) {
                // if yes, load custom settings
                properties.load(new FileInputStream(filename));
            }

        } catch (IOException e) {
            AnalyserController.logger.log(Level.CONFIG, "Load fail: {0}", e.getMessage());
        }
    }

    /**
     * Process when application starts
     */
    public void startup() {
        try {
            // Sets logging file
            LogManager.getLogManager().readConfiguration(Config.class.getResourceAsStream("/cz/jfx/CodeAnalyser/Config/logging.properties"));
        } catch (IOException | SecurityException e) {
            System.out.println(e.getMessage());
            AnalyserController.logger.log(Level.CONFIG, "Reading logging file error: {0}", e.getMessage());
        }

        loadDefaults(defaults);
    }

    /**
     * Write config to properly file
     * @param filename 
     */
    public void store(String filename) {
        try {
            properties.store(new FileOutputStream(filename), "CodeAnalyse: settings");
        } catch (IOException e) {
            AnalyserController.logger.log(Level.CONFIG, "Store fail: {0}", e.getMessage());
        }
    }

    /**
     * Fill first defaults data
     * @param resource 
     */
    private void loadDefaults(ResourceBundle resource) {
        Enumeration<String> keys = resource.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            properties.put(key, resource.getString(key));
        }
    }

    /**
     * Return Config instance
     * @return 
     */
    public static Config getInstance() {
        return instance;
    }
}
