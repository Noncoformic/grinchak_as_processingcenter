package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.SalesPoint;

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
public class SalesPointJDBCDaoImpl implements Dao<SalesPoint> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS sales_point (
                id SERIAL PRIMARY KEY,
                pos_name VARCHAR(255) NOT NULL,
                pos_address VARCHAR(255) NOT NULL,
                pos_inn VARCHAR(12) NOT NULL UNIQUE,
                acquiring_bank_id BIGINT NOT NULL REFERENCES acquiring_bank(id)
            );
            """;

    private static final String INSERT = """
            INSERT INTO sales_point (pos_name, pos_address, pos_inn, acquiring_bank_id) 
            VALUES (?, ?, ?, ?) 
            ON CONFLICT (pos_inn) DO NOTHING RETURNING id;
            """;

    private static final String GET_ALL = "SELECT * FROM sales_point;";
    private static final String GET_BY_ID = "SELECT * FROM sales_point WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE sales_point SET pos_name = ?, pos_address = ?, pos_inn = ?, acquiring_bank_id = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM sales_point WHERE id = ?;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE sales_point RESTART IDENTITY CASCADE;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS sales_point CASCADE;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
            log.info("Created table 'SalesPoint' successfully.");
        } catch (SQLException e) {
            log.error("Error creating SalesPoint table", e);
            throw new RuntimeException("Error creating SalesPoint table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            log.info("Dropped SalesPoint table successfully.");
        } catch (SQLException e) {
            log.error("Error dropping SalesPoint table", e);
            throw new RuntimeException("Error dropping SalesPoint table", e);
        }
    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            log.info("SalesPoint table cleared.");
        } catch (SQLException e) {
            log.error("Error clearing SalesPoint table", e);
            throw new RuntimeException("Error clearing SalesPoint table", e);
        }
    }

    @Override
    public void insert(SalesPoint salesPoint) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, salesPoint.getPosName());
            pstmt.setString(2, salesPoint.getPosAddress());
            pstmt.setString(3, salesPoint.getPosInn());
            pstmt.setLong(4, salesPoint.getAcquiringBankId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        salesPoint.setId(rs.getLong(1));
                        log.info("Inserted new SalesPoint: {}", salesPoint);
                    }
                }
            }
        } catch (SQLException e) {
            log.error("Error inserting into SalesPoint table", e);
            throw new RuntimeException("Error inserting into SalesPoint table", e);
        }
    }

    @Override
    public List<SalesPoint> getAll() {
        List<SalesPoint> salesPoints = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                salesPoints.add(mapResultSetToSalesPoint(rs));
            }
        } catch (SQLException e) {
            log.error("Error fetching all SalesPoints", e);
            throw new RuntimeException("Error fetching all SalesPoints", e);
        }
        return salesPoints;
    }

    @Override
    public Optional<SalesPoint> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToSalesPoint(rs));
                }
            }
            log.warn("SalesPoint with id {} not found.", id);
        } catch (SQLException e) {
            log.error("Error getting SalesPoint by ID", e);
            throw new RuntimeException("Error getting SalesPoint by ID", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(SalesPoint salesPoint) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, salesPoint.getPosName());
            pstmt.setString(2, salesPoint.getPosAddress());
            pstmt.setString(3, salesPoint.getPosInn());
            pstmt.setLong(4, salesPoint.getAcquiringBankId());
            pstmt.setLong(5, salesPoint.getId());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                log.info("Updated SalesPoint: {}", salesPoint);
            } else {
                log.warn("No SalesPoint found to update with id {}", salesPoint.getId());
            }
        } catch (SQLException e) {
            log.error("Error updating SalesPoint", e);
            throw new RuntimeException("Error updating SalesPoint", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                log.info("Deleted SalesPoint with id {}", id);
            } else {
                log.warn("No SalesPoint found to delete with id {}", id);
            }
        } catch (SQLException e) {
            log.error("Error deleting SalesPoint", e);
            throw new RuntimeException("Error deleting SalesPoint", e);
        }
    }

    private SalesPoint mapResultSetToSalesPoint(ResultSet rs) throws SQLException {
        return SalesPoint.builder()
                .id(rs.getLong("id"))
                .posName(rs.getString("pos_name"))
                .posAddress(rs.getString("pos_address"))
                .posInn(rs.getString("pos_inn"))
                .acquiringBankId(rs.getLong("acquiring_bank_id"))
                .build();
    }
}
