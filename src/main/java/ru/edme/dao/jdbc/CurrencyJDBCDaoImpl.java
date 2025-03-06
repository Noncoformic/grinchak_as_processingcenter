package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyJDBCDaoImpl implements Dao<Currency> {
    private static final Logger logger = LogManager.getLogger(CurrencyJDBCDaoImpl.class);
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS currency (
                id BIGSERIAL PRIMARY KEY,
                currency_digital_code VARCHAR(3) UNIQUE NOT NULL,
                currency_letter_code VARCHAR(3) UNIQUE NOT NULL,
                currency_name VARCHAR(100) NOT NULL
            );
            """;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS currency CASCADE";
    private static final String INSERT = "INSERT INTO currency (currency_digital_code, currency_letter_code, currency_name) VALUES (?, ?, ?);";
    private static final String GET_ALL = "SELECT * FROM currency;";
    private static final String GET_BY_ID = "SELECT * FROM currency WHERE id = ?;";
    private static final String UPDATE = "UPDATE currency SET currency_digital_code = ?, currency_letter_code = ?, currency_name = ? WHERE id = ?;";
    private static final String DELETE = "DELETE FROM currency WHERE id = ?;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE currency RESTART IDENTITY CASCADE;";
    private static final String CHECK_EXISTENCE_DIGITAL = "SELECT * FROM currency WHERE currency_digital_code = ?;";
    private static final String CHECK_EXISTENCE_LETTER = "SELECT * FROM currency WHERE currency_letter_code = ?;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            logger.info("Creating table 'Currency'...");
            stmt.executeUpdate(CREATE_TABLE);
            logger.info("Created table 'Currency'!");
        } catch (SQLException e) {
            logger.error("Error creating Currency table", e);
            throw new RuntimeException("Error creating Currency table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            logger.info("Dropping Currency table....");
            stmt.executeUpdate(DROP_TABLE);
            logger.info("Dropped Currency table successfully");
        } catch (SQLException e) {
            logger.error("Error dropping Currency table", e);
            throw new RuntimeException("Error dropping Currency table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
        } catch (SQLException e) {
            logger.error("Error clearing Currency table", e);
            throw new RuntimeException("Error clearing Currency table", e);
        }
    }

    @Override
    public void insert(Currency currency) {
        try (Connection connection = JDBCConfig.getConnection()) {
            // Проверка на существование
            try (PreparedStatement checkStmtDigital = connection.prepareStatement(CHECK_EXISTENCE_DIGITAL)) {
                logger.info("Checking if Currency with digital code already exists...");
                checkStmtDigital.setString(1, currency.getCurrencyDigitalCode());
                ResultSet rsDigital = checkStmtDigital.executeQuery();
                if (rsDigital.next()) {
                    logger.error(" ⚠\uFE0F Currency with digital code " + currency.getCurrencyDigitalCode() + " already exists.");
                    return;
                }
            }
            try (PreparedStatement checkStmtLetter = connection.prepareStatement(CHECK_EXISTENCE_LETTER)) {
                logger.info("Checking if Currency with letter code already exists...");
                checkStmtLetter.setString(1, currency.getCurrencyLetterCode());
                ResultSet rsLetter = checkStmtLetter.executeQuery();
                if (rsLetter.next()) {
                    logger.error(" ⚠\uFE0F Currency with letter code " + currency.getCurrencyLetterCode() + " already exists.");
                    return;
                }
            }

            try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
                pstmt.setString(1, currency.getCurrencyDigitalCode());
                pstmt.setString(2, currency.getCurrencyLetterCode());
                pstmt.setString(3, currency.getCurrencyName());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            logger.error("Error inserting into Currency table", e);
            throw new RuntimeException("Error inserting into Currency table", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            logger.info("Currency deleted");
        } catch (SQLException e) {
            logger.error("Error deleting Currency", e);
            throw new RuntimeException("Error deleting Currency", e);
        }
    }

    @Override
    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                currencies.add(Currency.builder()
                        .id(rs.getLong("id"))
                        .currencyDigitalCode(rs.getString("currency_digital_code"))
                        .currencyLetterCode(rs.getString("currency_letter_code"))
                        .currencyName(rs.getString("currency_name"))
                        .build());
            }
        } catch (SQLException e) {
            logger.error("Error fetching all Currencies", e);
            throw new RuntimeException("Error fetching all Currencies", e);
        }
        return currencies;
    }

    @Override
    public Optional<Currency> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(Currency.builder()
                        .id(rs.getLong("id"))
                        .currencyDigitalCode(rs.getString("currency_digital_code"))
                        .currencyLetterCode(rs.getString("currency_letter_code"))
                        .currencyName(rs.getString("currency_name"))
                        .build());
            } else {
                logger.error("Currency with id {} not found.", id);
            }
        } catch (SQLException e) {
            logger.error("Error getting Currency by ID", e);
            throw new RuntimeException("Error getting Currency by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(Currency currency) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, currency.getCurrencyDigitalCode());
            pstmt.setString(2, currency.getCurrencyLetterCode());
            pstmt.setString(3, currency.getCurrencyName());
            pstmt.setLong(4, currency.getId());
            pstmt.executeUpdate();
            logger.info("Currency updated: {}", currency);
        } catch (SQLException e) {
            logger.error("Error updating Currency", e);
            throw new RuntimeException("Error updating Currency", e);
        }
    }
}