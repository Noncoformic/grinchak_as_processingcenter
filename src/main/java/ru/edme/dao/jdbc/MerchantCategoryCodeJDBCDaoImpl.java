package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.MerchantCategoryCode;

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
public class MerchantCategoryCodeJDBCDaoImpl implements Dao<MerchantCategoryCode> {

  private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS merchant_category_code (
                id SERIAL PRIMARY KEY,
                mcc VARCHAR(4) NOT NULL UNIQUE,
                mcc_name VARCHAR(255) NOT NULL
            );
            """;

  private static final String INSERT = """
            INSERT INTO merchant_category_code (mcc, mcc_name) 
            VALUES (?, ?) 
            ON CONFLICT (mcc) DO NOTHING RETURNING id;
            """;

  private static final String GET_ALL = "SELECT * FROM merchant_category_code;";
  private static final String GET_BY_ID = "SELECT * FROM merchant_category_code WHERE id = ?;";
  private static final String UPDATE = """
            UPDATE merchant_category_code SET mcc = ?, mcc_name = ? WHERE id = ?;
            """;
  private static final String DELETE = "DELETE FROM merchant_category_code WHERE id = ?;";
  private static final String CLEAR_TABLE = "TRUNCATE TABLE merchant_category_code RESTART IDENTITY CASCADE;";
  private static final String DROP_TABLE = "DROP TABLE IF EXISTS merchant_category_code CASCADE;";

  @Override
  public void createTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CREATE_TABLE);
      log.info("Created table 'MerchantCategoryCode' successfully.");
    } catch (SQLException e) {
      log.error("Error creating MerchantCategoryCode table", e);
      throw new RuntimeException("Error creating MerchantCategoryCode table", e);
    }
  }

  @Override
  public void dropTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(DROP_TABLE);
      log.info("Dropped MerchantCategoryCode table successfully.");
    } catch (SQLException e) {
      log.error("Error dropping MerchantCategoryCode table", e);
      throw new RuntimeException("Error dropping MerchantCategoryCode table", e);
    }
  }

  @Override
  public void clearTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CLEAR_TABLE);
      log.info("MerchantCategoryCode table cleared.");
    } catch (SQLException e) {
      log.error("Error clearing MerchantCategoryCode table", e);
      throw new RuntimeException("Error clearing MerchantCategoryCode table", e);
    }
  }

  @Override
  public void insert(MerchantCategoryCode mcc) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, mcc.getMcc());
      pstmt.setString(2, mcc.getMccName());

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows > 0) {
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
          if (rs.next()) {
            mcc.setId(rs.getLong(1));
            log.info("Inserted new MerchantCategoryCode: {}", mcc);
          }
        }
      }
    } catch (SQLException e) {
      log.error("Error inserting into MerchantCategoryCode table", e);
      throw new RuntimeException("Error inserting into MerchantCategoryCode table", e);
    }
  }

  @Override
  public List<MerchantCategoryCode> getAll() {
    List<MerchantCategoryCode> mccList = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(GET_ALL)) {

      while (rs.next()) {
        mccList.add(mapResultSetToMCC(rs));
      }
    } catch (SQLException e) {
      log.error("Error fetching all MerchantCategoryCodes", e);
      throw new RuntimeException("Error fetching all MerchantCategoryCodes", e);
    }
    return mccList;
  }

  @Override
  public Optional<MerchantCategoryCode> getById(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
      pstmt.setLong(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToMCC(rs));
        }
      }
      log.warn("MerchantCategoryCode with id {} not found.", id);
    } catch (SQLException e) {
      log.error("Error getting MerchantCategoryCode by ID", e);
      throw new RuntimeException("Error getting MerchantCategoryCode by ID", e);
    }
    return Optional.empty();
  }

  @Override
  public void update(MerchantCategoryCode mcc) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
      pstmt.setString(1, mcc.getMcc());
      pstmt.setString(2, mcc.getMccName());
      pstmt.setLong(3, mcc.getId());

      int rowsUpdated = pstmt.executeUpdate();
      if (rowsUpdated > 0) {
        log.info("Updated MerchantCategoryCode: {}", mcc);
      } else {
        log.warn("No MerchantCategoryCode found to update with id {}", mcc.getId());
      }
    } catch (SQLException e) {
      log.error("Error updating MerchantCategoryCode", e);
      throw new RuntimeException("Error updating MerchantCategoryCode", e);
    }
  }

  @Override
  public void delete(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
      pstmt.setLong(1, id);
      int rowsDeleted = pstmt.executeUpdate();
      if (rowsDeleted > 0) {
        log.info("Deleted MerchantCategoryCode with id {}", id);
      } else {
        log.warn("No MerchantCategoryCode found to delete with id {}", id);
      }
    } catch (SQLException e) {
      log.error("Error deleting MerchantCategoryCode", e);
      throw new RuntimeException("Error deleting MerchantCategoryCode", e);
    }
  }

  private MerchantCategoryCode mapResultSetToMCC(ResultSet rs) throws SQLException {
    return MerchantCategoryCode.builder()
            .id(rs.getLong("id"))
            .mcc(rs.getString("mcc"))
            .mccName(rs.getString("mcc_name"))
            .build();
  }
}
