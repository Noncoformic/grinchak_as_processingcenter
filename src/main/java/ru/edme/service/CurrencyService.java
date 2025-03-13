package ru.edme.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.edme.dao.Dao;
import ru.edme.model.Currency;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CurrencyService {
    private final Dao<Currency> currencyDao;

    public CurrencyService(@Qualifier("currencyHibernateDaoImpl") Dao<Currency> currencyDao) {
        this.currencyDao = currencyDao;
    }

    public void createTable() {
        currencyDao.createTable();
        log.info("✅ Table 'currency' created.");
    }

    public void clearTable() {
        currencyDao.clearTable();
        log.info("✅ Table 'currency' cleared.");
    }

    public void dropTable() {
        currencyDao.dropTable();
        log.info("✅ Table 'currency' dropped.");
    }

    public void addCurrency(String digitalCode, String letterCode, String name) {
        if (currencyDao.getAll().stream().anyMatch(currency -> currency.getCurrencyLetterCode().equals(letterCode))) {
            log.warn("⚠️ Currency '{}' already exists. Skipping insert.", letterCode);
            return;
        }

        Currency currency = Currency.builder()
                .currencyDigitalCode(digitalCode)
                .currencyLetterCode(letterCode)
                .currencyName(name)
                .build();

        currencyDao.insert(currency);
        log.info("✅ Currency added: {}", name);
    }

    public List<Currency> getAllCurrencies() {
        return currencyDao.getAll();
    }

    public Optional<Currency> getCurrencyById(Long id) {
        return currencyDao.getById(id);
    }

    public void deleteCurrency(Long id) {
        currencyDao.delete(id);
        log.info("❌ Currency deleted: {}", id);
    }

    public void updateCurrency(Long id, String digitalCode, String letterCode, String name) {
        Optional<Currency> optionalCurrency = currencyDao.getById(id);
        if (optionalCurrency.isPresent()) {
            Currency updatedCurrency = optionalCurrency.get().toBuilder()
                    .currencyDigitalCode(digitalCode)
                    .currencyLetterCode(letterCode)
                    .currencyName(name)
                    .build();
            currencyDao.update(updatedCurrency);
            log.info("🔄 Currency updated: {} to {}", optionalCurrency.get().getCurrencyLetterCode(), letterCode);
        } else {
            log.warn("Currency with id {} not found. Update skipped.", id);
        }
    }
}
