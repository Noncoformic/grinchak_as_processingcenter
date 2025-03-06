package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardJDBCDaoImpl implements Dao<Card> {

  private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS card (
                id SERIAL PRIMARY KEY,
                card_number VARCHAR(50) UNIQUE NOT NULL,
                expiration_date DATE NOT NULL,
                holder_name VARCHAR(50) NOT NULL,
                card_status_id BIGINT NOT NULL REFERENCES card_status(id),
                payment_system_id BIGINT NOT NULL REFERENCES payment_system(id),
                account_id BIGINT NOT NULL REFERENCES account(id),
                received_from_issuing_bank TIMESTAMP NOT NULL,
                sent_to_issuing_bank TIMESTAMP NOT NULL
            );
            """;

  private static final String INSERT = """
            INSERT INTO card (card_number, expiration_date, holder_name, card_status_id, payment_system_id, account_id,received_from_issuing_bank,sent_to_issuing_bank)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?);
            """;

  private static final String GET_ALL = "SELECT * FROM card;";
  private static final String GET_BY_ID = "SELECT * FROM card WHERE id = ?;";
  private static final String UPDATE = """
            UPDATE card SET card_number = ?, expiration_date = ?, holder_name = ?, card_status_id = ?, payment_system_id = ?, account_id = ?, received_from_issuing_bank = ?, sent_to_issuing_bank = ? WHERE id = ?;
            """;
  private static final String DELETE = "DELETE FROM card WHERE id = ?;";
  private static final String CLEAR_TABLE = "TRUNCATE TABLE card RESTART IDENTITY CASCADE;";
  private static final String DROP_TABLE = "DROP TABLE IF EXISTS card CASCADE;";
  private static final String CHECK_EXISTENCE = "SELECT * FROM card WHERE card_number = ?;";
  private static final Logger logger = LogManager.getLogger(CardJDBCDaoImpl.class);

  @Override
  public void createTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      logger.info("Creating table 'Card'...");
      stmt.executeUpdate(CREATE_TABLE);
      logger.info("Created table 'Card'!");
    } catch (SQLException e) {
      logger.error("Error creating Card table", e);
      throw new RuntimeException("Error creating Card table", e);
    }
  }

  @Override
  public void dropTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      logger.info("Dropping Card table....");
      stmt.executeUpdate(DROP_TABLE);
      logger.info("Dropped Card table successfully");
    } catch (SQLException e) {
      logger.error("Error dropping Card table", e);
      throw new RuntimeException("Error dropping Card table", e);
    }
  }

  @Override
  public void clearTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CLEAR_TABLE);

    } catch (SQLException e) {
      logger.error("Error clearing Card table", e);
      throw new RuntimeException("Error clearing Card table", e);

    }
  }

  @Override
  public void insert(Card card) {
    try (Connection connection = JDBCConfig.getConnection()) {
      // Проверка на существование
      try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
        logger.info("Checking if Card already exists...");
        checkStmt.setString(1, card.getCardNumber());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
          logger.error(" ⚠\uFE0F Card with number " + card.getCardNumber() + " already exists.");
          return;
        }
      }

      // Вставка
      try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
        pstmt.setString(1, card.getCardNumber());
        pstmt.setDate(2, Date.valueOf(card.getExpirationDate()));
        pstmt.setString(3, card.getHolderName());
        pstmt.setLong(4, card.getCardStatusId());
        pstmt.setLong(5, card.getPaymentSystemId());
        pstmt.setLong(6, card.getAccountId());
        pstmt.setTimestamp(7, java.sql.Timestamp.valueOf(card.getReceivedFromIssuingBank()));
        pstmt.setTimestamp(8, java.sql.Timestamp.valueOf(card.getSentToIssuingBank()));
        pstmt.executeUpdate();
      }

    } catch (SQLException e) {
      logger.error("Error inserting into Card table", e);
      throw new RuntimeException("Error inserting into Card table", e);
    }
  }

  @Override
  public void delete(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
      pstmt.setLong(1, id);
      pstmt.executeUpdate();
      logger.info("Card deleted");
    } catch (SQLException e) {
      logger.error("Error deleting Card", e);
      throw new RuntimeException("Error deleting Card", e);
    }
  }

  @Override
  public List<Card> getAll() {
    List<Card> cards = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(GET_ALL)) {
      while (rs.next()) {
        cards.add(Card.builder()
                .id(rs.getLong("id"))
                .cardNumber(rs.getString("card_number"))
                .expirationDate(rs.getDate("expiration_date").toLocalDate())
                .holderName(rs.getString("holder_name"))
                .cardStatusId(rs.getLong("card_status_id"))
                .paymentSystemId(rs.getLong("payment_system_id"))
                .accountId(rs.getLong("account_id"))
                .receivedFromIssuingBank(rs.getTimestamp("received_from_issuing_bank").toLocalDateTime())
                .sentToIssuingBank(rs.getTimestamp("sent_to_issuing_bank").toLocalDateTime())
                .build());
      }
    } catch (SQLException e) {
      logger.error("Error fetching all Cards", e);
      throw new RuntimeException("Error fetching all Cards", e);
    }
    return cards;
  }

  @Override
  public Optional<Card> getById(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
      pstmt.setLong(1, id);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return Optional.of(Card.builder()
                .id(rs.getLong("id"))
                .cardNumber(rs.getString("card_number"))
                .expirationDate(rs.getDate("expiration_date").toLocalDate())
                .holderName(rs.getString("holder_name"))
                .cardStatusId(rs.getLong("card_status_id"))
                .paymentSystemId(rs.getLong("payment_system_id"))
                .accountId(rs.getLong("account_id"))
                .receivedFromIssuingBank(rs.getTimestamp("received_from_issuing_bank").toLocalDateTime())
                .sentToIssuingBank(rs.getTimestamp("sent_to_issuing_bank").toLocalDateTime())
                .build());
      } else {
        logger.error("Card with id {} not found.", id);
      }
    } catch (SQLException e) {
      logger.error("Error getting Card by ID", e);
      throw new RuntimeException("Error getting Card by ID", e);
    }
    return Optional.empty();
  }

  @Override
  public void update(Card card) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
      pstmt.setString(1, card.getCardNumber());
      pstmt.setDate(2, Date.valueOf(card.getExpirationDate()));
      pstmt.setString(3, card.getHolderName());
      pstmt.setLong(4, card.getCardStatusId());
      pstmt.setLong(5, card.getPaymentSystemId());
      pstmt.setLong(6, card.getAccountId());
      pstmt.setTimestamp(7, java.sql.Timestamp.valueOf(card.getReceivedFromIssuingBank()));
      pstmt.setTimestamp(8, java.sql.Timestamp.valueOf(card.getSentToIssuingBank()));
      pstmt.setLong(9, card.getId());
      pstmt.executeUpdate();
      logger.info("Card updated: {}", card);
    } catch (SQLException e) {
      logger.error("Error updating Card", e);
      throw new RuntimeException("Error updating Card", e);
    }
  }
}