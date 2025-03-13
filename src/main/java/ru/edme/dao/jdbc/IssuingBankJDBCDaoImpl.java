package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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

@Slf4j
@Repository
public class IssuingBankJDBCDaoImpl implements Dao<IssuingBank> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS issuing_bank (
                id BIGSERIAL PRIMARY KEY,
                bic VARCHAR(9) UNIQUE NOT NULL,
                abbreviated_name VARCHAR(255) NOT NULL
            );
            """;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS issuing_bank CASCADE";

    private static final String INSERT = """
            INSERT INTO issuing_bank (bic, abbreviated_name) 
            VALUES (?, ?) 
            ON CONFLICT (bic) DO NOTHING RETURNING id;
            """;

    private static final String GET_ALL = "SELECT * FROM issuing_bank;";
    private static final String GET_BY_ID = "SELECT * FROM issuing_bank WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE issuing_bank SET bic = ?, abbreviated_name = ? 
            WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM issuing_bank WHERE id = ?;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE issuing_bank RESTART IDENTITY CASCADE;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
            log.info("Created table 'IssuingBank' successfully.");
        } catch (SQLException e) {
            log.error("Error creating IssuingBank table", e);
            throw new RuntimeException("Error creating IssuingBank table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            log.info("Dropped IssuingBank table successfully.");
        } catch (SQLException e) {
            log.error("Error dropping IssuingBank table", e);
            throw new RuntimeException("Error dropping IssuingBank table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            log.info("IssuingBank table cleared.");
        } catch (SQLException e) {
            log.error("Error clearing IssuingBank table", e);
            throw new RuntimeException("Error clearing IssuingBank table", e);
        }
    }

    @Override
    public void insert(IssuingBank issuingBank) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, issuingBank.getBic());
            pstmt.setString(2, issuingBank.getAbbreviatedName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        issuingBank.setId(rs.getLong(1));
                        log.info("Inserted new IssuingBank: {}", issuingBank);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error inserting into IssuingBank table", e);
            throw new RuntimeException("Error inserting into IssuingBank table", e);
        }
    }

    @Override
    public List<IssuingBank> getAll() {
        List<IssuingBank> issuingBanks = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                issuingBanks.add(mapResultSetToIssuingBank(rs));
            }
        } catch (SQLException e) {
            log.error("Error fetching all IssuingBanks", e);
            throw new RuntimeException("Error fetching all IssuingBanks", e);
        }
        return issuingBanks;
    }

    @Override
    public Optional<IssuingBank> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToIssuingBank(rs));
                }
            }
            log.warn("IssuingBank with id {} not found.", id);
        } catch (SQLException e) {
            log.error("Error getting IssuingBank by ID", e);
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

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                log.info("Updated IssuingBank: {}", issuingBank);
            } else {
                log.warn("No IssuingBank found to update with id {}", issuingBank.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating IssuingBank", e);
            throw new RuntimeException("Error updating IssuingBank", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                log.info("Deleted IssuingBank with id {}", id);
            } else {
                log.warn("No IssuingBank found to delete with id {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting IssuingBank", e);
            throw new RuntimeException("Error deleting IssuingBank", e);
        }
    }

    private IssuingBank mapResultSetToIssuingBank(ResultSet rs) throws SQLException {
        return IssuingBank.builder()
                .id(rs.getLong("id"))
                .bic(rs.getString("bic"))
                .abbreviatedName(rs.getString("abbreviated_name"))
                .build();
    }
}
