package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.CardStatus;

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
public class CardStatusJDBCDaoImpl implements Dao<CardStatus> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS card_status (
                id SERIAL PRIMARY KEY,
                card_status_name VARCHAR(255) NOT NULL UNIQUE
            );
            """;

    private static final String INSERT = """
            INSERT INTO card_status (card_status_name) VALUES (?) RETURNING id;
            """;

    private static final String GET_ALL = "SELECT * FROM card_status;";
    private static final String GET_BY_ID = "SELECT * FROM card_status WHERE id = ?;";
    private static final String GET_BY_NAME = "SELECT * FROM card_status WHERE card_status_name = ?;";
    private static final String UPDATE = """
            UPDATE card_status SET card_status_name = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM card_status WHERE id = ?;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS card_status CASCADE";
    private static final String CLEAR_TABLE = "DELETE FROM card_status;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
            log.info("Created CardStatus table successfully.");
        } catch (SQLException e) {
            log.error("Error creating CardStatus table", e);
            throw new RuntimeException("Error creating CardStatus table", e);
        }
    }

    @Override
    public void dropTable() {
        log.info("Dropping CardStatus table...");
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            log.info("Dropped CardStatus table successfully.");
        } catch (SQLException e) {
            log.error("Error dropping CardStatus table", e);
            throw new RuntimeException("Error dropping CardStatus table", e);
        }
    }

    @Override
    public void clearTable() {
        log.info("Clearing all data from CardStatus table...");
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            log.info("CardStatus table cleared.");
        } catch (SQLException e) {
            log.error("Error clearing CardStatus table", e);
            throw new RuntimeException("Error clearing CardStatus table", e);
        }
    }

    @Override
    public void insert(CardStatus cardStatus) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cardStatus.getCardStatusName());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        cardStatus.setId(rs.getLong(1));
                        log.info("Inserted new CardStatus: {}", cardStatus);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error inserting into CardStatus table", e);
            throw new RuntimeException("Error inserting into CardStatus table", e);
        }
    }

    public Optional<CardStatus> getCardStatusByName(String cardStatusName) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_NAME)) {
            pstmt.setString(1, cardStatusName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CardStatus(
                            rs.getLong("id"),
                            rs.getString("card_status_name")
                    ));
                }
            }
            log.warn("CardStatus with name '{}' not found.", cardStatusName);
        } catch (SQLException e) {
            log.error("Error fetching CardStatus by name", e);
            throw new RuntimeException("Error fetching CardStatus by name", e);
        }
        return Optional.empty();
    }

    @Override
    public List<CardStatus> getAll() {
        List<CardStatus> statuses = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                statuses.add(new CardStatus(
                        rs.getLong("id"),
                        rs.getString("card_status_name")
                ));
            }
        } catch (SQLException e) {
            log.error("Error fetching all CardStatuses", e);
            throw new RuntimeException("Error fetching all CardStatuses", e);
        }
        return statuses;
    }

    @Override
    public Optional<CardStatus> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CardStatus(
                            rs.getLong("id"),
                            rs.getString("card_status_name")
                    ));
                }
            }
            log.warn("CardStatus with id {} not found.", id);
        } catch (SQLException e) {
            log.error("Error fetching CardStatus by ID", e);
            throw new RuntimeException("Error fetching CardStatus by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(CardStatus cardStatus) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, cardStatus.getCardStatusName());
            pstmt.setLong(2, cardStatus.getId());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                log.info("Updated CardStatus: {}", cardStatus);
            } else {
                log.warn("No CardStatus found to update with id {}", cardStatus.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating CardStatus", e);
            throw new RuntimeException("Error updating CardStatus", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                log.info("Deleted CardStatus with id {}", id);
            } else {
                log.warn("No CardStatus found to delete with id {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting CardStatus", e);
            throw new RuntimeException("Error deleting CardStatus", e);
        }
    }
}
