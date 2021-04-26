package org.nemocnica.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class AppProperties {
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
        try {
            properties.load(new FileInputStream(propertyFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDatabasenamePath(){
        return properties.getProperty(PROPERTY_DATABASE_PATH,DEFAULT_DATABASE_PATH);
    }

    public void setDatabasenamePath(String path){
        properties.setProperty(PROPERTY_DATABASE_PATH,path);
        saveProperties();
    }

    public void saveProperties(){
        try {
            properties.store(new FileOutputStream(propertyFilePath),"#");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
