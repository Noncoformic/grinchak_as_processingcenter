package ru.edme.dao.hibernate;

import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.edme.configuration.HibernateConfig;
import ru.edme.dao.Dao;
import ru.edme.model.IssuingBank;

import java.util.List;
import java.util.Optional;

public class IssuingBankHibernateDaoImpl implements Dao<IssuingBank> {
    private static final Logger logger = LogManager.getLogger(IssuingBankHibernateDaoImpl.class);
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
        logger.info("Creating table 'IssuingBank'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(CREATE_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Created table 'IssuingBank'!");
        } catch (Exception e) {
            logger.error("Error creating table 'IssuingBank'", e);
            throw new RuntimeException("Error creating table 'IssuingBank'", e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping table 'IssuingBank'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(DROP_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Dropped table 'IssuingBank'!");
        } catch (Exception e) {
            logger.error("Error dropping table 'IssuingBank'", e);
            throw new RuntimeException("Error dropping table 'IssuingBank'", e);
        }
    }

    @Override
    public void clearTable() {
        logger.info("Clearing all data from table");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from IssuingBank").executeUpdate();
            transaction.commit();
            logger.info("IssuingBanks have been deleted.");
        } catch (Exception e) {
            logger.error("Error clearing IssuingBank", e);
            throw new RuntimeException("Error clearing IssuingBank", e);
        }
    }

    @Override
    public void insert(IssuingBank issuingBank) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(issuingBank);
            transaction.commit();
            logger.info("IssuingBank inserted: {}", issuingBank);
        } catch (Exception e) {
            logger.error("Error inserting IssuingBank", e);
            throw new RuntimeException("Error inserting IssuingBank", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            IssuingBank issuingBank = session.get(IssuingBank.class, id);
            if (issuingBank != null) {
                session.remove(issuingBank);
                transaction.commit();
                logger.info("IssuingBank with id {} deleted", id);
            } else {
                logger.error("IssuingBank with id {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting IssuingBank with id {}", id, e);
            throw new RuntimeException("Error deleting IssuingBank", e);
        }
    }

    @Override
    public List<IssuingBank> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from IssuingBank", IssuingBank.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all IssuingBanks", e);
            throw new RuntimeException("Error fetching all IssuingBanks", e);
        }
    }

    @Override
    public Optional<IssuingBank> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(IssuingBank.class, id));
        } catch (Exception e) {
            logger.error("Error getting IssuingBank by ID", e);
            throw new RuntimeException("Error getting IssuingBank by ID", e);
        }
    }

    @Override
    public void update(IssuingBank issuingBank) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(issuingBank);
            transaction.commit();
            logger.info("IssuingBank updated: {}", issuingBank);
        } catch (Exception e) {
            logger.error("Error updating IssuingBank", e);
            throw new RuntimeException("Error updating IssuingBank", e);
        }
    }
}