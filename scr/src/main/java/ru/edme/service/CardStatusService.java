package ru.edme.service;

import ru.edme.dao.jdbc.CardStatusJDBCDaoImpl;
import ru.edme.model.CardStatus;

import java.util.List;

public class CardStatusService {
    private final CardStatusJDBCDaoImpl cardStatusDao;

    public CardStatusService() {
        this.cardStatusDao = new CardStatusJDBCDaoImpl();
    }

    public void createTable() {
        cardStatusDao.createTable();
    }
    public void clearTable() {
        cardStatusDao.clearTable();
    }

    public void addCardStatus(String cardStatusName) {
        CardStatus status = new CardStatus(null, cardStatusName);
        cardStatusDao.insert(status);
        System.out.println("✅ CardStatus added: " + cardStatusName);
    }

    public List<CardStatus> getAllCardStatuses() {
        return cardStatusDao.getAll();
    }

    public CardStatus getCardStatusById(Long id) {
        return cardStatusDao.getById(id);
    }

    public void updateCardStatus(Long id, String newStatusName) {
        CardStatus status = cardStatusDao.getById(id);
        if (status == null) {
            System.out.println("⚠️ CardStatus not found!");
            return;
        }
        status.setCardStatusName(newStatusName);
        cardStatusDao.update(status);
        System.out.println("✅ CardStatus updated: " + status);
    }

    public void deleteCardStatus(Long id) {
        cardStatusDao.delete(id);
        System.out.println("❌ CardStatus deleted: " + id);
    }

}
