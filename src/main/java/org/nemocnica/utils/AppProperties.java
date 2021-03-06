package org.nemocnica.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;


public class AppProperties {
    private static final Logger logger = LoggerFactory.getLogger(AppProperties.class.getName());

    private static AppProperties instance = null;

    private static final String PROPERTY_FILENAME = "app.properties";
    private static final String PROPERTY_DATABASE_PATH = "database.path";
    private static final String DEFAULT_DATABASE_PATH = "nemocnica.db";

    private String propertyFilePath;
    private Properties properties;


    public static AppProperties getInstance() {
        if (instance == null) {
            instance = new AppProperties();
        }
        return instance;
    }


    private AppProperties() {
        this.properties = new Properties();
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        this.propertyFilePath = rootPath + PROPERTY_FILENAME;

        try (InputStream inputStream = new FileInputStream(propertyFilePath)) {
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("error loading properties from file {}", propertyFilePath, e);
        }
    }

    public String getDatabasenamePath() {
        return properties.getProperty(PROPERTY_DATABASE_PATH, DEFAULT_DATABASE_PATH);
    }

    public void setDatabasenamePath(String path) {
        properties.setProperty(PROPERTY_DATABASE_PATH, path);
        saveProperties();
    }

    public void saveProperties() {

        try (OutputStream outputStream = new FileOutputStream(propertyFilePath)) {
            properties.store(outputStream, "#");
        } catch (IOException e) {
            logger.error("error writting properties from file {}", propertyFilePath, e);
        }
    }
}
