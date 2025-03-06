package ru.edme.dao.jdbc;

import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.AcquiringBank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AcquiringBankJDBCDaoImpl implements Dao<AcquiringBank> {

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS acquiring_bank (
                id SERIAL PRIMARY KEY,
                bic VARCHAR(9) NOT NULL UNIQUE,
                abbreviated_name VARCHAR(255) NOT NULL
            );
            """;

    private static final String INSERT = """
            INSERT INTO acquiring_bank (bic, abbreviated_name) VALUES (?, ?);
            """;

    private static final String GET_ALL = "SELECT * FROM acquiring_bank;";
    private static final String GET_BY_ID = "SELECT * FROM acquiring_bank WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE acquiring_bank SET bic = ?, abbreviated_name = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM acquiring_bank WHERE id = ?;";

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating AcquiringBank table", e);
        }
    }

    @Override
    public void dropTable() {

    }

    @Override
    public void clearTable() {

    }

    @Override
    public void insert(AcquiringBank bank) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, bank.getBic());
            pstmt.setString(2, bank.getAbbreviatedName());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into AcquiringBank table", e);
        }
    }

    @Override
    public List<AcquiringBank> getAll() {
        List<AcquiringBank> banks = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                banks.add(new AcquiringBank(
                        rs.getLong("id"),
                        rs.getString("bic"),
                        rs.getString("abbreviated_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all AcquiringBanks", e);
        }
        return banks;
    }

    @Override
    public Optional<AcquiringBank> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new AcquiringBank(
                        rs.getLong("id"),
                        rs.getString("bic"),
                        rs.getString("abbreviated_name")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching AcquiringBank by ID", e);
        }
        return null;
    }

    @Override
    public void update(AcquiringBank bank) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, bank.getBic());
            pstmt.setString(2, bank.getAbbreviatedName());
            pstmt.setLong(3, bank.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating AcquiringBank", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting AcquiringBank", e);
        }
    }
}

