package ru.edme.service;

import ru.edme.dao.jdbc.TransactionJDBCDaoImpl;
import ru.edme.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class TransactionService {
    private final TransactionJDBCDaoImpl transactionDao;

    public TransactionService() {
        this.transactionDao = new TransactionJDBCDaoImpl();
    }

    public void createTable() {
        transactionDao.createTable();
    }

    public void clearTable() {
        transactionDao.clearTable();
    }

    public  void dropTable() {
        transactionDao.dropTable();
    }

    public void addTransaction(Date transactionDate, BigDecimal sum, String transactionName, Long accountId,
            Long transactionTypeId, Long cardId, Long terminalId, Long responseCodeId,
            String authorizationCode) {
        Transaction transaction = new Transaction(null, transactionDate, sum, transactionName, accountId, transactionTypeId, cardId, terminalId, responseCodeId, authorizationCode, null, null);
        transactionDao.insert(transaction);
        System.out.println("✅ Transaction added: " + transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionDao.getAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionDao.getById(id);
    }

    public void deleteTransaction(Long id) {
        transactionDao.delete(id);
        System.out.println("❌ Transaction deleted: " + id);
    }
}
