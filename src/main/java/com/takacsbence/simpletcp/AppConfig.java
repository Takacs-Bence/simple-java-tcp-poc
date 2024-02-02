package com.takacsbence.simpletcp;

import java.io.FileInputStream;
import java.util.Properties;

public class AppConfig {

    private static final String CONFIG_PROPERTIES_PATH = "src/main/resources/config/config.properties";

    private static final Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_PROPERTIES_PATH)) {
            properties.load(input);
        } catch (Exception e) {
            System.err.println("loading configs were not successful");
            throw new IllegalStateException("config loading is mandatory");
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
