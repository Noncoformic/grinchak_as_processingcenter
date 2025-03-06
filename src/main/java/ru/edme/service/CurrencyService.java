package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.dao.Dao;
import ru.edme.model.Currency;

import java.util.List;
import java.util.Optional;

public class CurrencyService {
    private static final Logger logger = LogManager.getLogger(CurrencyService.class);
    private final Dao<Currency> currencyDao;

    public CurrencyService(Dao<Currency> currencyDao) {
        this.currencyDao = currencyDao;
    }

    public void createTable() {
        try {
            currencyDao.createTable();
        } catch (RuntimeException e) {
            logger.error("Error in createTable: " + e.getMessage());
            throw new RuntimeException("Error in createTable: " + e.getMessage(), e);
        }
    }

    public void clearTable() {
        try {
            currencyDao.clearTable();
        } catch (RuntimeException e) {
            logger.error("Error in clearTable: " + e.getMessage());
            throw new RuntimeException("Error in clearTable: " + e.getMessage(), e);
        }
    }

    public void dropTable() {
        try {
            currencyDao.dropTable();
        } catch (RuntimeException e) {
            logger.error("Error in dropTable: " + e.getMessage());
            throw new RuntimeException("Error in dropTable: " + e.getMessage(), e);
        }
    }

    public void addCurrency(String digitalCode, String letterCode, String name) {
        try {
            // Проверка на существование
            if (currencyDao.getAll().stream().anyMatch(currency -> currency.getCurrencyLetterCode().equals(letterCode))) {
                logger.warn("⚠️ Currency '{}' already exists. Skipping insert.", letterCode);
                return;
            }
            Currency currency = Currency.builder()
                    .currencyDigitalCode(digitalCode)
                    .currencyLetterCode(letterCode)
                    .currencyName(name)
                    .build();
            currencyDao.insert(currency);
            logger.info("✅ Currency added: " + name);
        } catch (RuntimeException e) {
            logger.error("Error in addCurrency: " + e.getMessage());
            throw new RuntimeException("Error in addCurrency: " + e.getMessage(), e);
        }
    }

}