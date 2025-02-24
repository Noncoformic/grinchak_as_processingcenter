package ru.edme.dao.jdbc;

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

public class MerchantCategoryCodeJDBCDaoImpl implements Dao<MerchantCategoryCode> {
  private static final String CREATE_TABLE =
      """
            CREATE TABLE IF NOT EXISTS merchant_category_code (
                id SERIAL PRIMARY KEY,
                mcc VARCHAR(4) NOT NULL UNIQUE,
                mcc_name VARCHAR(255) NOT NULL
            );
            """;

  private static final String INSERT =
      """
            INSERT INTO merchant_category_code (mcc, mcc_name) VALUES (?, ?);
            """;

  private static final String GET_ALL = "SELECT * FROM merchant_category_code;";

  private static final String GET_BY_ID = "SELECT * FROM merchant_category_code WHERE id = ?;";
  private static final String UPDATE =
      """
            UPDATE merchant_category_code SET mcc = ?, mcc_name = ? WHERE id = ?;
            """;
  private static final String DELETE = "DELETE FROM merchant_category_code WHERE id = ?;";
  private static final String CHECK_EXISTENCE = """
        SELECT COUNT(*) FROM merchant_category_code WHERE mcc = ?;
        """;

  @Override
  public void createTable() {
    try (Connection connection = JDBCConfig.getConnection();
        Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CREATE_TABLE);
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при создании таблицы MerchantCategoryCode", e);
    }
  }

  @Override
  public void dropTable() {}

  @Override
  public void clearTable() {}

  @Override
  public void insert(MerchantCategoryCode mcc) {
    try (Connection connection = JDBCConfig.getConnection()) {
      // Проверяем, существует ли уже этот MCC-код
      try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
        checkStmt.setString(1, mcc.getMcc());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
          System.out.println("⚠️ MCC '" + mcc.getMcc() + "' already exists. Skipping insert.");
          return; // Просто пропускаем вставку
        }
      }

      // Если MCC-код не найден, вставляем его
      try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
        pstmt.setString(1, mcc.getMcc());
        pstmt.setString(2, mcc.getMccName());
        pstmt.executeUpdate();
        System.out.println("✅ MCC added: " + mcc.getMcc());
      }

    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при вставке в таблицу MerchantCategoryCode", e);
    }
  }


  @Override
  public void delete(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
      pstmt.setLong(1, id);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error deleting MerchantCategoryCode", e);
    }
  }

  @Override
  public List<MerchantCategoryCode> getAll() {
    List<MerchantCategoryCode> mccList = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(GET_ALL)) {
      while (rs.next()) {
        mccList.add(
            new MerchantCategoryCode(
                rs.getLong("id"), rs.getString("mcc"), rs.getString("mcc_name")));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Ошибка при получении всех MCC-кодов", e);
    }
    return mccList;
  }

  @Override
  public MerchantCategoryCode getById(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
      pstmt.setLong(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return new MerchantCategoryCode(
                rs.getLong("id"),
                rs.getString("mcc"),
                rs.getString("mcc_name")
        );
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching MerchantCategoryCode by ID", e);
    }

    return null;
  }

  @Override
  public void update(MerchantCategoryCode mcc) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
      pstmt.setString(1, mcc.getMcc());
      pstmt.setString(2, mcc.getMccName());
      pstmt.setLong(3, mcc.getId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException("Error updating MerchantCategoryCode", e);
    }
  }
}
