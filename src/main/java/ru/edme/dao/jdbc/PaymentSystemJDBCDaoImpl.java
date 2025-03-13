package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
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
import java.util.Optional;

@Slf4j
@Repository
public class PaymentSystemJDBCDaoImpl implements Dao<PaymentSystem> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS payment_system (
                id SERIAL PRIMARY KEY,
                payment_system_name VARCHAR(255) NOT NULL UNIQUE
            );
            """;

    private static final String INSERT = """
            INSERT INTO payment_system (payment_system_name) 
            VALUES (?) 
            ON CONFLICT (payment_system_name) DO NOTHING RETURNING id;
            """;

    private static final String GET_ALL = "SELECT * FROM payment_system;";
    private static final String GET_BY_ID = "SELECT * FROM payment_system WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE payment_system SET payment_system_name = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM payment_system WHERE id = ?;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE payment_system RESTART IDENTITY CASCADE;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS payment_system CASCADE;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
            log.info("Created table 'PaymentSystem' successfully.");
        } catch (SQLException e) {
            log.error("Error creating PaymentSystem table", e);
            throw new RuntimeException("Error creating PaymentSystem table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            log.info("Dropped PaymentSystem table successfully.");
        } catch (SQLException e) {
            log.error("Error dropping PaymentSystem table", e);
            throw new RuntimeException("Error dropping PaymentSystem table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            log.info("PaymentSystem table cleared.");
        } catch (SQLException e) {
            log.error("Error clearing PaymentSystem table", e);
            throw new RuntimeException("Error clearing PaymentSystem table", e);
        }
    }

    @Override
    public void insert(PaymentSystem paymentSystem) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, paymentSystem.getPaymentSystemName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        paymentSystem.setId(rs.getLong(1));
                        log.info("Inserted new PaymentSystem: {}", paymentSystem);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error inserting into PaymentSystem table", e);
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
                paymentSystems.add(mapResultSetToPaymentSystem(rs));
            }
        } catch (SQLException e) {
            log.error("Error fetching all PaymentSystems", e);
            throw new RuntimeException("Error fetching all PaymentSystems", e);
        }
        return paymentSystems;
    }

    @Override
    public Optional<PaymentSystem> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToPaymentSystem(rs));
                }
            }
            log.warn("PaymentSystem with id {} not found.", id);
        } catch (SQLException e) {
            log.error("Error getting PaymentSystem by ID", e);
            throw new RuntimeException("Error getting PaymentSystem by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(PaymentSystem paymentSystem) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, paymentSystem.getPaymentSystemName());
            pstmt.setLong(2, paymentSystem.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                log.info("Updated PaymentSystem: {}", paymentSystem);
            } else {
                log.warn("No PaymentSystem found to update with id {}", paymentSystem.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating PaymentSystem", e);
            throw new RuntimeException("Error updating PaymentSystem", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                log.info("Deleted PaymentSystem with id {}", id);
            } else {
                log.warn("No PaymentSystem found to delete with id {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting PaymentSystem", e);
            throw new RuntimeException("Error deleting PaymentSystem", e);
        }
    }

    private PaymentSystem mapResultSetToPaymentSystem(ResultSet rs) throws SQLException {
        return PaymentSystem.builder()
                .id(rs.getLong("id"))
                .paymentSystemName(rs.getString("payment_system_name"))
                .build();
    }
}
