package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class CardService {
  private static final Logger logger = LogManager.getLogger(CardService.class);
  private final Dao<Card> cardDao;

  public CardService(Dao<Card> cardDao) {
    this.cardDao = cardDao;
  }

  public void createTable() {
    try {
      cardDao.createTable();
      logger.info("✅ Table card created");
    } catch (RuntimeException e) {
      logger.error("Error in createTable: " + e.getMessage());
      throw new RuntimeException("Error in createTable: " + e.getMessage(), e);
    }
  }

  public void clearTable() {
    try {
      cardDao.clearTable();
      logger.info("✅ Table card cleared");
    } catch (RuntimeException e) {
      logger.error("Error in clearTable: " + e.getMessage());
      throw new RuntimeException("Error in clearTable: " + e.getMessage(), e);
    }
  }

  public void dropTable() {
    try {
      cardDao.dropTable();
      logger.info("✅ Table card drop");
    } catch (RuntimeException e) {
      logger.error("Error in dropTable: " + e.getMessage());
      throw new RuntimeException("Error in dropTable: " + e.getMessage(), e);
    }
  }

  public void addCard(LocalDate expirationDate, String holderName, Long cardStatusId, Long paymentSystemId, Long accountId) {
    String cardNumber = generateCardNumber();
    try {
      if (cardDao.getAll().stream().anyMatch(card1 -> card1.getCardNumber().equals(cardNumber))) {
        logger.warn("⚠️ Card '{}' already exists. Skipping insert.", cardNumber);
        return;
      }
      Card card = Card.builder()
              .cardNumber(cardNumber)
              .expirationDate(expirationDate)
              .holderName(holderName)
              .cardStatusId(cardStatusId)
              .paymentSystemId(paymentSystemId)
              .accountId(accountId)
              .receivedFromIssuingBank(LocalDateTime.now())
              .sentToIssuingBank(LocalDateTime.now())
              .build();
      cardDao.insert(card);
      logger.info("✅ Card added: " + cardNumber);

    } catch (RuntimeException e) {
      logger.error("Error in addCard: " + e.getMessage());
      throw new RuntimeException("Error in addCard: " + e.getMessage(), e);
    }
  }

  public List<Card> getAllCards() {
    try {
      return cardDao.getAll();
    } catch (RuntimeException e) {
      logger.error("Error in getAllCards: " + e.getMessage());
      throw new RuntimeException("Error in getAllCards: " + e.getMessage(), e);
    }
  }

  public Optional<Card> getCardById(Long id) {
    try {
      return cardDao.getById(id);
    } catch (RuntimeException e) {
      logger.error("Error in getCardById: " + e.getMessage());
      throw new RuntimeException("Error in getCardById: " + e.getMessage(), e);
    }
  }

  public void deleteCard(Long id) {
    try {
      cardDao.delete(id);
      logger.info("❌ Card deleted: " + id);
    } catch (RuntimeException e) {
      logger.error("Error in deleteCard: " + e.getMessage());
      throw new RuntimeException("Error in deleteCard: " + e.getMessage(), e);
    }
  }

  public void updateCard(Long id, String cardNumber, LocalDate expirationDate, String holderName, Long cardStatusId, Long paymentSystemId, Long accountId) {
    try {
      Optional<Card> optionalCard = cardDao.getById(id);
      if (optionalCard.isPresent()) {
        Card card = optionalCard.get();
        Card updatedCard = card.toBuilder()
                .cardNumber(cardNumber)
                .expirationDate(expirationDate)
                .holderName(holderName)
                .cardStatusId(cardStatusId)
                .paymentSystemId(paymentSystemId)
                .accountId(accountId)
                .build();
        cardDao.update(updatedCard);
        logger.info("🔄 Card updated: {} to {}", card.getCardNumber(), cardNumber);
      }
    } catch (RuntimeException e) {
      logger.error("Error in updateCard: " + e.getMessage());
      throw new RuntimeException("Error in updateCard: " + e.getMessage(), e);
    }
  }
  public void updateCard(Card card){
    cardDao.update(card);
  }
  public String generateCardNumber() {
    // Генерация 15 случайных цифр
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 15; i++) {
      sb.append((int) (Math.random() * 10));
    }
    String partialNumber = sb.toString();
    // Вычисление контрольной цифры
    int checksum = calculateLuhnChecksum(partialNumber);
    // Добавление контрольной цифры к номеру карты
    return partialNumber + checksum;
  }

  public static int calculateLuhnChecksum(String number) {
    int sum = IntStream.range(0, number.length())
            .map(i -> {
              int digit = Character.getNumericValue(number.charAt(number.length() - 1 - i));
              return (i % 2 == 0) ? (digit * 2 > 9 ? digit * 2 - 9 : digit * 2) : digit;
            })
            .sum();

    return (10 - (sum % 10)) % 10;
  }

}