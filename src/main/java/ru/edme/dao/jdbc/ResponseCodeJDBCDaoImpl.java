package ru.edme.dao.jdbc;

import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.ResponseCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ResponseCodeJDBCDaoImpl implements Dao<ResponseCode> {
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS response_code (
                id SERIAL PRIMARY KEY,
                error_code VARCHAR(2) NOT NULL,
                error_description VARCHAR(255) NOT NULL,
                error_level VARCHAR(255) NOT NULL
            );
            """;

    private static final String INSERT = """
            INSERT INTO response_code (error_code, error_description, error_level) VALUES (?, ?, ?);
            """;

    private static final String GET_BY_ID = "SELECT * FROM response_code WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE response_code SET error_code = ?, error_description = ?, error_level = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM response_code WHERE id = ?;";



    private static final String GET_ALL = "SELECT * FROM response_code;";

    @Override
    public void createTable() {
        try(Connection connection = JDBCConfig.getConnection();
            Statement stmt = connection.createStatement()){
            stmt.executeUpdate(CREATE_TABLE);

        }catch (SQLException e){
            throw new RuntimeException("Error create ResponseCode table", e);
        }

    }

    @Override
    public void dropTable() {

    }

    @Override
    public void clearTable() {

    }

    @Override
    public void insert(ResponseCode responseCode) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, responseCode.getErrorCode());
            pstmt.setString(2, responseCode.getErrorDescription());
            pstmt.setString(3, responseCode.getErrorLevel());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при вставке в таблицу ResponseCode", e);
        }

    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting ResponseCode", e);
        }

    }

    @Override
    public List<ResponseCode> getAll() {
        List<ResponseCode> responseCodes = new ArrayList<>();
        try (Connection conn = JDBCConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                responseCodes.add(new ResponseCode(
                        rs.getLong("id"),
                        rs.getString("error_code"),
                        rs.getString("error_description"),
                        rs.getString("error_level")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении всех кодов ответов", e);
        }
        return responseCodes;
    }

    @Override
    public Optional<ResponseCode> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Optional.of(new ResponseCode(
                        rs.getLong("id"),
                        rs.getString("error_code"),
                        rs.getString("error_description"),
                        rs.getString("error_level")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching ResponseCode by ID", e);
        }
        return null;
    }

    @Override
    public void update(ResponseCode responseCode) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, responseCode.getErrorCode());
            pstmt.setString(2, responseCode.getErrorDescription());
            pstmt.setString(3, responseCode.getErrorLevel());
            pstmt.setLong(4, responseCode.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating ResponseCode", e);
        }

    }
}
