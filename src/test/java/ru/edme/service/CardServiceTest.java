package ru.edme.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
  private static final Logger logger = LogManager.getLogger(CardServiceTest.class);

  @Mock private Dao<Card> cardDao;

  @InjectMocks private CardService cardService;

  private Card testCard;

  @BeforeEach
  void setUp() {
    logger.info("Начало настройки тестовых данных для CardServiceTest");
    testCard =
            Card.builder()
                    .id(1L)
                    .cardNumber("1234567890123456")
                    .expirationDate(LocalDate.now().plusYears(5))
                    .holderName("Test Holder")
                    .cardStatusId(1L)
                    .paymentSystemId(1L)
                    .accountId(1L)
                    .receivedFromIssuingBank(LocalDateTime.now())
                    .sentToIssuingBank(LocalDateTime.now())
                    .build();
    logger.info("Настройка тестовых данных для CardServiceTest завершена");
  }

  @Test
  void createTable_Success() {
    logger.info("Начало теста: createTable_Success");
    cardService.createTable();
    verify(cardDao, times(1)).createTable();
    logger.info("Тест createTable_Success завершён успешно");
  }

  @Test
  void createTable_Exception() {
    logger.info("Начало теста: createTable_Exception");
    doThrow(new RuntimeException("Simulated exception")).when(cardDao).createTable();
    assertThrows(RuntimeException.class, () -> cardService.createTable());
    verify(cardDao, times(1)).createTable();
    logger.info("Тест createTable_Exception завершён успешно");
  }

  @Test
  void clearTable_Success() {
    logger.info("Начало теста: clearTable_Success");
    cardService.clearTable();
    verify(cardDao, times(1)).clearTable();
    logger.info("Тест clearTable_Success завершён успешно");
  }

  @Test
  void clearTable_Exception() {
    logger.info("Начало теста: clearTable_Exception");
    doThrow(new RuntimeException("Simulated exception")).when(cardDao).clearTable();
    assertThrows(RuntimeException.class, () -> cardService.clearTable());
    verify(cardDao, times(1)).clearTable();
    logger.info("Тест clearTable_Exception завершён успешно");
  }

  @Test
  void dropTable_Success() {
    logger.info("Начало теста: dropTable_Success");
    cardService.dropTable();
    verify(cardDao, times(1)).dropTable();
    logger.info("Тест dropTable_Success завершён успешно");
  }

  @Test
  void dropTable_Exception() {
    logger.info("Начало теста: dropTable_Exception");
    doThrow(new RuntimeException("Simulated exception")).when(cardDao).dropTable();
    assertThrows(RuntimeException.class, () -> cardService.dropTable());
    verify(cardDao, times(1)).dropTable();
    logger.info("Тест dropTable_Exception завершён успешно");
  }

  @Test
  void addCard_Success() {
    logger.info("Начало теста: addCard_Success");
    when(cardDao.getAll()).thenReturn(new ArrayList<>());
    cardService.addCard(LocalDate.now(), "Test Holder", 1L, 1L, 1L);
    verify(cardDao, times(1)).insert(any(Card.class));
    logger.info("Тест addCard_Success завершён успешно");
  }

  @Test
  void addCard_CardAlreadyExists() {
    logger.info("Начало теста: addCard_CardAlreadyExists");
    List<Card> existingCards = new ArrayList<>();
    existingCards.add(testCard);
    when(cardDao.getAll()).thenReturn(existingCards);

    // Mock generateCardNumber to return the same card number as the existing one.
    CardService cardServiceSpy = spy(cardService);
    doReturn(testCard.getCardNumber()).when(cardServiceSpy).generateCardNumber();

    cardServiceSpy.addCard(LocalDate.now(), "Test Holder", 1L, 1L, 1L);
    verify(cardDao, never()).insert(any(Card.class));
    logger.info("Тест addCard_CardAlreadyExists завершён успешно");
  }

  @Test
  void addCard_Exception() {
    logger.info("Начало теста: addCard_Exception");
    when(cardDao.getAll()).thenReturn(new ArrayList<>());
    doThrow(new RuntimeException("Simulated exception")).when(cardDao).insert(any(Card.class));
    assertThrows(
            RuntimeException.class,
            () -> cardService.addCard(LocalDate.now(), "Test Holder", 1L, 1L, 1L));
    verify(cardDao, times(1)).insert(any(Card.class));
    logger.info("Тест addCard_Exception завершён успешно");
  }

  @Test
  void getAllCards_Success() {
    logger.info("Начало теста: getAllCards_Success");
    List<Card> expectedCards = List.of(testCard);
    when(cardDao.getAll()).thenReturn(expectedCards);
    List<Card> actualCards = cardService.getAllCards();
    assertEquals(expectedCards, actualCards);
    verify(cardDao, times(1)).getAll();
    logger.info("Тест getAllCards_Success завершён успешно");
  }

  @Test
  void getAllCards_Exception() {
    logger.info("Начало теста: getAllCards_Exception");
    doThrow(new RuntimeException("Simulated exception")).when(cardDao).getAll();
    assertThrows(RuntimeException.class, () -> cardService.getAllCards());
    verify(cardDao, times(1)).getAll();
    logger.info("Тест getAllCards_Exception завершён успешно");
  }

  @Test
  void getCardById_Success() {
    logger.info("Начало теста: getCardById_Success");
    when(cardDao.getById(1L)).thenReturn(Optional.of(testCard));
    Optional<Card> actualCard = cardService.getCardById(1L);
    assertTrue(actualCard.isPresent());
    assertEquals(testCard, actualCard.get());
    verify(cardDao, times(1)).getById(1L);
    logger.info("Тест getCardById_Success завершён успешно");
  }

  @Test
  void getCardById_Exception() {
    logger.info("Начало теста: getCardById_Exception");
    doThrow(new RuntimeException("Simulated exception")).when(cardDao).getById(1L);
    assertThrows(RuntimeException.class, () -> cardService.getCardById(1L));
    verify(cardDao, times(1)).getById(1L);
    logger.info("Тест getCardById_Exception завершён успешно");
  }

  @Test
  void calculateLuhnChecksum_Success() {
    logger.info("Начало теста: calculateLuhnChecksum_Success");
    String number = "79927398713";
    int checksum = CardService.calculateLuhnChecksum(number);
    assertEquals(8, checksum);
    logger.info("Тест calculateLuhnChecksum_Success завершён успешно");
  }
}
