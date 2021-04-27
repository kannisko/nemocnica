package org.nemocnica.database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nemocnica.utils.UserMessageException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseOperations {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperations.class.getName());
    private static final String PARAM_CREATE = ";create=true";


    public static Connection connectToDatabase(String pathName) throws UserMessageException {
        String connectionString = getConnectionString(pathName);
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            return connection;
        }catch (SQLException exception){
            String message = "cannot connect database \"" + pathName + "\"";
            logger.error(message,exception);
            throw new UserMessageException(message+"\n"+exception.getMessage());
        }
    }

    private static String getConnectionString(String pathName) {
        return "jdbc:derby:" + pathName + ";";
    }


    public static void createEmptyDatabase(String pathName) throws UserMessageException{
        File directory  = new File(pathName);
        if( directory.exists() ) {
            if(directory.isFile()){
                String message =  "cannot create database \""+pathName+"\" is file";
                logger.error(message);
                throw new UserMessageException(message);
            }

            if( directory.isDirectory() && directory.listFiles().length != 0 ) {
                String message =  "cannot create database \""+pathName+"\" is not empty directory";
                logger.error(message);
                throw new UserMessageException(message);
            }
        }
        String connectionString = getConnectionString(pathName) + PARAM_CREATE;
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            connection.close();
        }catch (SQLException exception){
            logger.error("cannot create database \"{}\" {}",pathName,exception.getMessage(),exception);
            throw new UserMessageException("cannot create database\n"+exception.getMessage());
        }
    }
}
