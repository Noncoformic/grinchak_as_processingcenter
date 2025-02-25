package ru.edme.service;

import ru.edme.dao.jdbc.CardJDBCDaoImpl;
import ru.edme.model.Card;

import java.util.Date;
import java.util.List;

public class CardService {
    private final CardJDBCDaoImpl cardDao;

    public CardService() {
        this.cardDao = new CardJDBCDaoImpl();
    }

    public void createTable() {
        cardDao.createTable();
    }

    public void clearTable() {
        cardDao.clearTable();
    }

    public void dropTable() {
        cardDao.dropTable();
    }

    public void addCard(String cardNumber, Date expirationDate, String holderName, Long cardStatusId, Long paymentSystemId, Long accountId) {
        Card card = new Card(null, cardNumber, expirationDate, holderName, cardStatusId, paymentSystemId, accountId);
        cardDao.insert(card);
        System.out.println("✅ Card added: " + cardNumber);
    }

    public List<Card> getAllCards() {
        return cardDao.getAll();
    }

    public Card getCardById(Long id) {
        return cardDao.getById(id);
    }

    public void deleteCard(Long id) {
        cardDao.delete(id);
        System.out.println("❌ Card deleted: " + id);
    }
    public  void updateCard(Card card) {
        cardDao.update(card);
    }
}
