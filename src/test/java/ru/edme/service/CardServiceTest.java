//package ru.edme.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mockito;
//import ru.edme.dao.Dao;
//import ru.edme.model.Account;
//import ru.edme.model.Card;
//import ru.edme.model.CardStatus;
//import ru.edme.model.PaymentSystem;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//class CardServiceTest {
//
//    private Dao<Card> cardDaoMock;
//    private CardService cardService;
//    private CardBuilderService cardBuilderService = new CardBuilderService();
//
//    @BeforeEach
//    void setUp() {
//        // Создаём мок DAO
//        cardDaoMock = Mockito.mock(Dao.class);
//        // Создаём сервис, передавая ему мок
//        cardService = new CardService(cardDaoMock,cardBuilderService);
//    }
//
//    @Test
//    void testAddCard_Success() {
//        // Подготовка данных
//        LocalDate expirationDate = LocalDate.of(2030, 5, 11);
//        String holderName = "John Chikin";
//        CardStatus status = new CardStatus(1L, "Active");
//        PaymentSystem paymentSystem = new PaymentSystem(1L, "Visa");
//        Account account = new Account(1L, null, null, null, null);
//
//        // Вызываем метод
//        cardService.addCard(expirationDate, holderName, status, paymentSystem, account, null, null);
//
//        // Проверяем, что метод insert у DAO вызвался 1 раз
//        ArgumentCaptor<Card> captor = ArgumentCaptor.forClass(Card.class);
//        verify(cardDaoMock, times(1)).insert(captor.capture());
//
//        // Проверяем значения вставленной карты
//        Card capturedCard = captor.getValue();
//        assertEquals(holderName, capturedCard.getHolderName());
//        assertEquals(expirationDate, capturedCard.getExpirationDate());
//        assertEquals(status, capturedCard.getCardStatus());
//    }
//
//    @Test
//    void testGetAllCard_Success() {
//        // Подготовка тестовых данных
//        LocalDate expirationDate = LocalDate.of(2030, 5, 11);
//        String holderName = "John Chikin";
//        CardStatus status = new CardStatus(1L, "Active");
//        PaymentSystem paymentSystem = new PaymentSystem(1L, "Visa");
//        Account account = new Account(1L, null, null, null, null);
//
//        List<Card> testCards = List.of(
//                new Card(1L, "1234567890123456", expirationDate, holderName, status, paymentSystem, account, null, null),
//                new Card(2L, "9876543210987654", expirationDate, "Jane Doe", status, paymentSystem, account, null, null)
//        );
//
//        // Настраиваем мок: когда вызывается getAll(), возвращаем тестовые данные
//        when(cardDaoMock.getAll()).thenReturn(testCards);
//
//        // Вызываем тестируемый метод
//        List<Card> result = cardService.getAllCards();
//
//        // Проверяем, что метод getAll() у DAO вызвался 1 раз
//        verify(cardDaoMock, times(1)).getAll();
//
//        // Проверяем, что список карт не пуст
//        assertFalse(result.isEmpty());
//        assertEquals(2, result.size());
//        assertEquals("John Chikin", result.get(0).getHolderName());
//        assertEquals("Jane Doe", result.get(1).getHolderName());
//    }
//
//}

