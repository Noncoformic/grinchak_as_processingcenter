package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.dao.Dao;
import ru.edme.model.IssuingBank;

import java.util.List;
import java.util.Optional;

public class IssuingBankService {
    private static final Logger logger = LogManager.getLogger(IssuingBankService.class);
    private final Dao<IssuingBank> issuingBankDao;

    public IssuingBankService(Dao<IssuingBank> issuingBankDao) {
        this.issuingBankDao = issuingBankDao;
    }

    public void createTable() {
        issuingBankDao.createTable();
    }

    public void clearTable() {
        issuingBankDao.clearTable();
    }

    public void dropTable() {
        issuingBankDao.dropTable();
    }

    public void addIssuingBank(String bic, String abbreviatedName) {

        if (issuingBankDao.getAll().stream().anyMatch(issuingBank -> issuingBank.getBic().equals(bic))) {
            logger.warn("⚠️ IssuingBank '{}' already exists. Skipping insert.", bic);
            return;
        }
        IssuingBank bank = IssuingBank.builder()
                .bic(bic)
                .abbreviatedName(abbreviatedName)
                .build();
        issuingBankDao.insert(bank);
        logger.info("✅ IssuingBank added: bic: " + bic + ", Name: " + abbreviatedName);
    }

}