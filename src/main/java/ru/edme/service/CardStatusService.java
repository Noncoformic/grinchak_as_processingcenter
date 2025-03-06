package ru.edme.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.dao.Dao;
import ru.edme.model.CardStatus;

import java.util.List;
import java.util.Optional;

public class CardStatusService {
    private static final Logger logger = LogManager.getLogger(CardStatusService.class);
    private final Dao<CardStatus> cardStatusDao;

    public CardStatusService(Dao<CardStatus> cardStatusDao) {
        this.cardStatusDao = cardStatusDao;
    }

    public void createTable() {
        try {
            cardStatusDao.createTable();
        } catch (RuntimeException e) {
            logger.error("Error in createTable: " + e.getMessage());
            throw new RuntimeException("Error in createTable: " + e.getMessage(), e);
        }
    }

    public void dropTable() {
        try {
            cardStatusDao.dropTable();
        } catch (RuntimeException e) {
            logger.error("Error in dropTable: " + e.getMessage());
            throw new RuntimeException("Error in dropTable: " + e.getMessage(), e);
        }
    }

    public void clearTable() {
        try {
            cardStatusDao.clearTable();
        } catch (RuntimeException e) {
            logger.error("Error in clearTable: " + e.getMessage());
            throw new RuntimeException("Error in clearTable: " + e.getMessage(), e);
        }
    }

    public CardStatus getCardStatusByName(String cardStatusName) {
        try {
            Optional<CardStatus> existingStatus = cardStatusDao.getAll().stream().filter(status -> status.getCardStatusName().equals(cardStatusName)).findFirst();
            if (existingStatus.isPresent()) {
                return existingStatus.get();
            }

            CardStatus cardStatus = CardStatus.builder().cardStatusName(cardStatusName).build();
            cardStatusDao.insert(cardStatus);
            logger.info("✅ CardStatus added: {}", cardStatus);
            return cardStatus;
        } catch (RuntimeException e) {
            logger.error("Error in addCardStatus: " + e.getMessage());
            throw new RuntimeException("Error in addCardStatus: " + e.getMessage(), e);
        }
    }

    public List<CardStatus> getAllCardStatuses() {
        try {
            return cardStatusDao.getAll();
        } catch (RuntimeException e) {
            logger.error("Error in getAllCardStatuses: " + e.getMessage());
            throw new RuntimeException("Error in getAllCardStatuses: " + e.getMessage(), e);
        }
    }

    public Optional<CardStatus> getCardStatusById(Long id) {
        try {
            return cardStatusDao.getById(id);
        } catch (RuntimeException e) {
            logger.error("Error in getCardStatusById: " + e.getMessage());
            throw new RuntimeException("Error in getCardStatusById: " + e.getMessage(), e);
        }
    }

    public void deleteCardStatus(Long id) {
        try {
            cardStatusDao.delete(id);
            logger.info("❌ CardStatus deleted: " + id);
        } catch (RuntimeException e) {
            logger.error("Error in deleteCardStatus: " + e.getMessage());
            throw new RuntimeException("Error in deleteCardStatus: " + e.getMessage(), e);
        }
    }

    public void updateCardStatus(Long id, String cardStatusName) {
        try {
            Optional<CardStatus> optionalCardStatus = cardStatusDao.getById(id);
            if (optionalCardStatus.isPresent()) {
                CardStatus cardStatus = optionalCardStatus.get();
                CardStatus updatedCardStatus = cardStatus.toBuilder()
                        .cardStatusName(cardStatusName).build();
                cardStatusDao.update(updatedCardStatus);
                logger.info("🔄 CardStatus updated: {} to {}", cardStatus.getCardStatusName(), cardStatusName);
            }
        } catch (RuntimeException e) {
            logger.error("Error in updateCardStatus: " + e.getMessage());
            throw new RuntimeException("Error in updateCardStatus: " + e.getMessage(), e);
        }
    }
}