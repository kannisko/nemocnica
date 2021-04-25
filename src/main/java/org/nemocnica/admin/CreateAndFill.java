package org.nemocnica.admin;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class CreateAndFill {

    public static Properties getProperties(){
        String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
        String appConfigPath = rootPath + "app.properties";
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return appProps;
    }

    public static void main(String[] args) throws SQLException {
        Properties properties = getProperties();
        String dataBasePath = properties.getProperty("database.path");

        Connection conn = DriverManager.getConnection("jdbc:derby:sample;create=true");

    }

}
