package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.edme.dao.Dao;
import ru.edme.model.IssuingBank;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class IssuingBankServiceTest {
    private static final Logger logger = LogManager.getLogger(IssuingBankServiceTest.class);

    @Mock
    private Dao<IssuingBank> issuingBankDao;

    private IssuingBankService issuingBankService;

    @BeforeEach
    void setUp() {
        logger.info("Начало настройки тестовых данных для IssuingBankServiceTest");
        MockitoAnnotations.openMocks(this);
        issuingBankService = new IssuingBankService(issuingBankDao);
        logger.info("Настройка тестовых данных для IssuingBankServiceTest завершена");
    }

    @Test
    void createTable_ShouldCallDaoCreateTable() {
        logger.info("Начало теста: createTable_ShouldCallDaoCreateTable");
        issuingBankService.createTable();
        verify(issuingBankDao, times(1)).createTable();
        logger.info("Тест createTable_ShouldCallDaoCreateTable завершён успешно");
    }

    @Test
    void clearTable_ShouldCallDaoClearTable() {
        logger.info("Начало теста: clearTable_ShouldCallDaoClearTable");
        issuingBankService.clearTable();
        verify(issuingBankDao, times(1)).clearTable();
        logger.info("Тест clearTable_ShouldCallDaoClearTable завершён успешно");
    }

    @Test
    void dropTable_ShouldCallDaoDropTable() {
        logger.info("Начало теста: dropTable_ShouldCallDaoDropTable");
        issuingBankService.dropTable();
        verify(issuingBankDao, times(1)).dropTable();
        logger.info("Тест dropTable_ShouldCallDaoDropTable завершён успешно");
    }

    @Test
    void addIssuingBank_WhenBankDoesNotExist_ShouldInsertBank() {
        logger.info("Начало теста: addIssuingBank_WhenBankDoesNotExist_ShouldInsertBank");
        // Arrange
        String bic = "123456789";
        String abbreviatedName = "TestBank";
        when(issuingBankDao.getAll()).thenReturn(new ArrayList<>());

        // Act
        issuingBankService.addIssuingBank(bic, abbreviatedName);

        // Assert
        verify(issuingBankDao, times(1)).insert(any(IssuingBank.class));
        verify(issuingBankDao, times(1)).getAll();
        logger.info("Тест addIssuingBank_WhenBankDoesNotExist_ShouldInsertBank завершён успешно");
    }

    @Test
    void addIssuingBank_WhenBankAlreadyExists_ShouldNotInsertBank() {
        logger.info("Начало теста: addIssuingBank_WhenBankAlreadyExists_ShouldNotInsertBank");
        // Arrange
        String bic = "123456789";
        String abbreviatedName = "TestBank";
        IssuingBank existingBank = IssuingBank.builder()
                .bic(bic)
                .abbreviatedName("ExistingBank")
                .build();
        List<IssuingBank> existingBanks = List.of(existingBank);
        when(issuingBankDao.getAll()).thenReturn(existingBanks);

        // Act
        issuingBankService.addIssuingBank(bic, abbreviatedName);

        // Assert
        verify(issuingBankDao, never()).insert(any(IssuingBank.class));
        verify(issuingBankDao, times(1)).getAll();
        logger.info("Тест addIssuingBank_WhenBankAlreadyExists_ShouldNotInsertBank завершён успешно");

    }

    @Test
    void addIssuingBank_WhenBanksAlreadyExists_ShouldNotInsertAnyBank() {
        logger.info("Начало теста: addIssuingBank_WhenBanksAlreadyExists_ShouldNotInsertAnyBank");
        // Arrange
        String bic1 = "123456789";
        String bic2 = "987654321";
        String abbreviatedName1 = "TestBank1";
        String abbreviatedName2 = "TestBank2";

        IssuingBank existingBank1 = IssuingBank.builder()
                .bic(bic1)
                .abbreviatedName(abbreviatedName1)
                .build();

        IssuingBank existingBank2 = IssuingBank.builder()
                .bic(bic2)
                .abbreviatedName(abbreviatedName2)
                .build();

        List<IssuingBank> existingBanks = List.of(existingBank1, existingBank2);
        when(issuingBankDao.getAll()).thenReturn(existingBanks);

        // Act
        issuingBankService.addIssuingBank(bic1, abbreviatedName1);
        issuingBankService.addIssuingBank(bic2, abbreviatedName2);

        // Assert
        verify(issuingBankDao, never()).insert(any(IssuingBank.class));
        verify(issuingBankDao, times(2)).getAll();
        logger.info("Тест addIssuingBank_WhenBanksAlreadyExists_ShouldNotInsertAnyBank завершён успешно");
    }

    @Test
    void addIssuingBank_WhenBankDoesNotExistAndOtherBanksExist_ShouldInsertBank() {
        logger.info("Начало теста: addIssuingBank_WhenBankDoesNotExistAndOtherBanksExist_ShouldInsertBank");
        // Arrange
        String bic = "123456789";
        String abbreviatedName = "TestBank";
        String anotherBic = "987654321";
        String anotherName = "AnotherBank";

        IssuingBank existingBank = IssuingBank.builder()
                .bic(anotherBic)
                .abbreviatedName(anotherName)
                .build();
        List<IssuingBank> existingBanks = List.of(existingBank);
        when(issuingBankDao.getAll()).thenReturn(existingBanks);

        // Act
        issuingBankService.addIssuingBank(bic, abbreviatedName);

        // Assert
        verify(issuingBankDao, times(1)).insert(any(IssuingBank.class));
        verify(issuingBankDao, times(1)).getAll();
        logger.info("Тест addIssuingBank_WhenBankDoesNotExistAndOtherBanksExist_ShouldInsertBank завершён успешно");
    }
}