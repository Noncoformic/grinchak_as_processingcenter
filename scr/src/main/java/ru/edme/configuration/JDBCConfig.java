package ru.edme.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConfig {
    private static final Logger logger = LogManager.getLogger(JDBCConfig.class);
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "grinchak";
    private static final String PASSWORD = "zdnegbrt232";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't load JDBC driver.", e);
        }
    }
    public static Connection getConnection() {
        try{
            logger.info("Trying to connect to database...");
            Connection connection =  DriverManager.getConnection(URL,USER,PASSWORD);
            logger.info("Successfully connected to database.");
            return connection;
        }catch (SQLException e){
            logger.error("Can't connect to database.", e);
            throw new RuntimeException("Error connecting to the database",e);
        }
    }

}
