package ru.edme.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
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
import ru.edme.model.CardStatus;

@ExtendWith(MockitoExtension.class)
class CardStatusServiceTest {
    private static final Logger logger = LogManager.getLogger(CardStatusServiceTest.class);
    @Mock
    private Dao<CardStatus> cardStatusDao;

    private CardStatusService cardStatusService;

    @BeforeEach
    void setUp() {
        logger.info("Начало настройки тестовых данных для CardStatusServiceTest");
        cardStatusService = new CardStatusService(cardStatusDao);
        logger.info("Настройка тестовых данных для CardStatusServiceTest завершена");
    }

    @Test
    void createTable_Success() {
        logger.info("Начало теста: createTable_Success");
        cardStatusService.createTable();
        verify(cardStatusDao, times(1)).createTable();
        logger.info("Тест createTable_Success завершён успешно");
    }

    @Test
    void createTable_RuntimeException() {
        logger.info("Начало теста: createTable_RuntimeException");
        doThrow(new RuntimeException("Test exception")).when(cardStatusDao).createTable();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.createTable());
        assertEquals("Error in createTable: Test exception", exception.getMessage());
        verify(cardStatusDao, times(1)).createTable();
        logger.info("Тест createTable_RuntimeException завершён успешно");
    }

    @Test
    void dropTable_Success() {
        logger.info("Начало теста: dropTable_Success");
        cardStatusService.dropTable();
        verify(cardStatusDao, times(1)).dropTable();
        logger.info("Тест dropTable_Success завершён успешно");
    }

    @Test
    void dropTable_RuntimeException() {
        logger.info("Начало теста: dropTable_RuntimeException");
        doThrow(new RuntimeException("Test exception")).when(cardStatusDao).dropTable();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.dropTable());
        assertEquals("Error in dropTable: Test exception", exception.getMessage());
        verify(cardStatusDao, times(1)).dropTable();
        logger.info("Тест dropTable_RuntimeException завершён успешно");
    }

    @Test
    void clearTable_Success() {
        logger.info("Начало теста: clearTable_Success");
        cardStatusService.clearTable();
        verify(cardStatusDao, times(1)).clearTable();
        logger.info("Тест clearTable_Success завершён успешно");
    }

    @Test
    void clearTable_RuntimeException() {
        logger.info("Начало теста: clearTable_RuntimeException");
        doThrow(new RuntimeException("Test exception")).when(cardStatusDao).clearTable();
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.clearTable());
        assertEquals("Error in clearTable: Test exception", exception.getMessage());
        verify(cardStatusDao, times(1)).clearTable();
        logger.info("Тест clearTable_RuntimeException завершён успешно");
    }

    @Test
    void getCardStatusByName_Existing() {
        logger.info("Начало теста: getCardStatusByName_Existing");
        CardStatus existingStatus = CardStatus.builder().id(1L).cardStatusName("Existing").build();
        when(cardStatusDao.getAll()).thenReturn(List.of(existingStatus));
        CardStatus result = cardStatusService.getCardStatusByName("Existing");
        assertEquals(existingStatus, result);
        verify(cardStatusDao, never()).insert(any());
        logger.info("Тест getCardStatusByName_Existing завершён успешно");
    }

    @Test
    void getCardStatusByName_New() {
        logger.info("Начало теста: getCardStatusByName_New");
        when(cardStatusDao.getAll()).thenReturn(List.of());
        CardStatus expectedStatus = CardStatus.builder().cardStatusName("New").build();
        CardStatus result = cardStatusService.getCardStatusByName("New");
        assertEquals(expectedStatus.getCardStatusName(), result.getCardStatusName());
        verify(cardStatusDao, times(1)).insert(any(CardStatus.class));
        logger.info("Тест getCardStatusByName_New завершён успешно");
    }

    @Test
    void getCardStatusByName_RuntimeException() {
        logger.info("Начало теста: getCardStatusByName_RuntimeException");
        when(cardStatusDao.getAll()).thenThrow(new RuntimeException("Test exception"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.getCardStatusByName("New"));
        assertEquals("Error in addCardStatus: Test exception", exception.getMessage());
        verify(cardStatusDao, never()).insert(any(CardStatus.class));
        logger.info("Тест getCardStatusByName_RuntimeException завершён успешно");
    }

    @Test
    void getAllCardStatuses_Success() {
        logger.info("Начало теста: getAllCardStatuses_Success");
        List<CardStatus> expectedStatuses = Arrays.asList(
                CardStatus.builder().id(1L).cardStatusName("Status1").build(),
                CardStatus.builder().id(2L).cardStatusName("Status2").build()
        );
        when(cardStatusDao.getAll()).thenReturn(expectedStatuses);
        List<CardStatus> result = cardStatusService.getAllCardStatuses();
        assertEquals(expectedStatuses, result);
        logger.info("Тест getAllCardStatuses_Success завершён успешно");
    }

    @Test
    void getAllCardStatuses_RuntimeException() {
        logger.info("Начало теста: getAllCardStatuses_RuntimeException");
        when(cardStatusDao.getAll()).thenThrow(new RuntimeException("Test exception"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.getAllCardStatuses());
        assertEquals("Error in getAllCardStatuses: Test exception", exception.getMessage());
        logger.info("Тест getAllCardStatuses_RuntimeException завершён успешно");
    }

    @Test
    void getCardStatusById_Success() {
        logger.info("Начало теста: getCardStatusById_Success");
        CardStatus expectedStatus = CardStatus.builder().id(1L).cardStatusName("Status1").build();
        when(cardStatusDao.getById(1L)).thenReturn(Optional.of(expectedStatus));
        Optional<CardStatus> result = cardStatusService.getCardStatusById(1L);
        assertTrue(result.isPresent());
        assertEquals(expectedStatus, result.get());
        logger.info("Тест getCardStatusById_Success завершён успешно");
    }

    @Test
    void getCardStatusById_NotFound() {
        logger.info("Начало теста: getCardStatusById_NotFound");
        when(cardStatusDao.getById(1L)).thenReturn(Optional.empty());
        Optional<CardStatus> result = cardStatusService.getCardStatusById(1L);
        assertFalse(result.isPresent());
        logger.info("Тест getCardStatusById_NotFound завершён успешно");
    }

    @Test
    void getCardStatusById_RuntimeException() {
        logger.info("Начало теста: getCardStatusById_RuntimeException");
        when(cardStatusDao.getById(1L)).thenThrow(new RuntimeException("Test exception"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.getCardStatusById(1L));
        assertEquals("Error in getCardStatusById: Test exception", exception.getMessage());
        logger.info("Тест getCardStatusById_RuntimeException завершён успешно");
    }

    @Test
    void deleteCardStatus_Success() {
        logger.info("Начало теста: deleteCardStatus_Success");
        cardStatusService.deleteCardStatus(1L);
        verify(cardStatusDao, times(1)).delete(1L);
        logger.info("Тест deleteCardStatus_Success завершён успешно");
    }

    @Test
    void deleteCardStatus_RuntimeException() {
        logger.info("Начало теста: deleteCardStatus_RuntimeException");
        doThrow(new RuntimeException("Test exception")).when(cardStatusDao).delete(1L);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.deleteCardStatus(1L));
        assertEquals("Error in deleteCardStatus: Test exception", exception.getMessage());
        verify(cardStatusDao, times(1)).delete(1L);
        logger.info("Тест deleteCardStatus_RuntimeException завершён успешно");
    }

    @Test
    void updateCardStatus_Success() {
        logger.info("Начало теста: updateCardStatus_Success");
        CardStatus existingStatus = CardStatus.builder().id(1L).cardStatusName("Old").build();
        when(cardStatusDao.getById(1L)).thenReturn(Optional.of(existingStatus));
        cardStatusService.updateCardStatus(1L, "New");
        verify(cardStatusDao, times(1)).update(any(CardStatus.class));
        logger.info("Тест updateCardStatus_Success завершён успешно");
    }

    @Test
    void updateCardStatus_NotFound() {
        logger.info("Начало теста: updateCardStatus_NotFound");
        when(cardStatusDao.getById(1L)).thenReturn(Optional.empty());
        cardStatusService.updateCardStatus(1L,"New");
        verify(cardStatusDao, never()).update(any());
        logger.info("Тест updateCardStatus_NotFound завершён успешно");
    }

    @Test
    void updateCardStatus_RuntimeException() {
        logger.info("Начало теста: updateCardStatus_RuntimeException");
        when(cardStatusDao.getById(1L)).thenThrow(new RuntimeException("Test exception"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> cardStatusService.updateCardStatus(1L,"New"));
        assertEquals("Error in updateCardStatus: Test exception", exception.getMessage());
        verify(cardStatusDao, never()).update(any());
        logger.info("Тест updateCardStatus_RuntimeException завершён успешно");
    }
}