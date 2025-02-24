package ru.edme.service;

import ru.edme.dao.jdbc.AccountJDBCDaoImpl;
import ru.edme.model.Account;

import java.math.BigDecimal;
import java.util.List;

public class AccountService {
    private final AccountJDBCDaoImpl accountDao;

    public AccountService() {
        this.accountDao = new AccountJDBCDaoImpl();
    }

    public void dropTable() {
        this.accountDao.dropTable();
    }

    public void clearTable() {
        this.accountDao.clearTable();
    }

    public void createTable() {
        accountDao.createTable();
    }

    public void addAccount(String accountNumber, BigDecimal balance, Long currencyId, Long issuingBankId) {
        Account account = new Account(null, accountNumber, balance, currencyId, issuingBankId);
        accountDao.insert(account);
        System.out.println("✅ Account added: " + accountNumber);
    }

    public List<Account> getAllAccounts() {
        return accountDao.getAll();
    }

    public Account getAccountById(Long id) {
        return accountDao.getById(id);
    }

    public void updateAccount(Long id, String newAccountNumber, BigDecimal newBalance) {
        Account account = accountDao.getById(id);
        if (account == null) {
            System.out.println("⚠️ Account not found!");
            return;
        }
        account.setAccountNumber(newAccountNumber);
        account.setBalance(newBalance);
        accountDao.update(account);
        System.out.println("✅ Account updated: " + account);
    }

    public void deleteAccount(Long id) {
        accountDao.delete(id);
        System.out.println("❌ Account deleted: " + id);
    }
}
