package ru.edme.dao.jdbc;

import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.PaymentSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PaymentSystemJDBCDaoImpl implements Dao<PaymentSystem> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS payment_system (
                id SERIAL PRIMARY KEY,
                payment_system_name VARCHAR(255) NOT NULL UNIQUE
            );
            """;

    private static final String INSERT = """
            INSERT INTO payment_system (payment_system_name) VALUES (?) RETURNING id;
            """;

    private static final String GET_ALL = "SELECT * FROM payment_system;";
    private static final String GET_BY_ID = "SELECT * FROM payment_system WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE payment_system SET payment_system_name = ? WHERE payment_system_name = ?
            """;
    private static final String DELETE = "DELETE FROM payment_system WHERE id = ?;";
    private static final String CHECK_EXISTENCE = """
        SELECT COUNT(*) FROM payment_system WHERE payment_system_name = ?;
        """;

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating PaymentSystem table", e);
        }
    }

    @Override
    public void dropTable() {

    }

    @Override
    public void clearTable() {

    }

    @Override
    public void insert(PaymentSystem paymentSystem) {
        try (Connection connection = JDBCConfig.getConnection()) {
            // Проверяем, существует ли уже такая платежная система
            try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
                checkStmt.setString(1, paymentSystem.getPaymentSystemName());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    System.out.println("⚠️ PaymentSystem '" + paymentSystem.getPaymentSystemName() + "' already exists. Skipping insert.");
                    return; // Просто пропускаем вставку
                }
            }

            // Если платежная система не найдена, вставляем её
            try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
                pstmt.setString(1, paymentSystem.getPaymentSystemName());
                pstmt.executeUpdate();
                System.out.println("✅ PaymentSystem added: " + paymentSystem.getPaymentSystemName());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into PaymentSystem table", e);
        }
    }

    @Override
    public List<PaymentSystem> getAll() {
        List<PaymentSystem> paymentSystems = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                paymentSystems.add(new PaymentSystem(
                        rs.getLong("id"),
                        rs.getString("payment_system_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all PaymentSystems", e);
        }
        return paymentSystems;
    }

    @Override
    public PaymentSystem getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new PaymentSystem(
                        rs.getLong("id"),
                        rs.getString("payment_system_name")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching PaymentSystem by ID", e);
        }
        return null;
    }

    @Override
    public void update(PaymentSystem paymentSystem) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, paymentSystem.getPaymentSystemName());
            pstmt.setLong(2, paymentSystem.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating PaymentSystem", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting PaymentSystem", e);
        }
    }
}
