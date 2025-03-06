package ru.edme.dao.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.edme.configuration.JDBCConfig;
import ru.edme.dao.Dao;
import ru.edme.model.Account;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AccountJDBCDaoImpl implements Dao<Account> {
    private static final Logger logger = LogManager.getLogger(AccountJDBCDaoImpl.class);
    private static final String CREATE_TABLE = """
        CREATE TABLE IF NOT EXISTS account (
            id SERIAL PRIMARY KEY,
            account_number VARCHAR(20) UNIQUE NOT NULL,
            balance NUMERIC(19, 2) NOT NULL,
            currency_id BIGINT REFERENCES currency(id),
            issuing_bank_id BIGINT REFERENCES issuing_bank(id)
        );
        """;
    private static final String INSERT =
            "INSERT INTO account (account_number, balance, currency_id, issuing_bank_id) VALUES (?, ?, ?, ?)";
    private static final String GET_ALL = "SELECT * FROM account";
    private static final String GET_BY_ID = "SELECT * FROM account WHERE id = ?";
    private static final String UPDATE =
            "UPDATE account SET account_number = ?, balance = ?, currency_id = ?, issuing_bank_id = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM account WHERE id = ?";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS account CASCADE";
    private static final String CLEAR_TABLE = "DELETE FROM account;";
    private static final AccountJDBCDaoImpl INSTANCE = new AccountJDBCDaoImpl();
    public static AccountJDBCDaoImpl getInstance() {
        return INSTANCE;
    }

    public AccountJDBCDaoImpl() {
    }

    @Override
    public void createTable() {
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CREATE_TABLE);
            logger.info("Created Account table successfully");
        } catch (SQLException e) {
            logger.error("Error creating Account table", e);
            throw new RuntimeException("Error creating Account table", e);
        }
    }

    @Override
    public void dropTable() {
        logger.info("Dropping Account table....");
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(DROP_TABLE);
            logger.info("Dropped Account table successfully");
        } catch (SQLException e) {
            logger.error("Error dropping Account table", e);
            throw new RuntimeException("Error dropping Account table", e);
        }
    }

    @Override
    public void clearTable() {
        logger.info("Clearing all data from Account table");
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(CLEAR_TABLE);
            logger.info("Accounts have been cleared");
        } catch (SQLException e) {
            logger.error("Error clearing Account table", e);
            throw new RuntimeException("Error clearing Account table", e);
        }
    }

    @Override
    public void insert(Account account) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, account.getAccountNumber());
            pstmt.setBigDecimal(2, account.getBalance());
            pstmt.setLong(3, account.getCurrencyId());
            pstmt.setLong(4, account.getIssuingBankId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        account.setId(rs.getLong(1));
                        logger.info("Inserted new Account: " + account.getId());
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Error inserting Account", e);
            throw new RuntimeException("Error inserting Account", e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(DELETE)) {
            pstmt.setLong(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting Account with id {}", id, e);
            throw new RuntimeException("Error deleting Account", e);
        }
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = JDBCConfig.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL)) {

            while (rs.next()) {
                Account account = mapResultSetToAccount(rs);
                accounts.add(account);
            }
        } catch (SQLException e) {
            logger.error("Error fetching all Accounts", e);
            throw new RuntimeException("Error fetching all Accounts", e);
        }
        return accounts;
    }

    @Override
    public Optional<Account> getById(Long id) {
        try (Connection connection = JDBCConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAccount(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting Account by ID", e);
            throw new RuntimeException("Error getting Account by ID", e);
        }
        return Optional.empty();
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
            logger.info("Updated Account: " + account.getId());
        } catch (SQLException e) {
            logger.error("Error updating Account", e);
            throw new RuntimeException("Error updating Account", e);
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getLong("id"));
        account.setAccountNumber(rs.getString("account_number"));
        account.setBalance(rs.getBigDecimal("balance"));
        account.setCurrencyId(rs.getLong("currency_id"));
        account.setIssuingBankId(rs.getLong("issuing_bank_id"));
        return account;
    }
}

