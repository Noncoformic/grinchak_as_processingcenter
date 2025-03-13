package ru.edme.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dao.Dao;
import ru.edme.model.Currency;

import java.util.List;
import java.util.Optional;
@Slf4j
@Transactional
@Repository
public class CurrencyHibernateDaoImpl implements Dao<Currency> {

    @PersistenceContext
    private EntityManager entityManager;


    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS currency (
                id BIGSERIAL PRIMARY KEY,
                currency_digital_code VARCHAR(3) UNIQUE NOT NULL,
                currency_letter_code VARCHAR(3) UNIQUE NOT NULL,
                currency_name VARCHAR(255) NOT NULL
            );
            """;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS currency CASCADE";


    @Override
    public void createTable() {
        log.info("Creating table 'Currency'...");
        try {
            if (entityManager == null) {
                throw new IllegalStateException("EntityManager не был инициализирован");
            }
            entityManager.createNativeQuery(CREATE_TABLE)
                    .executeUpdate();
            log.info("Created table 'Currency'!");
        } catch (Exception e) {
            log.error("Error creating table 'Currency'", e);
            throw new RuntimeException("Error creating table 'Currency'", e);
        }
    }

    @Override
    public void dropTable() {
        log.info("Dropping table 'Currency'...");
        try {

           entityManager.createNativeQuery(DROP_TABLE);

            log.info("Dropped table 'Currency'!");
        } catch (Exception e) {
            log.error("Error dropping table 'Currency'", e);
            throw new RuntimeException("Error dropping table 'Currency'", e);
        }
    }

    @Override
    public void clearTable() {
        log.info("Clearing all data from table");
        try  {
            entityManager.createQuery("delete from Currency").executeUpdate();
            log.info("Currencies have been deleted.");
        } catch (Exception e) {
            log.error("Error clearing Currencies", e);
            throw new RuntimeException("Error clearing Currencies", e);
        }
    }

    @Override
    public void insert(Currency currency) {
        try{
            entityManager.persist(currency);
            log.info("Currency inserted: {}", currency);
        } catch (Exception e) {
            log.error("Error inserting Currency", e);
            throw new RuntimeException("Error inserting Currency", e);
        }
    }

    @Override
    public void delete(Long id) {
        try{

            Currency currency = entityManager.find(Currency.class, id);
            if (currency != null) {
                entityManager.remove(currency);
                log.info("Currency with id {} deleted", id);
            } else {
                log.error("Currency with id {} not found", id);
            }
        } catch (Exception e) {
            log.error("Error deleting Currency with id {}", id, e);
            throw new RuntimeException("Error deleting Currency", e);
        }
    }

    @Override
    public List<Currency> getAll() {
        try  {
            return entityManager.createQuery("from Currency", Currency.class).getResultList();
        } catch (Exception e) {
            log.error("Error fetching all Currencies", e);
            throw new RuntimeException("Error fetching all Currencies", e);
        }
    }

    @Override
    public Optional<Currency> getById(Long id) {
        try {
            return Optional.ofNullable(entityManager.find(Currency.class, id));
        } catch (Exception e) {
            log.error("Error getting Currency by ID", e);
            throw new RuntimeException("Error getting Currency by ID", e);
        }
    }

    @Override
    public void update(Currency currency) {
        try {
            entityManager.merge(currency);
            log.info("Currency updated: {}", currency);
        } catch (Exception e) {
            log.error("Error updating Currency", e);
            throw new RuntimeException("Error updating Currency", e);
        }
    }
}