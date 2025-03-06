package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edme.dao.Dao;
import ru.edme.model.Currency;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceTest {
    private static final Logger logger = LogManager.getLogger(CurrencyServiceTest.class);

    @Mock
    private Dao<Currency> currencyDao;

    @InjectMocks
    private CurrencyService currencyService;

    private Currency currency1;
    private Currency currency2;

    @BeforeEach
    void setUp() {
        logger.info("Начало настройки тестовых данных для CurrencyServiceTest");
        currency1 = Currency.builder()
                .currencyDigitalCode("840")
                .currencyLetterCode("USD")
                .currencyName("US Dollar")
                .build();
        currency2 = Currency.builder()
                .currencyDigitalCode("978")
                .currencyLetterCode("EUR")
                .currencyName("Euro")
                .build();
        logger.info("Настройка тестовых данных для CurrencyServiceTest завершена");
    }

    @Test
    void createTable_Success() {
        logger.info("Начало теста: createTable_Success");
        currencyService.createTable();
        verify(currencyDao, times(1)).createTable();
        logger.info("Тест createTable_Success завершён успешно");
    }

    @Test
    void createTable_ThrowsException() {
        logger.info("Начало теста: createTable_ThrowsException");
        RuntimeException exception = new RuntimeException("Failed to create table");
        doThrow(exception).when(currencyDao).createTable();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> currencyService.createTable());
        assertEquals("Error in createTable: Failed to create table", thrown.getMessage());
        assertEquals(exception, thrown.getCause());
        verify(currencyDao, times(1)).createTable();
        logger.info("Тест createTable_ThrowsException завершён успешно");
    }

    @Test
    void clearTable_Success() {
        logger.info("Начало теста: clearTable_Success");
        currencyService.clearTable();
        verify(currencyDao, times(1)).clearTable();
        logger.info("Тест clearTable_Success завершён успешно");
    }

    @Test
    void clearTable_ThrowsException() {
        logger.info("Начало теста: clearTable_ThrowsException");
        RuntimeException exception = new RuntimeException("Failed to clear table");
        doThrow(exception).when(currencyDao).clearTable();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> currencyService.clearTable());
        assertEquals("Error in clearTable: Failed to clear table", thrown.getMessage());
        assertEquals(exception, thrown.getCause());
        verify(currencyDao, times(1)).clearTable();
        logger.info("Тест clearTable_ThrowsException завершён успешно");
    }

    @Test
    void dropTable_Success() {
        logger.info("Начало теста: dropTable_Success");
        currencyService.dropTable();
        verify(currencyDao, times(1)).dropTable();
        logger.info("Тест dropTable_Success завершён успешно");
    }

    @Test
    void dropTable_ThrowsException() {
        logger.info("Начало теста: dropTable_ThrowsException");
        RuntimeException exception = new RuntimeException("Failed to drop table");
        doThrow(exception).when(currencyDao).dropTable();

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> currencyService.dropTable());
        assertEquals("Error in dropTable: Failed to drop table", thrown.getMessage());
        assertEquals(exception, thrown.getCause());
        verify(currencyDao, times(1)).dropTable();
        logger.info("Тест dropTable_ThrowsException завершён успешно");
    }

    @Test
    void addCurrency_NewCurrency_Success() {
        logger.info("Начало теста: addCurrency_NewCurrency_Success");
        List<Currency> emptyList = List.of();
        when(currencyDao.getAll()).thenReturn(emptyList);

        currencyService.addCurrency("124", "CAD", "Canadian Dollar");

        verify(currencyDao, times(1)).getAll();
        verify(currencyDao, times(1)).insert(any(Currency.class));
        logger.info("Тест addCurrency_NewCurrency_Success завершён успешно");
    }

    @Test
    void addCurrency_ExistingCurrency_SkipInsert() {
        logger.info("Начало теста: addCurrency_ExistingCurrency_SkipInsert");
        List<Currency> existingCurrencies = Arrays.asList(currency1, currency2);
        when(currencyDao.getAll()).thenReturn(existingCurrencies);

        currencyService.addCurrency("840", "USD", "US Dollar");

        verify(currencyDao, times(1)).getAll();
        verify(currencyDao, never()).insert(any());
        logger.info("Тест addCurrency_ExistingCurrency_SkipInsert завершён успешно");
    }

    @Test
    void addCurrency_ThrowsException() {
        logger.info("Начало теста: addCurrency_ThrowsException");
        RuntimeException exception = new RuntimeException("Failed to add currency");
        when(currencyDao.getAll()).thenReturn(List.of());
        doThrow(exception).when(currencyDao).insert(any());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> currencyService.addCurrency("124", "CAD", "Canadian Dollar"));
        assertEquals("Error in addCurrency: Failed to add currency", thrown.getMessage());
        assertEquals(exception, thrown.getCause());
        verify(currencyDao, times(1)).getAll();
        verify(currencyDao, times(1)).insert(any());
        logger.info("Тест addCurrency_ThrowsException завершён успешно");
    }
}