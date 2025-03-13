package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.ResponseCode;
import ru.edme.model.Terminal;
import ru.edme.model.Transaction;
import ru.edme.model.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class TransactionJDBCDaoImpl implements Dao<Transaction> {

  private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS transaction (
                id SERIAL PRIMARY KEY,
                transaction_date TIMESTAMP NOT NULL,
                sum DECIMAL(15,2) NOT NULL,
                transaction_name VARCHAR(255) NOT NULL,
                account_id BIGINT NOT NULL REFERENCES account(id),
                transaction_type_id BIGINT NOT NULL REFERENCES transaction_type(id),
                card_id BIGINT NOT NULL REFERENCES card(id),
                terminal_id BIGINT NOT NULL REFERENCES terminal(id),
                response_code_id BIGINT NOT NULL REFERENCES response_code(id),
                authorization_code VARCHAR(6) NOT NULL,
                received_from_issuing_bank TIMESTAMP,
                sent_to_issuing_bank TIMESTAMP
            );
            """;

  private static final String INSERT = """
            INSERT INTO transaction (transaction_date, sum, transaction_name, account_id, transaction_type_id, card_id, terminal_id, response_code_id, authorization_code, received_from_issuing_bank, sent_to_issuing_bank)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) 
            ON CONFLICT (authorization_code) DO NOTHING RETURNING id;
            """;

  private static final String GET_ALL = "SELECT * FROM transaction;";
  private static final String GET_BY_ID = "SELECT * FROM transaction WHERE id = ?;";
  private static final String UPDATE = """
            UPDATE transaction SET transaction_date = ?, sum = ?, transaction_name = ?, account_id = ?, transaction_type_id = ?, card_id = ?, terminal_id = ?, response_code_id = ?, authorization_code = ?, received_from_issuing_bank = ?, sent_to_issuing_bank = ? WHERE id = ?;
            """;
  private static final String DELETE = "DELETE FROM transaction WHERE id = ?;";
  private static final String CLEAR_TABLE = "TRUNCATE TABLE transaction RESTART IDENTITY CASCADE;";
  private static final String DROP_TABLE = "DROP TABLE IF EXISTS transaction CASCADE;";

  @Override
  public void createTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CREATE_TABLE);
      log.info("Created table 'Transaction' successfully.");
    } catch (SQLException e) {
      log.error("Error creating Transaction table", e);
      throw new RuntimeException("Error creating Transaction table", e);
    }
  }

  @Override
  public void dropTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(DROP_TABLE);
      log.info("Dropped Transaction table successfully.");
    } catch (SQLException e) {
      log.error("Error dropping Transaction table", e);
      throw new RuntimeException("Error dropping Transaction table", e);
    }
  }

  @Override
  public void clearTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CLEAR_TABLE);
      log.info("Transaction table cleared.");
    } catch (SQLException e) {
      log.error("Error clearing Transaction table", e);
      throw new RuntimeException("Error clearing Transaction table", e);
    }
  }

  @Override
  public void insert(Transaction transaction) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setTimestamp(1, Timestamp.valueOf(transaction.getTransactionDate()));
      pstmt.setBigDecimal(2, transaction.getSum());
      pstmt.setString(3, transaction.getTransactionName());
      pstmt.setLong(4, transaction.getAccount().getId());
      pstmt.setLong(5, transaction.getTransactionType().getId());
      pstmt.setLong(6, transaction.getCard().getId());
      pstmt.setLong(7, transaction.getTerminal().getId());
      pstmt.setLong(8, transaction.getResponseCode().getId());
      pstmt.setString(9, transaction.getAuthorizationCode());
      pstmt.setTimestamp(10, transaction.getReceivedFromIssuingBank() != null ? Timestamp.valueOf(transaction.getReceivedFromIssuingBank()) : null);
      pstmt.setTimestamp(11, transaction.getSentToIssuingBank() != null ? Timestamp.valueOf(transaction.getSentToIssuingBank()) : null);

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows > 0) {
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
          if (rs.next()) {
            transaction.setId(rs.getLong(1));
            log.info("Inserted new Transaction: {}", transaction);
          }
        }
      }
    } catch (SQLException e) {
      log.error("Error inserting into Transaction table", e);
      throw new RuntimeException("Error inserting into Transaction table", e);
    }
  }

  @Override
  public List<Transaction> getAll() {
    List<Transaction> transactions = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(GET_ALL)) {

      while (rs.next()) {
        transactions.add(mapResultSetToTransaction(rs));
      }
    } catch (SQLException e) {
      log.error("Error fetching all transactions", e);
      throw new RuntimeException("Error fetching all transactions", e);
    }
    return transactions;
  }

  @Override
  public Optional<Transaction> getById(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
      pstmt.setLong(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToTransaction(rs));
        }
      }
      log.warn("Transaction with id {} not found.", id);
    } catch (SQLException e) {
      log.error("Error getting Transaction by ID", e);
      throw new RuntimeException("Error getting Transaction by ID", e);
    }
    return Optional.empty();
  }

  private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
    return Transaction.builder()
            .id(rs.getLong("id"))
            .transactionDate(rs.getTimestamp("transaction_date").toLocalDateTime())
            .sum(rs.getBigDecimal("sum"))
            .transactionName(rs.getString("transaction_name"))
            .account(Account.builder().id(rs.getLong("account_id")).build())
            .transactionType(TransactionType.builder().id(rs.getLong("transaction_type_id")).build())
            .card(Card.builder().id(rs.getLong("card_id")).build())
            .terminal(Terminal.builder().id(rs.getLong("terminal_id")).build())
            .responseCode(ResponseCode.builder().id(rs.getLong("response_code_id")).build())
            .authorizationCode(rs.getString("authorization_code"))
            .receivedFromIssuingBank(rs.getTimestamp("received_from_issuing_bank") != null ?
                    rs.getTimestamp("received_from_issuing_bank").toLocalDateTime() : null)
            .sentToIssuingBank(rs.getTimestamp("sent_to_issuing_bank") != null ?
                    rs.getTimestamp("sent_to_issuing_bank").toLocalDateTime() : null)
            .build();
  }

  @Override
  public void update(Transaction transaction) {
    // Реализовать, если потребуется
  }

  @Override
  public void delete(Long id) {
    // Реализовать, если потребуется
  }
}
