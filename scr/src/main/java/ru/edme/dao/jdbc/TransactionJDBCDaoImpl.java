package ru.edme.dao.jdbc;

import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class TransactionJDBCDaoImpl implements Dao<Transaction> {

  private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS transaction (
                id SERIAL PRIMARY KEY,
                transaction_date DATE NOT NULL,
                sum DECIMAL(15,2) NOT NULL,
                transaction_name VARCHAR(255) NOT NULL,
                account_id BIGINT NOT NULL REFERENCES account(id),
                transaction_type_id BIGINT NOT NULL REFERENCES transaction_type(id),
                card_id BIGINT NOT NULL REFERENCES card(id),
                terminal_id BIGINT NOT NULL,
                response_code_id BIGINT NOT NULL,
                authorization_code VARCHAR(6) NOT NULL,
                received_from_issuing_bank TIMESTAMP,
                sent_to_issuing_bank TIMESTAMP
            );
            """;

  private static final String INSERT = """
        INSERT INTO transaction (transaction_date, sum, transaction_name, account_id, transaction_type_id, card_id, terminal_id, response_code_id, authorization_code, received_from_issuing_bank, sent_to_issuing_bank)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;

  private static final String GET_ALL = "SELECT * FROM transaction;";
//  private static final String GET_BY_ID = "SELECT * FROM transaction WHERE id = ?;";
//  private static final String DELETE = "DELETE FROM transaction WHERE id = ?;";
private static final String CLEAR_TABLE = "TRUNCATE TABLE transaction RESTART IDENTITY CASCADE;";
  private static final String DROP_TABLE = "DROP TABLE IF EXISTS transaction CASCADE;";
  public static final String CHECK_EXISTENCE = "SELECT * FROM transaction WHERE transaction_name = ?;";



  @Override
  public void createTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CREATE_TABLE);
    } catch (SQLException e) {
      throw new RuntimeException("Error creating Transaction table", e);
    }
  }

  @Override
  public void dropTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(DROP_TABLE);
    } catch (SQLException e) {
      throw new RuntimeException("Error dropping Transaction table", e);
    }

  }

  @Override
  public void clearTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CLEAR_TABLE);
    } catch (SQLException e) {
      throw new RuntimeException("Error clearing Transaction table", e);
    }

  }

  @Override
  public void insert(Transaction transaction) {
        try (Connection connection = JDBCConfig.getConnection()) {
          try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
            checkStmt.setString(1, transaction.getAuthorizationCode());
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
              System.out.println("⚠️ Account '" + transaction.getTransactionName() + "' already exists. Skipping insert.");
              return;}
          }

          try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
            pstmt.setDate(1, new java.sql.Date(transaction.getTransactionDate().getTime()));
            pstmt.setBigDecimal(2, transaction.getSum());
            pstmt.setString(3, transaction.getTransactionName());
            pstmt.setLong(4, transaction.getAccountId());
            pstmt.setLong(5, transaction.getTransactionTypeId());
            pstmt.setLong(6, transaction.getCardId());
            pstmt.setLong(7, transaction.getTerminalId());
            pstmt.setLong(8, transaction.getResponseCodeId());
            pstmt.setString(9, transaction.getAuthorizationCode());
            if (transaction.getReceivedFromIssuingBank() != null) {
              pstmt.setTimestamp(10, new Timestamp(transaction.getReceivedFromIssuingBank().getTime()));
            } else {
              pstmt.setNull(10, Types.TIMESTAMP);
            }

            if (transaction.getSentToIssuingBank() != null) {
              pstmt.setTimestamp(11, new Timestamp(transaction.getSentToIssuingBank().getTime()));
            } else {
              pstmt.setNull(11, Types.TIMESTAMP);
            }

            pstmt.executeUpdate();
          }

        } catch (SQLException e) {
          throw new RuntimeException("Error inserting into Transaction table", e);
        }
      }

      @Override
  public void delete(Long id) {

  }

  @Override
  public List<Transaction> getAll() {
    List<Transaction> transactions = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(GET_ALL)) {
      while (rs.next()) {
        transactions.add(new Transaction(
                rs.getLong("id"),
                rs.getDate("transaction_date"),
                rs.getBigDecimal("sum"),
                rs.getString("transaction_name"),
                rs.getLong("account_id"),
                rs.getLong("transaction_type_id"),
                rs.getLong("card_id"),
                rs.getLong("terminal_id"),
                rs.getLong("response_code_id"),
                rs.getString("authorization_code"),
                rs.getTimestamp("received_from_issuing_bank"),
                rs.getTimestamp("sent_to_issuing_bank")
        ));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching all transactions", e);
    }
    return transactions;
  }

  @Override
  public Transaction getById(Long id) {
    return null;
  }

  @Override
  public void update(Transaction entity) {

  }
}
