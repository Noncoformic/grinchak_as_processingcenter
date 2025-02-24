package ru.edme.dao.jdbc;

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
    private static final String UPDATE = """
            UPDATE card_status SET card_status_name = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM card_status WHERE id = ?;";
    private static final String CHECK_EXISTENCE = " SELECT * FROM card_status WHERE card_status_name = ?;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating CardStatus table", e);
        }
    }

    @Override
    public void dropTable() {


    }

    @Override
    public void clearTable() {

    }

    @Override
    public void insert(CardStatus cardStatus) {
    try (Connection connection = JDBCConfig.getConnection()) {
      // Проверяем, существует ли уже такой статус
      try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
        checkStmt.setString(1, cardStatus.getCardStatusName());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
          System.out.println(
              "⚠️ CardStatus '"
                  + cardStatus.getCardStatusName()
                  + "' already exists. Skipping insert.");
          return; // Просто пропускаем вставку
        }
      }
        try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
            pstmt.setString(1, cardStatus.getCardStatusName());
            pstmt.executeUpdate();
            System.out.println("✅ CardStatus added: " + cardStatus.getCardStatusName());
        }

    } catch (SQLException e) {
        throw new RuntimeException("Error inserting into CardStatus table", e);
    }
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
            throw new RuntimeException("Error fetching all CardStatuses", e);
        }
        return statuses;
    }

    @Override
    public CardStatus getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new CardStatus(
                        rs.getLong("id"),
                        rs.getString("card_status_name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching CardStatus by ID", e);
        }
        return null;
    }

    @Override
    public void update(CardStatus cardStatus) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, cardStatus.getCardStatusName());
            pstmt.setLong(2, cardStatus.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating CardStatus", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting CardStatus", e);
        }
    }
}
