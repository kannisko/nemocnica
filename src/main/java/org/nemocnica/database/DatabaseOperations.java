package org.nemocnica.database;
import org.nemocnica.utils.UserMessageException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseOperations {
    private static final String PARAM_CREATE = ";create=true";


    public static Connection connectToDatabase(String pathName) throws UserMessageException {
        String connectionString = getConnectionString(pathName);
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            return connection;
        }catch (SQLException exception){
            //log!!!
            throw new UserMessageException("cannot connect database\n"+exception.getMessage());
        }
    }

    private static String getConnectionString(String pathName) {
        return "jdbc:derby:" + pathName + ";";
    }


    public static void createEmptyDatabase(String pathName) throws UserMessageException{
        File directory  = new File(pathName);
        if( directory.exists() ) {
            if(directory.isFile()){
                throw new UserMessageException( "cannot create database\n"+pathName+" is file");
            }

            if( directory.isDirectory() && directory.listFiles().length != 0 ) {
                throw new UserMessageException( "cannot create database\n"+pathName+" is non empty directory");
            }
        }
        String connectionString = getConnectionString(pathName) + PARAM_CREATE;
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            connection.close();
        }catch (SQLException exception){
            //log!!!
            throw new UserMessageException("cannot create database\n"+exception.getMessage());
        }
    }
}
