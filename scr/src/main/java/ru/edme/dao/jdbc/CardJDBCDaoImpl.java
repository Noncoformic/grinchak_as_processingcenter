package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CardJDBCDaoImpl implements Dao<Card> {

  private static final String CREATE_TABLE =
      """
            CREATE TABLE IF NOT EXISTS card (
                id SERIAL PRIMARY KEY,
                card_number VARCHAR(50) UNIQUE NOT NULL,
                expiration_date DATE NOT NULL,
                holder_name VARCHAR(50) NOT NULL,
                card_status_id BIGINT NOT NULL REFERENCES card_status(id),
                payment_system_id BIGINT NOT NULL REFERENCES payment_system(id),
                account_id BIGINT NOT NULL REFERENCES account(id)
            );
            """;

  private static final String INSERT =
      """
            INSERT INTO card (card_number, expiration_date, holder_name, card_status_id, payment_system_id, account_id)
            VALUES (?, ?, ?, ?, ?, ?);
            """;

  private static final String GET_ALL = "SELECT * FROM card;";
  //private static final String GET_BY_ID = "SELECT * FROM card WHERE id = ?;";
  private static final String UPDATE =
      """
            UPDATE card SET card_number = ?, expiration_date = ?, holder_name = ?, card_status_id = ?, payment_system_id = ?, account_id = ? WHERE id = ?;
            """;
  //private static final String DELETE = "DELETE FROM card WHERE id = ?;";
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
    }
  }

  @Override
  public void clearTable() {
    try (Connection connection = JDBCConfig.getConnection();
        Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CLEAR_TABLE);

    } catch (SQLException e) {

      throw new RuntimeException("Error clearing Card table", e);

    }
  }

  @Override
  public void insert(Card card) {
    try (Connection connection = JDBCConfig.getConnection()) {
      try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
        logger.info("Checking if Card already exists...");
        checkStmt.setString(1, card.getCardNumber());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
          logger.error("Card with number " + card.getCardNumber() + " already exists.");
          return;
        }
      }

      try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
        pstmt.setString(1, card.getCardNumber());
        pstmt.setDate(2, new java.sql.Date(card.getExpirationDate().getTime()));
        pstmt.setString(3, card.getHolderName());
        pstmt.setLong(4, card.getCardStatusId());
        pstmt.setLong(5, card.getPaymentSystemId());
        pstmt.setLong(6, card.getAccountId());
        pstmt.executeUpdate();
      }

    } catch (SQLException e) {
      throw new RuntimeException("Error inserting into Card table", e);
    }
  }

  @Override
  public void delete(Long id) {}

  @Override
  public List<Card> getAll() {
    List<Card> cards = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(GET_ALL)) {
      while (rs.next()) {
        cards.add(
            new Card(
                rs.getLong("id"),
                rs.getString("card_number"),
                rs.getDate("expiration_date"),
                rs.getString("holder_name"),
                rs.getLong("card_status_id"),
                rs.getLong("payment_system_id"),
                rs.getLong("account_id")));
      }
    } catch (SQLException e) {
      throw new RuntimeException("Error fetching all cards", e);
    }
    return cards;
  }

  @Override
  public Card getById(Long id) {
    return null;
  }

  @Override
  public void update(Card card) {
    try (Connection connection = JDBCConfig.getConnection();
        PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
      pstmt.setString(1, card.getCardNumber());
      pstmt.setDate(2, new java.sql.Date(card.getExpirationDate().getTime()));
      pstmt.setString(3, card.getHolderName());
      pstmt.setLong(4, card.getCardStatusId());
      pstmt.setLong(5, card.getPaymentSystemId());
      pstmt.setLong(6, card.getAccountId());
      pstmt.setLong(7, card.getId());
      pstmt.executeUpdate();

    } catch (SQLException e) {
      throw new RuntimeException("Error updating card", e);
    }
  }
}
