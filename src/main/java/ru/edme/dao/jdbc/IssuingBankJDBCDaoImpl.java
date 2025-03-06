package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.IssuingBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IssuingBankJDBCDaoImpl implements Dao<IssuingBank> {
    private static final Logger logger = LogManager.getLogger(IssuingBankJDBCDaoImpl.class);
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS issuing_bank (
                id BIGSERIAL PRIMARY KEY,
                bic VARCHAR(9) UNIQUE NOT NULL,
                abbreviated_name VARCHAR(255) NOT NULL
            );
            """;
    private static final String DROP_COLUMN = "ALTER TABLE issuing_bank DROP COLUMN IF EXISTS bin;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS issuing_bank CASCADE";
    private static final String INSERT = "INSERT INTO issuing_bank (bic, abbreviated_name) VALUES (?, ?);";
    private static final String GET_ALL = "SELECT * FROM issuing_bank;";
    private static final String GET_BY_ID = "SELECT * FROM issuing_bank WHERE id = ?;";
    private static final String UPDATE = "UPDATE issuing_bank SET bic = ?, abbreviated_name = ? WHERE id = ?;";
    private static final String DELETE = "DELETE FROM issuing_bank WHERE id = ?;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE issuing_bank RESTART IDENTITY CASCADE;";
    private static final String CHECK_EXISTENCE = "SELECT * FROM issuing_bank WHERE bic = ?;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            logger.info("Creating table 'IssuingBank'...");
            stmt.executeUpdate(CREATE_TABLE);
            logger.info("Created table 'IssuingBank'!");
        } catch (SQLException e) {
            logger.error("Error creating IssuingBank table", e);
            throw new RuntimeException("Error creating IssuingBank table", e);
        }
    }
    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            logger.info("Dropping IssuingBank table....");
            stmt.executeUpdate(DROP_TABLE);
            logger.info("Dropped IssuingBank table successfully");
        } catch (SQLException e) {
            logger.error("Error dropping IssuingBank table", e);
            throw new RuntimeException("Error dropping IssuingBank table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
        } catch (SQLException e) {
            logger.error("Error clearing IssuingBank table", e);
            throw new RuntimeException("Error clearing IssuingBank table", e);
        }
    }

    @Override
    public void insert(IssuingBank issuingBank) {
        try (Connection connection = JDBCConfig.getConnection()) {
            // Проверка на существование
            try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
                logger.info("Checking if IssuingBank already exists...");
                checkStmt.setString(1, issuingBank.getBic());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    logger.error(" ⚠\uFE0F IssuingBank with bic " + issuingBank.getBic() + " already exists.");
                    return;
                }
            }
            try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
                pstmt.setString(1, issuingBank.getBic());
                pstmt.setString(2, issuingBank.getAbbreviatedName());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error inserting into IssuingBank table", e);
            throw new RuntimeException("Error inserting into IssuingBank table", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            logger.info("IssuingBank deleted");
        } catch (SQLException e) {
            logger.error("Error deleting IssuingBank", e);
            throw new RuntimeException("Error deleting IssuingBank", e);
        }
    }

    @Override
    public List<IssuingBank> getAll() {
        List<IssuingBank> issuingBanks = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                issuingBanks.add(IssuingBank.builder()
                        .id(rs.getLong("id"))
                        .bic(rs.getString("bic"))
                        .abbreviatedName(rs.getString("abbreviated_name"))
                        .build());
            }
        } catch (SQLException e) {
            logger.error("Error fetching all IssuingBanks", e);
            throw new RuntimeException("Error fetching all IssuingBanks", e);
        }
        return issuingBanks;
    }

    @Override
    public Optional<IssuingBank> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(IssuingBank.builder()
                        .id(rs.getLong("id"))
                        .bic(rs.getString("bic"))
                        .abbreviatedName(rs.getString("abbreviated_name"))
                        .build());
            } else {
                logger.error("IssuingBank with id {} not found.", id);
            }
        } catch (SQLException e) {
            logger.error("Error getting IssuingBank by ID", e);
            throw new RuntimeException("Error getting IssuingBank by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(IssuingBank issuingBank) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, issuingBank.getBic());
            pstmt.setString(2, issuingBank.getAbbreviatedName());
            pstmt.setLong(3, issuingBank.getId());
            pstmt.executeUpdate();
            logger.info("IssuingBank updated: {}", issuingBank);
        } catch (SQLException e) {
            logger.error("Error updating IssuingBank", e);
            throw new RuntimeException("Error updating IssuingBank", e);
        }
    }
}