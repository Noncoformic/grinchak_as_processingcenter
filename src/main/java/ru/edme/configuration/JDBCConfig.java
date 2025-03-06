package ru.edme.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCConfig {

    private static final Logger logger = LogManager.getLogger(JDBCConfig.class);

    private static final HikariDataSource dataSource = createDataSource();

    private static HikariDataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(PropertiesUtil.get("db.url"));
        config.setUsername(PropertiesUtil.get("db.username"));
        config.setPassword(PropertiesUtil.get("db.password"));
        config.setDriverClassName(PropertiesUtil.get("db.driver"));
        return new HikariDataSource(config);
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.error("Failed to get connection from DataSource", e);
            throw new RuntimeException("Failed to get connection from DataSource", e);
        }
    }
}
