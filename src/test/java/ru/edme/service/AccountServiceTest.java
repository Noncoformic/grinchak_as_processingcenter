package ru.edme.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edme.dao.Dao;
import ru.edme.model.Account;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    private static final Logger logger = LogManager.getLogger(AccountServiceTest.class);

    @Mock
    private Dao<Account> accountDao;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        logger.info("Начало настройки тестовых данных для AccountServiceTest");
        accountService = new AccountService(accountDao);
        logger.info("Настройка тестовых данных для AccountServiceTest завершена");
    }

    @Test
    void createTable_Success() {
        logger.info("Начало теста: createTable_Success");
        accountService.createTable();
        verify(accountDao, times(1)).createTable();
        logger.info("Тест createTable_Success завершён успешно");
    }

    @Test
    void createTable_ThrowsException() {
        logger.info("Начало теста: createTable_ThrowsException");
        doThrow(new RuntimeException("Test exception")).when(accountDao).createTable();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.createTable());
        assertEquals("Error in createTable: Test exception", exception.getMessage());
        verify(accountDao, times(1)).createTable();
        logger.info("Тест createTable_ThrowsException завершён успешно");
    }

    @Test
    void clearTable_Success() {
        logger.info("Начало теста: clearTable_Success");
        accountService.clearTable();
        verify(accountDao, times(1)).clearTable();
        logger.info("Тест clearTable_Success завершён успешно");
    }

    @Test
    void clearTable_ThrowsException() {
        logger.info("Начало теста: clearTable_ThrowsException");
        doThrow(new RuntimeException("Test exception")).when(accountDao).clearTable();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.clearTable());
        assertEquals("Error in clearTable: Test exception", exception.getMessage());
        verify(accountDao, times(1)).clearTable();
        logger.info("Тест clearTable_ThrowsException завершён успешно");
    }

    @Test
    void dropTable_Success() {
        logger.info("Начало теста: dropTable_Success");
        accountService.dropTable();
        verify(accountDao, times(1)).dropTable();
        logger.info("Тест dropTable_Success завершён успешно");
    }

    @Test
    void dropTable_ThrowsException() {
        logger.info("Начало теста: dropTable_ThrowsException");
        doThrow(new RuntimeException("Test exception")).when(accountDao).dropTable();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.dropTable());
        assertEquals("Error in dropTable: Test exception", exception.getMessage());
        verify(accountDao, times(1)).dropTable();
        logger.info("Тест dropTable_ThrowsException завершён успешно");
    }

    @Test
    void addAccount_Success() {
        logger.info("Начало теста: addAccount_Success");
        String accountNumber = "12345";
        BigDecimal balance = BigDecimal.valueOf(100.00);
        Long currencyId = 1L;
        Long issuingBankId = 1L;
        when(accountDao.getAll()).thenReturn(new ArrayList<>());
        accountService.addAccount(accountNumber, balance, currencyId, issuingBankId);
        verify(accountDao, times(1)).insert(any(Account.class));
        logger.info("Тест addAccount_Success завершён успешно");
    }

    @Test
    void addAccount_AccountAlreadyExists() {
        logger.info("Начало теста: addAccount_AccountAlreadyExists");
        String accountNumber = "12345";
        BigDecimal balance = BigDecimal.valueOf(100.00);
        Long currencyId = 1L;
        Long issuingBankId = 1L;
        Account existingAccount = Account.builder().accountNumber(accountNumber).build();
        List<Account> existingAccounts = List.of(existingAccount);
        when(accountDao.getAll()).thenReturn(existingAccounts);

        accountService.addAccount(accountNumber, balance, currencyId, issuingBankId);
        verify(accountDao, never()).insert(any(Account.class));
        logger.info("Тест addAccount_AccountAlreadyExists завершён успешно");
    }

    @Test
    void addAccount_ThrowsException() {
        logger.info("Начало теста: addAccount_ThrowsException");
        String accountNumber = "12345";
        BigDecimal balance = BigDecimal.valueOf(100.00);
        Long currencyId = 1L;
        Long issuingBankId = 1L;
        when(accountDao.getAll()).thenReturn(new ArrayList<>());
        doThrow(new RuntimeException("Test exception")).when(accountDao).insert(any(Account.class));
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> accountService.addAccount(accountNumber, balance, currencyId, issuingBankId));
        assertEquals("Error in addAccount: Test exception", exception.getMessage());
        verify(accountDao, times(1)).insert(any(Account.class));
        logger.info("Тест addAccount_ThrowsException завершён успешно");
    }

    @Test
    void getAllAccounts_Success() {
        logger.info("Начало теста: getAllAccounts_Success");
        List<Account> expectedAccounts = List.of(new Account(), new Account());
        when(accountDao.getAll()).thenReturn(expectedAccounts);
        List<Account> actualAccounts = accountService.getAllAccounts();
        assertEquals(expectedAccounts, actualAccounts);
        verify(accountDao, times(1)).getAll();
        logger.info("Тест getAllAccounts_Success завершён успешно");
    }

    @Test
    void getAllAccounts_ThrowsException() {
        logger.info("Начало теста: getAllAccounts_ThrowsException");
        doThrow(new RuntimeException("Test exception")).when(accountDao).getAll();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.getAllAccounts());
        assertEquals("Error in getAllAccounts: Test exception", exception.getMessage());
        verify(accountDao, times(1)).getAll();
        logger.info("Тест getAllAccounts_ThrowsException завершён успешно");
    }

    @Test
    void getAccountById_Success() {
        logger.info("Начало теста: getAccountById_Success");
        Long accountId = 1L;
        Account expectedAccount = new Account();
        when(accountDao.getById(accountId)).thenReturn(Optional.of(expectedAccount));
        Optional<Account> actualAccount = accountService.getAccountById(accountId);
        assertTrue(actualAccount.isPresent());
        assertEquals(expectedAccount, actualAccount.get());
        verify(accountDao, times(1)).getById(accountId);
        logger.info("Тест getAccountById_Success завершён успешно");
    }

    @Test
    void getAccountById_ThrowsException() {
        logger.info("Начало теста: getAccountById_ThrowsException");
        Long accountId = 1L;
        doThrow(new RuntimeException("Test exception")).when(accountDao).getById(accountId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.getAccountById(accountId));
        assertEquals("Error in getAccountById: Test exception", exception.getMessage());
        verify(accountDao, times(1)).getById(accountId);
        logger.info("Тест getAccountById_ThrowsException завершён успешно");
    }

    @Test
    void deleteAccount_Success() {
        logger.info("Начало теста: deleteAccount_Success");
        Long accountId = 1L;
        accountService.deleteAccount(accountId);
        verify(accountDao, times(1)).delete(accountId);
        logger.info("Тест deleteAccount_Success завершён успешно");
    }

    @Test
    void deleteAccount_ThrowsException() {
        logger.info("Начало теста: deleteAccount_ThrowsException");
        Long accountId = 1L;
        doThrow(new RuntimeException("Test exception")).when(accountDao).delete(accountId);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.deleteAccount(accountId));
        assertEquals("Error in deleteAccount: Test exception", exception.getMessage());
        verify(accountDao, times(1)).delete(accountId);
        logger.info("Тест deleteAccount_ThrowsException завершён успешно");
    }

    @Test
    void updateAccount_Success() {
        logger.info("Начало теста: updateAccount_Success");
        Account account = new Account();
        accountService.updateAccount(account);
        verify(accountDao, times(1)).update(account);
        logger.info("Тест updateAccount_Success завершён успешно");
    }

    @Test
    void updateAccount_ThrowsException() {
        logger.info("Начало теста: updateAccount_ThrowsException");
        Account account = new Account();
        doThrow(new RuntimeException("Test exception")).when(accountDao).update(account);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> accountService.updateAccount(account));
        assertEquals("Error in updateAccount: Test exception", exception.getMessage());
        verify(accountDao, times(1)).update(account);
        logger.info("Тест updateAccount_ThrowsException завершён успешно");
    }
}