package ru.edme;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.dao.Dao;
import ru.edme.dao.DaoFactory;
import ru.edme.dao.DaoType;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.CardStatus;
import ru.edme.model.Currency;
import ru.edme.model.IssuingBank;
import ru.edme.service.AccountService;
import ru.edme.service.CardService;
import ru.edme.service.CardStatusService;
import ru.edme.service.CurrencyService;
import ru.edme.service.IssuingBankService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class Main {
  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) {
    logger.info("🔹 Starting Processing Center application...");
    // Выбираем тип DAO: JDBC или HIBERNATE
    DaoType daoType = DaoType.JDBC; // Меняем здесь для переключения
    DaoFactory daoFactory = new DaoFactory(daoType);

    // Получаем DAO через фабрику
    Dao<Card> cardDao = daoFactory.getDao(Card.class);
    Dao<Account> accountDao = daoFactory.getDao(Account.class);
    Dao<CardStatus> cardStatusDao = daoFactory.getDao(CardStatus.class);
    Dao<Currency> currencyDao = daoFactory.getDao(Currency.class);
    Dao<IssuingBank> issuingBankDao = daoFactory.getDao(IssuingBank.class);
    // Создаём сервисы, передавая им DAO
    CardService cardService = new CardService(cardDao);
    AccountService accountService = new AccountService(accountDao);
    CardStatusService cardStatusService = new CardStatusService(cardStatusDao);
    CurrencyService currencyService = new CurrencyService(currencyDao);
    IssuingBankService issuingBankService = new IssuingBankService(issuingBankDao);

    // Создаём таблицы
    currencyService.createTable();
    issuingBankService.createTable();
    cardStatusService.createTable();
    accountService.createTable();
    cardService.createTable();

//    // Удаляем таблицы
//    cardService.dropTable();
//    accountService.dropTable();
//    cardStatusService.dropTable();
//    currencyService.dropTable();
//    issuingBankService.dropTable();

    logger.info("✅ Tables created.");
    // Получаем статусы
    CardStatus activeStatus = cardStatusService.getCardStatusByName("Active");
    CardStatus blockedStatus = cardStatusService.getCardStatusByName("Blocked");
    CardStatus inactiveStatus = cardStatusService.getCardStatusByName("Inactive");
    logger.info("✅ Card statuses added.");

    // Добавляем валюту
    currencyService.addCurrency(String.valueOf(810), "RUB", "Russian Ruble");
    currencyService.addCurrency(String.valueOf(860), "USD", "United States Dollar");
    logger.info("✅ Currencies added");

    // Добавляем банки
    issuingBankService.addIssuingBank("111111111", "Bank1");
    issuingBankService.addIssuingBank("222222222", "Bank2");
    logger.info("✅ IssuingBanks added");
    // Добавляем аккаунты
    accountService.addAccount("40817810800000000001", new BigDecimal("10000.00"), 1L, 1L);
    accountService.addAccount("40817810800000000002", new BigDecimal("15000.00"), 1L, 2L);
    accountService.addAccount("40817810800000000003", new BigDecimal("20000.00"), 2L, 1L);
    accountService.addAccount("40817810800000000004", new BigDecimal("25000.00"), 2L, 2L);
    logger.info("✅ Accounts added.");

    // Добавляем карты
    cardService.addCard(LocalDate.of(2030, 5, 11), "John Chikin", activeStatus.getId(), 1L, 1L);
    cardService.addCard( LocalDate.of(2032, 1, 12), "Jane Smith", inactiveStatus.getId(), 1L, 2L);
    cardService.addCard( LocalDate.of(2033, 1, 13), "Alice Johnson", blockedStatus.getId(), 2L, 3L);
    cardService.addCard( LocalDate.of(2027, 7, 23), "Bob Brown", activeStatus.getId(), 2L, 4L);
    logger.info("✅ Cards added.");

    // Выводим все карты
    List<Card> cards = cardService.getAllCards();
    logger.info("\n📌 All cards in the database:");
    cards.forEach(System.out::println);

    // Обновляем 2 карты
    if (cards.size() >= 2) {
      Card card1 = cards.get(0);
      cardService.updateCard(card1.getId(), card1.getCardNumber(), LocalDate.of(2050, 2, 11), "John Chikin Updated", card1.getCardStatusId(), card1.getPaymentSystemId(),
              card1.getAccountId());
      Card card2 = cards.get(1);
      cardService.updateCard(card2.getId(), card2.getCardNumber(), LocalDate.of(2043, 5, 21), "Jane Smith Updated", card2.getCardStatusId(), card2.getPaymentSystemId(), card2.getAccountId());
      logger.info("\n✅ Updated 2 cards!");
    }
    // Очищаем таблицу card
    cardService.clearTable();
    logger.info("✅ Table card cleared");
    // Удаляем таблицу card
    cardService.dropTable();
    logger.info("✅ Table card drop");
  }
}