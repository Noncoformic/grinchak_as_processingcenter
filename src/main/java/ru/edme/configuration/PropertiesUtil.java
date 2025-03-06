package ru.edme.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger logger = LogManager.getLogger(PropertiesUtil.class);
    private static final String DATABASE_PROPERTIES_FILE = "database.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = PropertiesUtil.class.getClassLoader().getResourceAsStream(DATABASE_PROPERTIES_FILE)) {
            if (input == null) {
                logger.error("Unable to find database.properties");
                return;
            }
            PROPERTIES.load(input);
            logger.info("Loaded properties from database.properties");
        } catch (IOException ex) {
            logger.error("Error loading database.properties", ex);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}