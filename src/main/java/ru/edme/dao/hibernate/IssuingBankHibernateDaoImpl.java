package ru.edme.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dao.Dao;
import ru.edme.model.IssuingBank;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Repository
public class IssuingBankHibernateDaoImpl implements Dao<IssuingBank> {

    @PersistenceContext
    private EntityManager entityManager; // Теперь Spring сам управляет EntityManager

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS issuing_bank (
                id BIGSERIAL PRIMARY KEY,
                bic VARCHAR(9) UNIQUE NOT NULL,
                abbreviated_name VARCHAR(255) NOT NULL
            );
            """;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS issuing_bank CASCADE";

    @Override
    public void createTable() {
        log.info("Creating table 'IssuingBank'...");
        try {
            Query nativeQuery = entityManager.createNativeQuery(CREATE_TABLE);
            nativeQuery.executeUpdate();
            log.info("Created table 'IssuingBank'!");
        } catch (Exception e) {
            log.error("Error creating table 'IssuingBank'", e);
            throw new RuntimeException("Error creating table 'IssuingBank'", e);
        }
    }

    @Override
    public void dropTable() {
        log.info("Dropping table 'IssuingBank'...");
        try {
            Query nativeQuery = entityManager.createNativeQuery(DROP_TABLE);
            nativeQuery.executeUpdate();
            log.info("Dropped table 'IssuingBank'!");
        } catch (Exception e) {
            log.error("Error dropping table 'IssuingBank'", e);
            throw new RuntimeException("Error dropping table 'IssuingBank'", e);
        }
    }

    @Override
    public void clearTable() {
        log.info("Clearing all data from table...");
        try {
            entityManager.createQuery("DELETE FROM IssuingBank").executeUpdate();
            log.info("IssuingBanks have been deleted.");
        } catch (Exception e) {
            log.error("Error clearing IssuingBank", e);
            throw new RuntimeException("Error clearing IssuingBank", e);
        }
    }

    @Override
    public void insert(IssuingBank issuingBank) {
        entityManager.persist(issuingBank);
        log.info("IssuingBank inserted: {}", issuingBank);
    }

    @Override
    public void delete(Long id) {
        IssuingBank issuingBank = entityManager.find(IssuingBank.class, id);
        if (issuingBank != null) {
            entityManager.remove(issuingBank);
            log.info("IssuingBank with id {} deleted", id);
        } else {
            log.error("IssuingBank with id {} not found", id);
        }
    }

    @Override
    public List<IssuingBank> getAll() {
        return entityManager.createQuery("FROM IssuingBank", IssuingBank.class).getResultList();
    }

    @Override
    public Optional<IssuingBank> getById(Long id) {
        return Optional.ofNullable(entityManager.find(IssuingBank.class, id));
    }

    @Override
    public void update(IssuingBank issuingBank) {
        entityManager.merge(issuingBank);
        log.info("IssuingBank updated: {}", issuingBank);
    }
}
