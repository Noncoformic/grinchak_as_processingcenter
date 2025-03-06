package ru.edme.dao.hibernate;

import jakarta.persistence.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.edme.configuration.HibernateConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Account;

import java.util.List;
import java.util.Optional;

public class AccountHibernateDaoImpl implements Dao<Account> {
    private static final Logger logger = LogManager.getLogger(AccountHibernateDaoImpl.class);
    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS account (
                id BIGSERIAL PRIMARY KEY,
                account_number VARCHAR(20) UNIQUE NOT NULL,
                balance NUMERIC(19, 2) NOT NULL,
                currency_id BIGINT REFERENCES currency(id),
                issuing_bank_id BIGINT REFERENCES issuing_bank(id)
            );
            """;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS account CASCADE";
    @Override
    public void createTable() {
        logger.info("Creating table 'Account'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(CREATE_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Created table 'Account'!");
        } catch (Exception e) {
            logger.error("Error creating table 'Account'", e);
            throw new RuntimeException("Error creating table 'Account'", e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping table 'Account'...");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query nativeQuery = session.createNativeQuery(DROP_TABLE);
            nativeQuery.executeUpdate();
            transaction.commit();
            logger.info("Dropped table 'Account'!");
        } catch (Exception e) {
            logger.error("Error dropping table 'Account'", e);
            throw new RuntimeException("Error dropping table 'Account'", e);
        }
    }
    @Override
    public void clearTable() {
        logger.info("Clearing all data from table");
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Account").executeUpdate();
            transaction.commit();
            logger.info("Accounts have been deleted.");
        } catch (Exception e) {
            logger.error("Error clearing Account", e);
            throw new RuntimeException("Error clearing Account", e);
        }
    }

    @Override
    public void insert(Account account) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(account);
            transaction.commit();
            logger.info("Account inserted: {}", account);
        } catch (Exception e) {
            logger.error("Error inserting Account", e);
            throw new RuntimeException("Error inserting Account", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Account account = session.get(Account.class, id);
            if (account != null) {
                session.remove(account);
                transaction.commit();
                logger.info("Account with id {} deleted", id);
            } else {
                logger.error("Account with id {} not found", id);
            }
        } catch (Exception e) {
            logger.error("Error deleting Account with id {}", id, e);
            throw new RuntimeException("Error deleting Account", e);
        }
    }

    @Override
    public List<Account> getAll() {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return session.createQuery("from Account", Account.class).list();
        } catch (Exception e) {
            logger.error("Error fetching all Accounts", e);
            throw new RuntimeException("Error fetching all Accounts", e);
        }
    }

    @Override
    public Optional<Account> getById(Long id) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Account.class, id));
        } catch (Exception e) {
            logger.error("Error getting Account by ID", e);
            throw new RuntimeException("Error getting Account by ID", e);
        }
    }

    @Override
    public void update(Account account) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.merge(account);
            transaction.commit();
            logger.info("Account updated: {}", account);
        } catch (Exception e) {
            logger.error("Error updating Account", e);
            throw new RuntimeException("Error updating Account", e);
        }
    }
}