package ru.edme.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional
public class CardHibernateDaoImpl implements Dao<Card> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void createTable() {

    }

    @Override
    public void dropTable() {

    }

    @Override
    public void clearTable() {

    }

    @Override
    public void insert(Card card) {
        try {
            entityManager.persist(card);
            log.info("✅ Card inserted: {}", card);
        } catch (Exception e) {
            log.error("❌ Error inserting Card", e);
            throw new RuntimeException("Error inserting Card", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Card card = entityManager.find(Card.class, id);
            if (card != null) {
                entityManager.remove(card);
                log.info("✅ Card deleted: {}", id);
            } else {
                log.warn("⚠️ Card with id {} not found", id);
            }
        } catch (Exception e) {
            log.error("❌ Error deleting Card with id {}", id, e);
            throw new RuntimeException("Error deleting Card", e);
        }
    }

    @Override
    public List<Card> getAll() {
        try {
            return entityManager.createQuery("SELECT c FROM Card c", Card.class).getResultList();
        } catch (Exception e) {
            log.error("❌ Error fetching all Cards", e);
            throw new RuntimeException("Error fetching all Cards", e);
        }
    }

    @Override
    public Optional<Card> getById(Long id) {
        try {
            return Optional.ofNullable(entityManager.find(Card.class, id));
        } catch (Exception e) {
            log.error("❌ Error getting Card by ID", e);
            throw new RuntimeException("Error getting Card by ID", e);
        }
    }

    @Override
    public void update(Card card) {
        try {
            entityManager.merge(card);
            log.info("✅ Card updated: {}", card);
        } catch (Exception e) {
            log.error("❌ Error updating Card", e);
            throw new RuntimeException("Error updating Card", e);
        }
    }
}
