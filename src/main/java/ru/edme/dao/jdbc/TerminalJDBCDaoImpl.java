package ru.edme.dao.jdbc;

import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Terminal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TerminalJDBCDaoImpl implements Dao<Terminal> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS terminal (
                id SERIAL PRIMARY KEY,
                terminal_id VARCHAR(9) NOT NULL UNIQUE,
                mcc_id BIGINT NOT NULL REFERENCES merchant_category_code(id),
                pos_id BIGINT NOT NULL REFERENCES sales_point(id)
            );
            """;

    private static final String CLEAR_TABLE = "TRUNCATE TABLE terminal RESTART IDENTITY CASCADE;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS terminal CASCADE;";
    private static final String INSERT = """
            INSERT INTO terminal (terminal_id, mcc_id, pos_id) VALUES (?, ?, ?) RETURNING id;
            """;
    private static final String GET_BY_ID = "SELECT * FROM terminal WHERE id = ?;";
    private static final String GET_ALL = "SELECT * FROM terminal;";
    private static final String DELETE = "DELETE FROM terminal WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE terminal SET terminal_id = ?, mcc_id = ?, pos_id = ? WHERE id = ?;
            """;

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Terminal table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            System.out.println("✅ Terminal table cleared!");
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing Terminal table", e);
        }
    }

    @Override
    public void insert(Terminal terminal) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, terminal.getTerminalId());
            pstmt.setLong(2, terminal.getMccId());
            pstmt.setLong(3, terminal.getPosId());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                terminal.setId(rs.getLong(1)); // Устанавливаем ID
            }
            System.out.println("✅ Terminal added: " + terminal.getTerminalId() + " (ID: " + terminal.getId() + ")");

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into Terminal table", e);
        }
    }

    @Override
    public Terminal getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {

            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Terminal(
                        rs.getLong("id"),
                        rs.getString("terminal_id"),
                        rs.getLong("mcc_id"),
                        rs.getLong("pos_id")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching Terminal by ID", e);
        }
        return null;
    }

    @Override
    public List<Terminal> getAll() {
        List<Terminal> terminals = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                terminals.add(new Terminal(
                        rs.getLong("id"),
                        rs.getString("terminal_id"),
                        rs.getLong("mcc_id"),
                        rs.getLong("pos_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all Terminals", e);
        }
        return terminals;
    }

    @Override
    public void update(Terminal terminal) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, terminal.getTerminalId());
            pstmt.setLong(2, terminal.getMccId());
            pstmt.setLong(3, terminal.getPosId());
            pstmt.setLong(4, terminal.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Terminal updated: " + terminal.getTerminalId());
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Terminal table", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
            System.out.println("❌ Terminal deleted: ID " + id);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting from Terminal table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            System.out.println("❌ Terminal table dropped!");
        } catch (SQLException e) {
            throw new RuntimeException("Error dropping Terminal table", e);
        }
    }
}
