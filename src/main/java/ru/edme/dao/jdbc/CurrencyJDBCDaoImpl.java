package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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

@Slf4j
@Repository
public class CurrencyJDBCDaoImpl implements Dao<Currency> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS currency (
                id BIGSERIAL PRIMARY KEY,
                currency_digital_code VARCHAR(3) UNIQUE NOT NULL,
                currency_letter_code VARCHAR(3) UNIQUE NOT NULL,
                currency_name VARCHAR(100) NOT NULL
            );
            """;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS currency CASCADE";

    private static final String INSERT = """
        INSERT INTO currency (currency_digital_code, currency_letter_code, currency_name) 
        VALUES (?, ?, ?) 
        ON CONFLICT DO NOTHING
        RETURNING id;
        """;

    private static final String GET_ALL = "SELECT * FROM currency;";
    private static final String GET_BY_ID = "SELECT * FROM currency WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE currency SET currency_digital_code = ?, currency_letter_code = ?, currency_name = ? 
            WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM currency WHERE id = ?;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE currency RESTART IDENTITY CASCADE;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
            log.info("Created table 'Currency' successfully.");
        } catch (SQLException e) {
            log.error("Error creating Currency table", e);
            throw new RuntimeException("Error creating Currency table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            log.info("Dropped Currency table successfully.");
        } catch (SQLException e) {
            log.error("Error dropping Currency table", e);
            throw new RuntimeException("Error dropping Currency table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            log.info("Currency table cleared.");
        } catch (SQLException e) {
            log.error("Error clearing Currency table", e);
            throw new RuntimeException("Error clearing Currency table", e);
        }
    }

    @Override
    public void insert(Currency currency) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, currency.getCurrencyDigitalCode());
            pstmt.setString(2, currency.getCurrencyLetterCode());
            pstmt.setString(3, currency.getCurrencyName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        currency.setId(rs.getLong(1));
                        log.info("Inserted new Currency: {}", currency);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error inserting into Currency table", e);
            throw new RuntimeException("Error inserting into Currency table", e);
        }
    }

    @Override
    public List<Currency> getAll() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                currencies.add(mapResultSetToCurrency(rs));
            }
        } catch (SQLException e) {
            log.error("Error fetching all Currencies", e);
            throw new RuntimeException("Error fetching all Currencies", e);
        }
        return currencies;
    }

    @Override
    public Optional<Currency> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToCurrency(rs));
                }
            }
            log.warn("Currency with id {} not found.", id);
        } catch (SQLException e) {
            log.error("Error getting Currency by ID", e);
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

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                log.info("Updated Currency: {}", currency);
            } else {
                log.warn("No Currency found to update with id {}", currency.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating Currency", e);
            throw new RuntimeException("Error updating Currency", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                log.info("Deleted Currency with id {}", id);
            } else {
                log.warn("No Currency found to delete with id {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting Currency", e);
            throw new RuntimeException("Error deleting Currency", e);
        }
    }

    private Currency mapResultSetToCurrency(ResultSet rs) throws SQLException {
        return Currency.builder()
                .id(rs.getLong("id"))
                .currencyDigitalCode(rs.getString("currency_digital_code"))
                .currencyLetterCode(rs.getString("currency_letter_code"))
                .currencyName(rs.getString("currency_name"))
                .build();
    }
}
