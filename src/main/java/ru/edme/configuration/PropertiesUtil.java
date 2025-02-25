package ru.edme.configuration;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }
    private PropertiesUtil() {}

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
      try (var inputStream =  org.apache.logging.log4j.util.PropertiesUtil.class.getClassLoader().getResourceAsStream("database.properties")){
          PROPERTIES.load(inputStream);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
    }
}
