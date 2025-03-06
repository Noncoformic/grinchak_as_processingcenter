package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

public class CardStatusJDBCDaoImpl implements Dao<CardStatus> {
    private static final Logger logger = LogManager.getLogger(CardStatusJDBCDaoImpl.class);
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
    private static final CardStatusJDBCDaoImpl INSTANCE = new CardStatusJDBCDaoImpl();

    public static CardStatusJDBCDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            logger.error("Error creating CardStatus table", e);
            throw new RuntimeException("Error creating CardStatus table", e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping CardStatus table....");
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            logger.info("Dropped CardStatus table successfully");
        } catch (SQLException e) {
            logger.error("Error dropping CardStatus table", e);
            throw new RuntimeException("Error dropping CardStatus table", e);
        }
    }

    @Override
    public void clearTable() {
        logger.info("Clearing all data from CardStatus table");
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            logger.info("CardStatus have been cleared");
        } catch (SQLException e) {
            logger.error("Error clearing CardStatus table", e);
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
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error inserting into CardStatus table", e);
            throw new RuntimeException("Error inserting into CardStatus table", e);
        }
    }

    public Optional<CardStatus> getCardStatusByName(String cardStatusName) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_NAME)) {
            pstmt.setString(1, cardStatusName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new CardStatus(
                        rs.getLong("id"),
                        rs.getString("card_status_name")
                ));
            } else {
                logger.error("CardStatus with name {} not found", cardStatusName);
            }
        } catch (SQLException e) {
            logger.error("Error fetching CardStatus by name", e);
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
            logger.error("Error fetching all CardStatuses", e);
            throw new RuntimeException("Error fetching all CardStatuses", e);
        }
        return statuses;
    }

    @Override
    public Optional<CardStatus> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new CardStatus(
                        rs.getLong("id"),
                        rs.getString("card_status_name")
                ));
            } else {
                logger.error("CardStatus with id {} not found", id);
            }
        } catch (SQLException e) {
            logger.error("Error fetching CardStatus by ID", e);
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
            pstmt.executeUpdate();
            logger.info("CardStatus updated: " + cardStatus);
        } catch (SQLException e) {
            logger.error("Error updating CardStatus", e);
            throw new RuntimeException("Error updating CardStatus", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            logger.info("CardStatus deleted: " + id);
        } catch (SQLException e) {
            logger.error("Error deleting CardStatus", e);
            throw new RuntimeException("Error deleting CardStatus", e);
        }
    }
}
