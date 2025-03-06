package ru.edme.dao.hibernate;

import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.edme.configuration.HibernateConfig;
import ru.edme.dao.Dao;
import ru.edme.model.CardStatus;

import java.util.List;
import java.util.Optional;

public class CardStatusHibernateDaoImpl implements Dao<CardStatus> {
    private static final Logger logger = LogManager.getLogger(CardStatusHibernateDaoImpl.class);
    private static final CardStatusHibernateDaoImpl INSTANCE = new CardStatusHibernateDaoImpl();
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS card_status (
                id BIGSERIAL PRIMARY KEY,
                card_status_name VARCHAR(255) UNIQUE NOT NULL
            );
            """;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS card_status CASCADE";

    public static CardStatusHibernateDaoImpl getInstance() {
        return INSTANCE;
    }

    public CardStatusHibernateDaoImpl() {
    }

    @Override
    public void createTable() {
        logger.info("Creating table 'CardStatus'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(CREATE_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Created table 'CardStatus'!");
        } catch (Exception e) {
            logger.error("Error creating table 'CardStatus'", e);
            throw new RuntimeException("Error creating table 'CardStatus'", e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping table 'CardStatus'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(DROP_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Dropped table 'CardStatus'!");
        } catch (Exception e) {
            logger.error("Error dropping table 'CardStatus'", e);
            throw new RuntimeException("Error dropping table 'CardStatus'", e);
        }
    }

    @Override
    public void clearTable() {
        logger.info("Clearing all data from table");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from CardStatus").executeUpdate();
            transaction.commit();
            logger.info("CardStatuses have been deleted.");
        } catch (Exception e) {
            logger.error("Error clearing CardStatus", e);
            throw new RuntimeException("Error clearing CardStatus", e);
        }
    }

    @Override
    public void insert(CardStatus cardStatus) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(cardStatus);
            transaction.commit();
            logger.info("CardStatus inserted: {}", cardStatus);
        } catch (Exception e) {
            logger.error("Error inserting CardStatus", e);
            throw new RuntimeException("Error inserting CardStatus", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            CardStatus cardStatus = session.get(CardStatus.class, id);
            if (cardStatus != null) {
                session.remove(cardStatus);
                transaction.commit();
                logger.info("CardStatus with id {} deleted", id);
            } else {
                logger.error("CardStatus with id {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting CardStatus with id {}", id, e);
            throw new RuntimeException("Error deleting CardStatus", e);
        }
    }

    @Override
    public List<CardStatus> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from CardStatus", CardStatus.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all CardStatuses", e);
            throw new RuntimeException("Error fetching all CardStatuses", e);
        }
    }

    @Override
    public Optional<CardStatus> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(CardStatus.class, id));
        } catch (Exception e) {
            logger.error("Error getting CardStatus by ID", e);
            throw new RuntimeException("Error getting CardStatus by ID", e);
        }
    }

    @Override
    public void update(CardStatus cardStatus) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(cardStatus);
            transaction.commit();
            logger.info("CardStatus updated: {}", cardStatus);
        } catch (Exception e) {
            logger.error("Error updating CardStatus", e);
            throw new RuntimeException("Error updating CardStatus", e);
        }
    }
}