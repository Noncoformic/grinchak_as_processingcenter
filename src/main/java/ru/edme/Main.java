package ru.edme;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.edme.configuration.AppConfig;
import ru.edme.dao.Dao;
import ru.edme.dao.DaoFactory;
import ru.edme.dao.DaoType;
import ru.edme.model.*;
import ru.edme.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("🔹 Starting Processing Center application...");

        // Выбираем тип DAO: JDBC или HIBERNATE
        DaoType daoType = DaoType.HIBERNATE; // Меняем здесь для переключения
        DaoFactory daoFactory = new DaoFactory(daoType);

//        // Получаем DAO через фабрику
//        Dao<Card> cardDao = daoFactory.getDao(Card.class);
//        Dao<Account> accountDao = daoFactory.getDao(Account.class);
//        Dao<CardStatus> cardStatusDao = daoFactory.getDao(CardStatus.class);
//        Dao<Currency> currencyDao = daoFactory.getDao(Currency.class);
//        Dao<IssuingBank> issuingBankDao = daoFactory.getDao(IssuingBank.class);
//
//        // Создаём сервисы, передавая им DAO
//        CardBuilderService cardBuilderService = new CardBuilderService();
//
//        AccountService accountService = new AccountService(accountDao);
//        CardStatusService cardStatusService = new CardStatusService(cardStatusDao);
//        CurrencyService currencyService = new CurrencyService(currencyDao);
//        IssuingBankService issuingBankService = new IssuingBankService(issuingBankDao);

        log.info("🔹 Starting Processing Center application...");

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        CardService cardService = context.getBean(CardService.class);

        log.info("✅ Spring Context Initialized!");

        // Пример работы с картами через Spring Data JPA
        cardService.createTable();
    }
    }

