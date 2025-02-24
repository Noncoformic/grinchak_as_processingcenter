package ru.edme;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.service.AccountService;
import ru.edme.service.CardService;
import ru.edme.model.Card;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Main {
  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) {
    // ✅ Создаём сервисы
    CardService cardService = new CardService();
    AccountService accountService = new AccountService();

    // ✅ Создаём таблицы
    cardService.createTable();
    System.out.println("✅ Table 'Card' created!");

    // ✅ Добавляем 4 аккаунта (чтобы избежать ошибок с foreign key)
    accountService.addAccount("40817810800000000001", new BigDecimal("10000.00"), 1L, 1L);
    accountService.addAccount("40817810800000000002", new BigDecimal("15000.00"), 1L, 1L);
    accountService.addAccount("40817810800000000003", new BigDecimal("20000.00"), 1L, 1L);
    accountService.addAccount("40817810800000000004", new BigDecimal("25000.00"), 1L, 1L);
    System.out.println("✅ 4 Accounts added.");

    // ✅ Добавляем 4 карты (они теперь привязаны к существующим Account)
    cardService.addCard("1111222233334444", new Date(), "John Doe", 1L, 1L, 1L);
    cardService.addCard("5555666677778888", new Date(), "Jane Smith", 2L, 1L, 2L);
    cardService.addCard("9999000011112222", new Date(), "Alice Johnson", 1L, 2L, 3L);
    cardService.addCard("3333444455556666", new Date(), "Bob Brown", 2L, 2L, 4L);
    System.out.println("\n✅ Cards added to the table!");

    // ✅ Получаем и выводим все карты
    List<Card> cards = cardService.getAllCards();
    System.out.println("\n📌 All cards in the database:");
    cards.forEach(System.out::println);

    // ✅ Обновляем 2 карты (меняем владельца)
    if (cards.size() >= 2) {
      Card card1 = cards.get(0);
      Card updatedCard1 =
          new Card(
              card1.getId(), // ✅ ID ОБЯЗАТЕЛЕН
              card1.getCardNumber(),
              new Date(), // ✅ Новая дата истечения
              "John Doe Updated", // ✅ Новое имя владельца
              card1.getCardStatusId(),
              card1.getPaymentSystemId(),
              card1.getAccountId());
      cardService.updateCard(updatedCard1); // ✅ Передаём объект с полным набором данных

      Card card2 = cards.get(1);
      Card updatedCard2 =
          new Card(
              card2.getId(),
              card2.getCardNumber(),
              new Date(),
              "Jane Smith Updated",
              card2.getCardStatusId(),
              card2.getPaymentSystemId(),
              card2.getAccountId());
      cardService.updateCard(updatedCard2);

      System.out.println("\n✅ Updated 2 cards!");
    }

    logger.info("Clearing all accounts in the database!");
    accountService.clearTable();


    // ✅ Очищаем таблицу
    logger.info("Clearing all cards in the database!");
    cardService.clearTable();


    // ✅ Удаляем таблицу
    logger.info("Dropping Cards in the database!");
    cardService.dropTable();
    }
}


