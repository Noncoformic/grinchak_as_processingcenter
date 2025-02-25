package ru.edme;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import ru.edme.configuration.HibernateConfig;
import ru.edme.model.Card;
import ru.edme.service.AccountService;
import ru.edme.service.CardService;


public class Main {
  private static final Logger logger = LogManager.getLogger(Main.class);

  public static void main(String[] args) {
    // ✅ Создаём сервисы
    System.out.println("🔹 Starting Hibernate test...");

    try (Session session = HibernateConfig.getSessionFactory().openSession()) {
      System.out.println("✅ Hibernate is working! Session created successfully.");
    } catch (Exception e) {
      System.out.println("❌ Error connecting to Hibernate: " + e.getMessage());
    } finally {
      HibernateConfig.close();
    }



//    CardService cardService = new CardService();
//    AccountService accountService = new AccountService();
//
//    // ✅ Создаём таблицы
//    cardService.createTable();
//    System.out.println("✅ Table 'Card' created!");
//
//    // ✅ Добавляем 4 аккаунта (чтобы избежать ошибок с foreign key)
//    accountService.addAccount("40817810800000000001", new BigDecimal("10000.00"), 1L, 1L);
//    accountService.addAccount("40817810800000000002", new BigDecimal("15000.00"), 1L, 1L);
//    accountService.addAccount("40817810800000000003", new BigDecimal("20000.00"), 1L, 1L);
//    accountService.addAccount("40817810800000000004", new BigDecimal("25000.00"), 1L, 1L);
//    System.out.println("✅ 4 Accounts added.");
//
//    // ✅ Добавляем 4 карты (они теперь привязаны к существующим Account)
//    cardService.addCard("1111222233334444", Date.valueOf(LocalDate.of(2030, 5, 11)), "John Doe", 1L, 1L, 1L);
//    cardService.addCard("5555666677778888", Date.valueOf(LocalDate.of(2032, 1, 12)), "Jane Smith", 2L, 1L, 2L);
//    cardService.addCard("9999000011112222", Date.valueOf(LocalDate.of(2033, 1, 13)), "Alice Johnson", 1L, 2L, 3L);
//    cardService.addCard("3333444455556666", Date.valueOf(LocalDate.of(2027, 7, 23)), "Bob Brown", 2L, 2L, 4L);
//
//    // ✅ Получаем и выводим все карты
//    List<Card> cards = cardService.getAllCards();
//    System.out.println("\n📌 All cards in the database:");
//    cards.forEach(System.out::println);
//
//    // ✅ Обновляем 2 карты (меняем владельца)
//    if (cards.size() >= 2) {
//      Card card1 = cards.get(0);
//      Card updatedCard1 =
//          new Card(
//              card1.getId(), // ✅ ID ОБЯЗАТЕЛЕН
//              card1.getCardNumber(),
//                  Date.valueOf(LocalDate.of(2050,2,11)), // ✅ Новая дата истечения
//              "John Doe Updated", // ✅ Новое имя владельца
//              card1.getCardStatusId(),
//              card1.getPaymentSystemId(),
//              card1.getAccountId());
//      cardService.updateCard(updatedCard1); // ✅ Передаём объект с полным набором данных
//
//      Card card2 = cards.get(1);
//      Card updatedCard2 =
//          new Card(
//              card2.getId(),
//              card2.getCardNumber(),
//              Date.valueOf(LocalDate.of(2043,5,21)),
//              "Jane Smith Updated",
//              card2.getCardStatusId(),
//              card2.getPaymentSystemId(),
//              card2.getAccountId());
//      cardService.updateCard(updatedCard2);
//
//      System.out.println("\n✅ Updated 2 cards!");
//    }
//
//     //✅ Очищаем таблицу Account
//    logger.info("Clearing all accounts in the database!");
//    accountService.clearTable();
//
//
//    // ✅ Очищаем таблицу Card
//    logger.info("Clearing all cards in the database!");
//    cardService.clearTable();
//
//
//    // ✅ Удаляем таблицу
//    logger.info("Dropping Cards in the database!");
//    cardService.dropTable();
    }
}


