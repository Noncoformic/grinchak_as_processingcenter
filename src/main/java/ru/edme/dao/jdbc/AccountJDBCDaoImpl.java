package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AccountJDBCDaoImpl implements Dao<Account> {

    private static final Logger logger = LogManager.getLogger(AccountJDBCDaoImpl.class);

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS account (
                id SERIAL PRIMARY KEY,
                account_number VARCHAR(50) UNIQUE NOT NULL,
                balance DECIMAL(15,2) NOT NULL,
                currency_id BIGINT NOT NULL REFERENCES currency(id),
                issuing_bank_id BIGINT NOT NULL REFERENCES issuing_bank(id)
            );
            """;
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS account CASCADE;";
    private static final String CLEAR_TABLE = "TRUNCATE TABLE account RESTART IDENTITY CASCADE;";
    private static final String INSERT = """
            INSERT INTO account (account_number, balance, currency_id, issuing_bank_id)
            VALUES (?, ?, ?, ?);
            """;

    private static final String GET_ALL = "SELECT * FROM account;";
    private static final String GET_BY_ID = "SELECT * FROM account WHERE id = ?;";
    private static final String UPDATE = """
            UPDATE account SET account_number = ?, balance = ?, currency_id = ?, issuing_bank_id = ? WHERE id = ?;
            """;
    private static final String DELETE = "DELETE FROM account WHERE id = ?;";
    private static final String CHECK_EXISTENCE = """
        SELECT COUNT(*) FROM account WHERE account_number = ?;
        """;

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating Account table", e);
        }
    }

    @Override
    public void dropTable() {
        try(Connection connection =JDBCConfig.getConnection();
        Statement  stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
        }catch (SQLException e){
            throw new RuntimeException("Error dropping Account table", e);
        }

    }

    @Override
    public void clearTable() {
        try(Connection connection = JDBCConfig.getConnection();
        Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);

        }catch (SQLException e){
            throw new RuntimeException("Error clearing Account table");
        }

    }

    @Override
    public void insert(Account account) {
        try (Connection connection = JDBCConfig.getConnection()) {
            try (PreparedStatement checkStmt = connection.prepareStatement(CHECK_EXISTENCE)) {
                logger.info("Inserting account with account_number: " + account.getAccountNumber());
                logger.info("Checking existence of account " + account.getAccountNumber());
                checkStmt.setString(1, account.getAccountNumber());
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    logger.info("⚠️ Account '" + account.getAccountNumber() + "' already exists. Skipping insert.");
                    return;
                }
                logger.info("Inserting account Done");
            }

            try (PreparedStatement pstmt = connection.prepareStatement(INSERT)) {
                pstmt.setString(1, account.getAccountNumber());
                pstmt.setBigDecimal(2, account.getBalance());
                pstmt.setLong(3, account.getCurrencyId());
                pstmt.setLong(4, account.getIssuingBankId());
                pstmt.executeUpdate();
                System.out.println("✅ Account added: " + account.getAccountNumber());
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error inserting into Account table", e);
        }
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                accounts.add(new Account(
                        rs.getLong("id"),
                        rs.getString("account_number"),
                        rs.getBigDecimal("balance"),
                        rs.getLong("currency_id"),
                        rs.getLong("issuing_bank_id")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching all accounts", e);
        }
        return accounts;
    }

    @Override
    public Account getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Account(
                        rs.getLong("id"),
                        rs.getString("account_number"),
                        rs.getBigDecimal("balance"),
                        rs.getLong("currency_id"),
                        rs.getLong("issuing_bank_id")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching account by ID", e);
        }
        return null;
    }

    @Override
    public void update(Account account) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(UPDATE)) {
            pstmt.setString(1, account.getAccountNumber());
            pstmt.setBigDecimal(2, account.getBalance());
            pstmt.setLong(3, account.getCurrencyId());
            pstmt.setLong(4, account.getIssuingBankId());
            pstmt.setLong(5, account.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Account", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting account", e);
        }
    }
}

