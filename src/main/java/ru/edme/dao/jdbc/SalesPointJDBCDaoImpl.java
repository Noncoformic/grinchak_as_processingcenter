package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.SalesPoint;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SalesPointJDBCDaoImpl implements Dao<SalesPoint> {
    private static final Logger logger = LogManager.getLogger(SalesPointJDBCDaoImpl.class);

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS sales_point (
                id SERIAL PRIMARY KEY,
                pos_name VARCHAR(255) NOT NULL,
                pos_address VARCHAR(255) NOT NULL,
                pos_inn VARCHAR(12) NOT NULL,
                acquiring_bank_id BIGINT NOT NULL REFERENCES acquiring_bank(id)
            );
            """;

    private static final String INSERT = """
            INSERT INTO sales_point (pos_name, pos_address, pos_inn, acquiring_bank_id)
            VALUES (?, ?, ?, ?);
            """;
    private static final String CLEAR_TABLE = "TRUNCATE TABLE sales_point RESTART IDENTITY CASCADE;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS sales_point CASCADE;";

    private static final String GET_ALL = "SELECT * FROM sales_point;";
    private static final String GET_BY_ID = "SELECT * FROM sales_point WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE sales_point SET pos_name = ?, pos_address = ?, pos_inn = ?, acquiring_bank_id = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM sales_point WHERE id = ?;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            logger.info("Creating table 'SalesPoint'...");
            stmt.executeUpdate(CREATE_TABLE);
            logger.info("Created table 'SalesPoint'!");
        } catch (SQLException e) {
            logger.error("Error creating SalesPoint table", e);
            throw new RuntimeException("Error creating SalesPoint table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            logger.info("Dropping SalesPoint table....");
            stmt.executeUpdate(DROP_TABLE);
            logger.info("Dropped SalesPoint table successfully");
        } catch (SQLException e) {
            logger.error("Error dropping SalesPoint table", e);
            throw new RuntimeException("Error dropping SalesPoint table", e);
        }

    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            logger.info("Cleared table SalesPoint.");
        } catch (SQLException e) {
            logger.error("Error clearing SalesPoint table", e);
            throw new RuntimeException("Error clearing SalesPoint table", e);
        }

    }

    @Override
    public void insert(SalesPoint salesPoint) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, salesPoint.getPosName());
            pstmt.setString(2, salesPoint.getPosAddress());
            pstmt.setString(3, salesPoint.getPosInn());
            pstmt.setLong(4, salesPoint.getAcquiringBankId());
            pstmt.executeUpdate();
            logger.info("SalesPoint inserted: {}", salesPoint);
        } catch (SQLException e) {
            logger.error("Error inserting into SalesPoint table", e);
            throw new RuntimeException("Error inserting into SalesPoint table", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            logger.info("SalesPoint deleted. Id: {}", id);
        } catch (SQLException e) {
            logger.error("Error deleting SalesPoint", e);
            throw new RuntimeException("Error deleting SalesPoint", e);
        }
    }

    @Override
    public List<SalesPoint> getAll() {
        List<SalesPoint> salesPoints = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                salesPoints.add(SalesPoint.builder()
                        .id(rs.getLong("id"))
                        .posName(rs.getString("pos_name"))
                        .posAddress(rs.getString("pos_address"))
                        .posInn(rs.getString("pos_inn"))
                        .acquiringBankId(rs.getLong("acquiring_bank_id"))
                        .build());
            }
            logger.info("Fetched all sales points");
        } catch (SQLException e) {
            logger.error("Error fetching all sales points", e);
            throw new RuntimeException("Error fetching all sales points", e);
        }
        return salesPoints;
    }

    @Override
    public Optional<SalesPoint> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                logger.info("Fetched sales point. Id: {}", id);
                return Optional.of(SalesPoint.builder()
                        .id(rs.getLong("id"))
                        .posName(rs.getString("pos_name"))
                        .posAddress(rs.getString("pos_address"))
                        .posInn(rs.getString("pos_inn"))
                        .acquiringBankId(rs.getLong("acquiring_bank_id"))
                        .build());
            } else {
                logger.warn("Sales point with id {} not found.", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error fetching sales point by ID", e);
            throw new RuntimeException("Error fetching sales point by ID", e);
        }
    }

    @Override
    public void update(SalesPoint salesPoint) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, salesPoint.getPosName());
            pstmt.setString(2, salesPoint.getPosAddress());
            pstmt.setString(3, salesPoint.getPosInn());
            pstmt.setLong(4, salesPoint.getAcquiringBankId());
            pstmt.setLong(5, salesPoint.getId());
            pstmt.executeUpdate();
            logger.info("SalesPoint updated: {}", salesPoint);
        } catch (SQLException e) {
            logger.error("Error updating SalesPoint", e);
            throw new RuntimeException("Error updating SalesPoint", e);
        }
    }
}