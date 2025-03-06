package ru.edme.dao.hibernate;

import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.edme.configuration.HibernateConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Currency;

import java.util.List;
import java.util.Optional;

public class CurrencyHibernateDaoImpl implements Dao<Currency> {
    private static final Logger logger = LogManager.getLogger(CurrencyHibernateDaoImpl.class);
    private static final CurrencyHibernateDaoImpl INSTANCE = new CurrencyHibernateDaoImpl();
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS currency (
                id BIGSERIAL PRIMARY KEY,
                currency_digital_code VARCHAR(3) UNIQUE NOT NULL,
                currency_letter_code VARCHAR(3) UNIQUE NOT NULL,
                currency_name VARCHAR(255) NOT NULL
            );
            """;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS currency CASCADE";

    public static CurrencyHibernateDaoImpl getInstance() {
        return INSTANCE;
    }

    public CurrencyHibernateDaoImpl() {
    }

    @Override
    public void createTable() {
        logger.info("Creating table 'Currency'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(CREATE_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Created table 'Currency'!");
        } catch (Exception e) {
            logger.error("Error creating table 'Currency'", e);
            throw new RuntimeException("Error creating table 'Currency'", e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping table 'Currency'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(DROP_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Dropped table 'Currency'!");
        } catch (Exception e) {
            logger.error("Error dropping table 'Currency'", e);
            throw new RuntimeException("Error dropping table 'Currency'", e);
        }
    }

    @Override
    public void clearTable() {
        logger.info("Clearing all data from table");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Currency").executeUpdate();
            transaction.commit();
            logger.info("Currencies have been deleted.");
        } catch (Exception e) {
            logger.error("Error clearing Currencies", e);
            throw new RuntimeException("Error clearing Currencies", e);
        }
    }

    @Override
    public void insert(Currency currency) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(currency);
            transaction.commit();
            logger.info("Currency inserted: {}", currency);
        } catch (Exception e) {
            logger.error("Error inserting Currency", e);
            throw new RuntimeException("Error inserting Currency", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Currency currency = session.get(Currency.class, id);
            if (currency != null) {
                session.remove(currency);
                transaction.commit();
                logger.info("Currency with id {} deleted", id);
            } else {
                logger.error("Currency with id {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting Currency with id {}", id, e);
            throw new RuntimeException("Error deleting Currency", e);
        }
    }

    @Override
    public List<Currency> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Currency", Currency.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all Currencies", e);
            throw new RuntimeException("Error fetching all Currencies", e);
        }
    }

    @Override
    public Optional<Currency> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Currency.class, id));
        } catch (Exception e) {
            logger.error("Error getting Currency by ID", e);
            throw new RuntimeException("Error getting Currency by ID", e);
        }
    }

    @Override
    public void update(Currency currency) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(currency);
            transaction.commit();
            logger.info("Currency updated: {}", currency);
        } catch (Exception e) {
            logger.error("Error updating Currency", e);
            throw new RuntimeException("Error updating Currency", e);
        }
    }
}