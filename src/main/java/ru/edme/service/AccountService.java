package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.dao.Dao;
import ru.edme.model.Account;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class AccountService {
    private static final Logger logger = LogManager.getLogger(AccountService.class);
    private final Dao<Account> accountDao;

    public AccountService(Dao<Account> accountDao) {
        this.accountDao = accountDao;
    }

    public void createTable() {
        try {
            accountDao.createTable();
        }catch (RuntimeException e){
            logger.error("Error in createTable: " + e.getMessage());
            throw new RuntimeException("Error in createTable: " + e.getMessage(), e);
        }
    }

    public void clearTable() {
        try {
            accountDao.clearTable();
        }catch (RuntimeException e){
            logger.error("Error in clearTable: " + e.getMessage());
            throw new RuntimeException("Error in clearTable: " + e.getMessage(), e);
        }
    }

    public void dropTable() {
        try {
            accountDao.dropTable();
        }catch (RuntimeException e){
            logger.error("Error in dropTable: " + e.getMessage());
            throw new RuntimeException("Error in dropTable: " + e.getMessage(), e);
        }
    }

    public void addAccount(String accountNumber, BigDecimal balance, Long currencyId, Long issuingBankId) {
        if (accountDao.getAll().stream().anyMatch(account -> account.getAccountNumber().equals(accountNumber))) {
            logger.warn("⚠️ Account '{}' already exists. Skipping insert.", accountNumber);
            return;
        }

        try {
            Account account = Account.builder()
                    .accountNumber(accountNumber)
                    .balance(balance)
                    .currencyId(currencyId)
                    .issuingBankId(issuingBankId)
                    .build();
            accountDao.insert(account);
            logger.info("✅ Account added: " + accountNumber);
        } catch (RuntimeException e) {
            logger.error("Error in addAccount: " + e.getMessage());
            throw new RuntimeException("Error in addAccount: " + e.getMessage(), e);
        }
    }

    public List<Account> getAllAccounts() {
        try {
            return accountDao.getAll();
        } catch (RuntimeException e) {
            logger.error("Error in getAllAccounts: " + e.getMessage());
            throw new RuntimeException("Error in getAllAccounts: " + e.getMessage(), e);
        }
    }

    public Optional<Account> getAccountById(Long id) {
        try {
            return accountDao.getById(id);
        } catch (RuntimeException e) {
            logger.error("Error in getAccountById: " + e.getMessage());
            throw new RuntimeException("Error in getAccountById: " + e.getMessage(), e);
        }
    }

    public void deleteAccount(Long id) {
        try {
            accountDao.delete(id);
            logger.info("❌ Account deleted: " + id);
        } catch (RuntimeException e) {
            logger.error("Error in deleteAccount: " + e.getMessage());
            throw new RuntimeException("Error in deleteAccount: " + e.getMessage(), e);
        }
    }

    public void updateAccount(Account account) {
        try {
            accountDao.update(account);
            logger.info("🔄 Account updated: " + account.getAccountNumber());
        } catch (RuntimeException e) {
            logger.error("Error in updateAccount: " + e.getMessage());
            throw new RuntimeException("Error in updateAccount: " + e.getMessage(), e);
        }
    }
}