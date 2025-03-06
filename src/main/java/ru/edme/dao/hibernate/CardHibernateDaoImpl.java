package ru.edme.dao.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.edme.configuration.HibernateConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Card;

import java.util.List;
import java.util.Optional;

public class CardHibernateDaoImpl implements Dao<Card> {
    private static final Logger logger = LogManager.getLogger(CardHibernateDaoImpl.class);
    private static final CardHibernateDaoImpl INSTANCE = new CardHibernateDaoImpl();

    public static CardHibernateDaoImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public void createTable() {
        logger.info("createTable() Card.");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("""
                    CREATE TABLE IF NOT EXISTS card (
                    id BIGSERIAL PRIMARY KEY,
                    card_number VARCHAR(50) UNIQUE NOT NULL,
                    expiration_date DATE NOT NULL,
                    holder_name VARCHAR(50) NOT NULL,
                    card_status_id BIGINT NOT NULL,
                    payment_system_id BIGINT NOT NULL,
                    account_id BIGINT NOT NULL,
                    received_from_issuing_bank TIMESTAMP,
                    sent_to_issuing_bank TIMESTAMP
                    )
                                """).executeUpdate();
            transaction.commit();
            logger.info("Card created.");
        } catch (Exception e) {
            logger.error("Error in createTable: " + e.getMessage());
            throw new RuntimeException("Error in createTable: " + e.getMessage(), e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping Card table....");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery("DROP TABLE IF EXISTS card CASCADE").executeUpdate();
            transaction.commit();
            logger.info("Dropped Card table successfully");
        } catch (Exception e) {
            logger.error("Error dropping table", e);
            throw new RuntimeException("Error dropping table", e);
        }
    }

    @Override
    public void clearTable() {
        logger.info("Clearing all data from table");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Card").executeUpdate();
            transaction.commit();
            logger.info("Cards have been deleted.");
        } catch (Exception e) {
            logger.error("Error clearing Card", e);
            throw new RuntimeException("Error clearing Card", e);
        }
    }

    @Override
    public void insert(Card card) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(card);
            transaction.commit();
            logger.info("Card inserted: {}", card);
        } catch (Exception e) {
            logger.error("Error inserting Card", e);
            throw new RuntimeException("Error inserting Card", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Card card = session.get(Card.class, id);
            if (card != null) {
                session.remove(card);
                transaction.commit();
                logger.info("Card with id {} deleted", id);
            } else {
                logger.error("Card with id {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting Card with id {}", id, e);
            throw new RuntimeException("Error deleting Card", e);
        }
    }

    @Override
    public List<Card> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Card", Card.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all Cards", e);
            throw new RuntimeException("Error fetching all Cards", e);
        }
    }

    @Override
    public Optional<Card> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Card.class, id));
        } catch (Exception e) {
            logger.error("Error getting Card by ID", e);
            throw new RuntimeException("Error getting Card by ID", e);
        }
    }

    @Override
    public void update(Card card) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(card);
            transaction.commit();
            logger.info("Card updated: {}", card);
        } catch (Exception e) {
            logger.error("Error updating Card", e);
            throw new RuntimeException("Error updating Card", e);
        }
    }
}