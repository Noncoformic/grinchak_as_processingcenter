package ru.edme.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.edme.dao.Dao;
import ru.edme.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Service
public class CardService {
  private final Dao<Card> cardDao;
  private final CardBuilderService cardBuilderService;

  public CardService(@Qualifier("cardHibernateDaoImpl") Dao<Card> cardDao, CardBuilderService cardBuilderService) {
    this.cardDao = cardDao;
    this.cardBuilderService = cardBuilderService;
  }

  public void createTable() {
    cardDao.createTable();
    log.info("✅ Table 'card' created");
  }

  public void clearTable() {
    cardDao.clearTable();
    log.info("✅ Table 'card' cleared");
  }

  public void dropTable() {
    cardDao.dropTable();
    log.info("✅ Table 'card' dropped");
  }

  public void addCard(LocalDate expirationDate, String holderName, CardStatus cardStatus,
                      PaymentSystem paymentSystem, Account account, LocalDate receivedFromIssuingBank,
                      LocalDate sentToIssuingBank) {
    String cardNumber = generateCardNumber();

    if (cardDao.getAll().stream().anyMatch(card -> card.getCardNumber().equals(cardNumber))) {
      log.warn("⚠️ Card '{}' already exists. Skipping insert.", cardNumber);
      return;
    }

    // Преобразуем LocalDate в LocalDateTime, устанавливая время на начало дня
    LocalDateTime receivedDateTime = receivedFromIssuingBank != null ?
            receivedFromIssuingBank.atStartOfDay() : null;
    LocalDateTime sentDateTime = sentToIssuingBank != null ?
            sentToIssuingBank.atStartOfDay() : null;

    Card card = cardBuilderService.buildCard(cardNumber, expirationDate, holderName,
            cardStatus, paymentSystem, account, receivedDateTime, sentDateTime);

    cardDao.insert(card);
    log.info("✅ Card added: {}", cardNumber);
  }

  public List<Card> getAllCards() {
    return cardDao.getAll();
  }

  public Optional<Card> getCardById(Long id) {
    return cardDao.getById(id);
  }

  public void deleteCard(Long id) {
    cardDao.delete(id);
    log.info("❌ Card deleted: {}", id);
  }

  public void updateCard(Long id, String cardNumber, LocalDate expirationDate, String holderName,
                         CardStatus cardStatus, PaymentSystem paymentSystem, Account account,
                         LocalDateTime receivedFromIssuingBank, LocalDateTime sentToIssuingBank) {
    Optional<Card> optionalCard = cardDao.getById(id);
    if (optionalCard.isPresent()) {
      Card updatedCard = cardBuilderService.buildCard(
              cardNumber,
              expirationDate,
              holderName,
              cardStatus,
              paymentSystem,
              account,
              receivedFromIssuingBank,
              sentToIssuingBank
      );
      updatedCard.setId(id); // Важно установить ID для обновления
      cardDao.update(updatedCard);
      log.info("🔄 Карта обновлена: {} на {}", optionalCard.get().getCardNumber(), cardNumber);
    } else {
      log.warn("Карта с id {} не найдена. Обновление пропущено.", id);
    }
  }

  public void updateCard(Card card) {
    cardDao.update(card);
  }

  public String generateCardNumber() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < 15; i++) {
      sb.append((int) (Math.random() * 10));
    }
    String partialNumber = sb.toString();
    return partialNumber + calculateLuhnChecksum(partialNumber);
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
