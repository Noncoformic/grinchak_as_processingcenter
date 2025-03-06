package ru.edme.dao;

import ru.edme.dao.hibernate.AccountHibernateDaoImpl;
import ru.edme.dao.hibernate.CardHibernateDaoImpl;
import ru.edme.dao.hibernate.CardStatusHibernateDaoImpl;
import ru.edme.dao.hibernate.CurrencyHibernateDaoImpl;
import ru.edme.dao.hibernate.IssuingBankHibernateDaoImpl;
import ru.edme.dao.jdbc.AccountJDBCDaoImpl;
import ru.edme.dao.jdbc.CardJDBCDaoImpl;
import ru.edme.dao.jdbc.CardStatusJDBCDaoImpl;
import ru.edme.dao.jdbc.CurrencyJDBCDaoImpl;
import ru.edme.dao.jdbc.IssuingBankJDBCDaoImpl;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.CardStatus;
import ru.edme.model.Currency;
import ru.edme.model.IssuingBank;

public class DaoFactory {
    private final DaoType daoType;

    public DaoFactory(DaoType daoType) {
        this.daoType = daoType;
    }

    public <T> Dao<T> getDao(Class<T> entityClass) {
        if (daoType == DaoType.JDBC) {
            if (entityClass == Card.class) {
                return (Dao<T>) new CardJDBCDaoImpl();
            }
            if (entityClass == Account.class) {
                return (Dao<T>) new AccountJDBCDaoImpl();
            }
            if (entityClass == CardStatus.class) {
                return (Dao<T>) new CardStatusJDBCDaoImpl();
            }
            if (entityClass == Currency.class) {
                return (Dao<T>) new CurrencyJDBCDaoImpl();
            }
            if (entityClass == IssuingBank.class) {
                return (Dao<T>) new IssuingBankJDBCDaoImpl();
            }
        } else if (daoType == DaoType.HIBERNATE) {
            if (entityClass == Card.class) {
                return (Dao<T>) new CardHibernateDaoImpl();
            }
            if (entityClass == Account.class) {
                return (Dao<T>) new AccountHibernateDaoImpl();
            }
            if (entityClass == CardStatus.class) {
                return (Dao<T>) new CardStatusHibernateDaoImpl();
            }
            if (entityClass == Currency.class) {
                return (Dao<T>) new CurrencyHibernateDaoImpl();
            }
            if (entityClass == IssuingBank.class) {
                return (Dao<T>) new IssuingBankHibernateDaoImpl();
            }
        }
        throw new UnsupportedOperationException("Hibernate not implemented for this entity yet.");
    }
}