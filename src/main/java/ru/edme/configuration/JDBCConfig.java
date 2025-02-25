package ru.edme.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConfig {
    private static final Logger logger = LogManager.getLogger(JDBCConfig.class);
    private static final String URL = "URL";
    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";


    public static Connection getConnection() {
        Connection connection = null;
        try{
            logger.info("Trying to connect to database...");
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    PropertiesUtil.getProperty(URL),
                    PropertiesUtil.getProperty(USER),
                    PropertiesUtil.getProperty(PASSWORD)
            );
            logger.info("Successfully connected to database.");
            }catch (ClassNotFoundException e){
                logger.error("PostgreSQL JDBC Driver class not found.",e);
        }catch (SQLException e){
            logger.error("Can't connect to database.", e);
            throw new RuntimeException("Error connecting to the database",e);
        }
        return connection ;
    }
}
