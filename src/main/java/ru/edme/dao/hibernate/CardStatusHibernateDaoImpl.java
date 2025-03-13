package ru.edme.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dao.Dao;
import ru.edme.model.CardStatus;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Repository
public class CardStatusHibernateDaoImpl implements Dao<CardStatus> {

    @PersistenceContext
    private EntityManager entityManager; // Теперь Spring сам управляет EntityManager

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS card_status (
                id BIGSERIAL PRIMARY KEY,
                card_status_name VARCHAR(255) UNIQUE NOT NULL
            );
            """;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS card_status CASCADE";

    @Override
    public void createTable() {
        log.info("Creating table 'CardStatus'...");
        try {
            Query nativeQuery = entityManager.createNativeQuery(CREATE_TABLE);
            nativeQuery.executeUpdate();
            log.info("Created table 'CardStatus'!");
        } catch (Exception e) {
            log.error("Error creating table 'CardStatus'", e);
            throw new RuntimeException("Error creating table 'CardStatus'", e);
        }
    }

    @Override
    public void dropTable() {
        log.info("Dropping table 'CardStatus'...");
        try {
            Query nativeQuery = entityManager.createNativeQuery(DROP_TABLE);
            nativeQuery.executeUpdate();
            log.info("Dropped table 'CardStatus'!");
        } catch (Exception e) {
            log.error("Error dropping table 'CardStatus'", e);
            throw new RuntimeException("Error dropping table 'CardStatus'", e);
        }
    }

    @Override
    public void clearTable() {
        log.info("Clearing all data from table...");
        try {
            entityManager.createQuery("DELETE FROM CardStatus").executeUpdate();
            log.info("CardStatuses have been deleted.");
        } catch (Exception e) {
            log.error("Error clearing CardStatus", e);
            throw new RuntimeException("Error clearing CardStatus", e);
        }
    }

    @Override
    public void insert(CardStatus cardStatus) {
        entityManager.persist(cardStatus);
        log.info("CardStatus inserted: {}", cardStatus);
    }

    @Override
    public void delete(Long id) {
        CardStatus cardStatus = entityManager.find(CardStatus.class, id);
        if (cardStatus != null) {
            entityManager.remove(cardStatus);
            log.info("CardStatus with id {} deleted", id);
        } else {
            log.error("CardStatus with id {} not found", id);
        }
    }

    @Override
    public List<CardStatus> getAll() {
        return entityManager.createQuery("FROM CardStatus", CardStatus.class).getResultList();
    }

    @Override
    public Optional<CardStatus> getById(Long id) {
        return Optional.ofNullable(entityManager.find(CardStatus.class, id));
    }

    @Override
    public void update(CardStatus cardStatus) {
        entityManager.merge(cardStatus);
        log.info("CardStatus updated: {}", cardStatus);
    }
}
