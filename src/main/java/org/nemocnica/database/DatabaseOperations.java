package org.nemocnica.database;

import org.nemocnica.utils.AppProperties;
import org.nemocnica.utils.Entry;
import org.nemocnica.utils.XmlToObjects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.nemocnica.utils.UserMessageException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;
import java.util.Map;


public class DatabaseOperations {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseOperations.class.getName());
    private static final String PARAM_CREATE = ";create=true";


    public static Connection connectToDatabase(String pathName) throws UserMessageException {
        String connectionString = getConnectionString(pathName);
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            return connection;
        } catch (SQLException exception) {
            String message = "cannot connect database \"" + pathName + "\"";
            logger.error(message, exception);
            throw new UserMessageException(message + "\n" + exception.getMessage());
        }
    }

    private static String getConnectionString(String pathName) {
        return "jdbc:derby:" + pathName + ";";
    }


    public static void createEmptyDatabase(String pathName) throws UserMessageException {
        File directory = new File(pathName);
        if (directory.exists()) {
            String message = "cannot create database \"" + pathName + "\" file or directory already exists";
            logger.error(message);
            throw new UserMessageException(message);
        }
        String connectionString = getConnectionString(pathName) + PARAM_CREATE;
        try {
            Connection connection = DriverManager.getConnection(connectionString);
            connection.close();
        } catch (SQLException exception) {
            logger.error("cannot create database \"{}\"", pathName,  exception);
            throw new UserMessageException("cannot create database\n" + exception.getMessage());
        }
    }

    public static void createTables(String pathName) throws UserMessageException {
        List<Entry> tablesToCreate = XmlToObjects.getList("/sql/create.xml");
        String key=null;
        try (
                Connection connection = connectToDatabase(pathName);
                Statement stmt = connection.createStatement()) {
            for(Entry entry : tablesToCreate){
                key= entry.getKey();
                stmt.executeUpdate(entry.getValue());
            }

        } catch (SQLException exception) {
            logger.error("error while creating tables {} ",key, exception);
            throw new UserMessageException("error while creating tables "+key+"\n"+exception.getMessage());
        }
    }

    public static void addTestData(String pathName) throws UserMessageException {
        try (
                Connection connection = connectToDatabase(pathName);
                Statement stmt = connection.createStatement();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(DatabaseOperations.class.getResourceAsStream("/sql/insert.txt")) )) {

            String line;
            while ((line = br.readLine()) != null) {
                stmt.executeUpdate(line);
            }
            connection.commit();
        } catch (SQLException | IOException exception) {
            logger.error("error while inserting data", exception);
            throw new UserMessageException("error while inserting data "+exception.getMessage());
        }
    }

    public static void deleteFromTable(Connection connection, String tableName, String idName, String idValue) throws UserMessageException {
        String strStatement = "DELETE FROM " + tableName + " WHERE " + idName + "=" + idValue;
        executeStatement(connection,strStatement);
    }

    public static void executeStatement(Connection connection, String strStatement) throws UserMessageException {
        try(Statement stmt = connection.createStatement()){
            stmt.executeUpdate(strStatement);
            connection.commit();
        } catch (SQLException exception) {
            logger.error("error while executing statement:\n"+strStatement, exception);
            throw new UserMessageException(exception.getMessage());
        }
    }


    public static void main(String args[]) throws UserMessageException, SQLException {
        //wypisz co w tablicy doktorzy
        Connection connection = connectToDatabase(AppProperties.getInstance().getDatabasenamePath());
        Statement stmt = connection.createStatement();
        String sql = "SELECT doctor_id,DOCTORS.name,surname,med_specialisation,position,chief_doctor_id,DOCTORS.department_id,DEPARTMENTS.name,salary "+
                "FROM DOCTORS " +
                "LEFT JOIN DEPARTMENTS ON DOCTORS.department_id=DEPARTMENTS.department_id";



        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        while(rs.next()){
            for( int column=1; column<=columnCount; column++){
                Object obj = rs.getObject(column);
                String str = obj != null ? obj.toString() : "null";
                System.out.print(str+"  ");
            }
            //Display values
            System.out.println();
        }
        rs.close();
        connection.close();
    }
}
