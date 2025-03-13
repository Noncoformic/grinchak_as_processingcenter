package ru.edme.dao.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.CardStatus;
import ru.edme.model.PaymentSystem;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Types;

@Slf4j
@Repository
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
                received_from_issuing_bank TIMESTAMP,
                sent_to_issuing_bank TIMESTAMP
            );
            """;

  private static final String INSERT = """
            INSERT INTO card (card_number, expiration_date, holder_name, card_status_id, payment_system_id, account_id, received_from_issuing_bank, sent_to_issuing_bank)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT (card_number) DO NOTHING RETURNING id;
            """;

  private static final String GET_ALL = "SELECT * FROM card;";
  private static final String GET_BY_ID = "SELECT * FROM card WHERE id = ?;";
  private static final String UPDATE = """
            UPDATE card SET card_number = ?, expiration_date = ?, holder_name = ?, card_status_id = ?, payment_system_id = ?, account_id = ?, received_from_issuing_bank = ?, sent_to_issuing_bank = ? WHERE id = ?;
            """;
  private static final String DELETE = "DELETE FROM card WHERE id = ?;";
  private static final String CLEAR_TABLE = "TRUNCATE TABLE card RESTART IDENTITY CASCADE;";
  private static final String DROP_TABLE = "DROP TABLE IF EXISTS card CASCADE;";

  @Override
  public void createTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CREATE_TABLE);
      log.info("Created table 'Card' successfully.");
    } catch (SQLException e) {
      log.error("Error creating Card table", e);
      throw new RuntimeException("Error creating Card table", e);
    }
  }

  @Override
  public void dropTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(DROP_TABLE);
      log.info("Dropped Card table successfully.");
    } catch (SQLException e) {
      log.error("Error dropping Card table", e);
      throw new RuntimeException("Error dropping Card table", e);
    }
  }

  @Override
  public void clearTable() {
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement()) {
      stmt.executeUpdate(CLEAR_TABLE);
      log.info("Card table cleared.");
    } catch (SQLException e) {
      log.error("Error clearing Card table", e);
      throw new RuntimeException("Error clearing Card table", e);
    }
  }

  @Override
  public void insert(Card card) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

      pstmt.setString(1, card.getCardNumber());
      pstmt.setDate(2, Date.valueOf(card.getExpirationDate()));
      pstmt.setString(3, card.getHolderName());
      pstmt.setLong(4, card.getCardStatus().getId());
      pstmt.setLong(5, card.getPaymentSystem().getId());
      pstmt.setLong(6, card.getAccount().getId());

      if (card.getReceivedFromIssuingBank() != null) {
        pstmt.setTimestamp(7, Timestamp.valueOf(card.getReceivedFromIssuingBank()));
      } else {
        pstmt.setNull(7, Types.TIMESTAMP);
      }

      if (card.getSentToIssuingBank() != null) {
        pstmt.setTimestamp(8, Timestamp.valueOf(card.getSentToIssuingBank()));
      } else {
        pstmt.setNull(8, Types.TIMESTAMP);
      }

      int affectedRows = pstmt.executeUpdate();
      if (affectedRows > 0) {
        try (ResultSet rs = pstmt.getGeneratedKeys()) {
          if (rs.next()) {
            card.setId(rs.getLong(1));
            log.info("Inserted new Card: {}", card);
          }
        }
      }
    } catch (SQLException e) {
      log.error("Error inserting into Card table", e);
      throw new RuntimeException("Error inserting into Card table", e);
    }
  }

  @Override
  public List<Card> getAll() {
    List<Card> cards = new ArrayList<>();
    try (Connection connection = JDBCConfig.getConnection();
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery(GET_ALL)) {

      while (rs.next()) {
        cards.add(mapResultSetToCard(rs));
      }
    } catch (SQLException e) {
      log.error("Error fetching all Cards", e);
      throw new RuntimeException("Error fetching all Cards", e);
    }
    return cards;
  }

  @Override
  public Optional<Card> getById(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
      pstmt.setLong(1, id);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          return Optional.of(mapResultSetToCard(rs));
        }
      }
      log.warn("Card with id {} not found.", id);
    } catch (SQLException e) {
      log.error("Error getting Card by ID", e);
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
      pstmt.setLong(4, card.getCardStatus().getId());
      pstmt.setLong(5, card.getPaymentSystem().getId());
      pstmt.setLong(6, card.getAccount().getId());
      pstmt.setTimestamp(7, Timestamp.valueOf(card.getReceivedFromIssuingBank()));
      pstmt.setTimestamp(8, Timestamp.valueOf(card.getSentToIssuingBank()));
      pstmt.setLong(9, card.getId());

      int rowsUpdated = pstmt.executeUpdate();
      if (rowsUpdated > 0) {
        log.info("Updated Card: {}", card);
      } else {
        log.warn("No Card found to update with id {}", card.getId());
      }
    } catch (SQLException e) {
      log.error("Error updating Card", e);
      throw new RuntimeException("Error updating Card", e);
    }
  }

  @Override
  public void delete(Long id) {
    try (Connection connection = JDBCConfig.getConnection();
         PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
      pstmt.setLong(1, id);
      int rowsDeleted = pstmt.executeUpdate();
      if (rowsDeleted > 0) {
        log.info("Deleted Card with id {}", id);
      } else {
        log.warn("No Card found to delete with id {}", id);
      }
    } catch (SQLException e) {
      log.error("Error deleting Card", e);
      throw new RuntimeException("Error deleting Card", e);
    }
  }

  private Card mapResultSetToCard(ResultSet rs) throws SQLException {
    return Card.builder()
            .id(rs.getLong("id"))
            .cardNumber(rs.getString("card_number"))
            .expirationDate(rs.getDate("expiration_date").toLocalDate())
            .holderName(rs.getString("holder_name"))
            .cardStatus(new CardStatus(rs.getLong("card_status_id"), null))
            .paymentSystem(new PaymentSystem(rs.getLong("payment_system_id"), null))
            .account(new Account(rs.getLong("account_id"), null, null, null, null))
            .build();
  }
}
