package ru.edme.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.edme.dao.Dao;
import ru.edme.model.Account;

import java.util.List;
import java.util.Optional;
@Slf4j
@Repository
@Transactional
public class AccountHibernateDaoImpl implements Dao<Account> {

    @PersistenceContext
    private EntityManager entityManager;

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
        log.info("Creating table 'Account'...");
        try  {
         entityManager.createNativeQuery(CREATE_TABLE).executeUpdate();
            log.info("Created table 'Account'!");
        } catch (Exception e) {
            log.error("Error creating table 'Account'", e);
            throw new RuntimeException("Error creating table 'Account'", e);
        }
    }

    @Override
    public void dropTable() {
        log.info("Dropping table 'Account'...");
        try  {
            entityManager.createNativeQuery(DROP_TABLE).executeUpdate();
            log.info("Dropped table 'Account'!");
        } catch (Exception e) {
            log.error("Error dropping table 'Account'", e);
            throw new RuntimeException("Error dropping table 'Account'", e);
        }
    }
    @Override
    public void clearTable() {
        log.info("Clearing all data from table");
        try  {
            entityManager.createQuery("delete from Account").executeUpdate();
            log.info("Accounts have been deleted.");
        } catch (Exception e) {
            log.error("Error clearing Account", e);
            throw new RuntimeException("Error clearing Account", e);
        }
    }

    @Override
    public void insert(Account account) {
        try  {

            entityManager.persist(account);
            log.info("Account inserted: {}", account);
        } catch (Exception e) {
            log.error("Error inserting Account", e);
            throw new RuntimeException("Error inserting Account", e);
        }
    }

    @Override
    public void delete(Long id) {
        try  {

            Account account = entityManager.find(Account.class, id);
            if (account != null) {
                entityManager.remove(account);
                log.info("Account with id {} deleted", id);
            } else {
                log.error("Account with id {} not found", id);
            }
        } catch (Exception e) {
            log.error("Error deleting Account with id {}", id, e);
            throw new RuntimeException("Error deleting Account", e);
        }
    }

    @Override
    public List<Account> getAll() {
        try  {
            return entityManager.createQuery("from Account", Account.class).getResultList();
        } catch (Exception e) {
            log.error("Error fetching all Accounts", e);
            throw new RuntimeException("Error fetching all Accounts", e);
        }
    }

    @Override
    public Optional<Account> getById(Long id) {
            try  {
            return Optional.ofNullable(entityManager.find(Account.class, id));
        } catch (Exception e) {
            log.error("Error getting Account by ID", e);
            throw new RuntimeException("Error getting Account by ID", e);
        }
    }

    @Override
    public void update(Account account) {
        try  {
            entityManager.merge(account);
            log.info("Account updated: {}", account);
        } catch (Exception e) {
            log.error("Error updating Account", e);
            throw new RuntimeException("Error updating Account", e);
        }
    }
}