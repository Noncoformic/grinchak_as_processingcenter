package ru.edme.dao.jdbc;

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

public class SalesPointJDBCDaoImpl implements Dao<SalesPoint> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS sales_point (
                id SERIAL PRIMARY KEY,
                pos_name VARCHAR(255) NOT NULL,
                pos_address VARCHAR(255) NOT NULL,
                pos_inn VARCHAR(12) NOT NULL,
                acquiring_bank_id BIGINT NOT NULL REFERENCES acquiring_bank(id)
            );
            """;

    private static final String INSERT = """
            INSERT INTO sales_point (pos_name, pos_address, pos_inn, acquiring_bank_id)
            VALUES (?, ?, ?, ?);
            """;
    private static final String CLEAR_TABLE = "TRUNCATE TABLE sales_point RESTART IDENTITY CASCADE;";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS sales_point CASCADE;";

    private static final String GET_ALL = "SELECT * FROM sales_point;";
    private static final String GET_BY_ID = "SELECT * FROM sales_point WHERE id = ?;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating SalesPoint table", e);
        }
    }

    @Override
    public void dropTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error dropping SalesPoint table", e);
        }

    }

    @Override
    public void clearTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
        } catch (SQLException e) {
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
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into SalesPoint table", e);
        }
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<SalesPoint> getAll() {
        List<SalesPoint> salesPoints = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                salesPoints.add(new SalesPoint(
                        rs.getLong("id"),
                        rs.getString("pos_name"),
                        rs.getString("pos_address"),
                        rs.getString("pos_inn"),
                        rs.getLong("acquiring_bank_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all sales points", e);
        }
        return salesPoints;
    }

    @Override
    public SalesPoint getById(Long id) {
        return null;
    }

    @Override
    public void update(SalesPoint entity) {

    }
}
