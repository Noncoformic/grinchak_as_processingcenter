package ru.edme.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.edme.model.Account;
import ru.edme.model.Card;
import ru.edme.model.CardStatus;
import ru.edme.model.Currency;
import ru.edme.model.IssuingBank;

import java.util.Properties;

public class HibernateConfig {
    private static final Logger logger = LogManager.getLogger(HibernateConfig.class);
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(getHibernateProperties())
                        .build();
                MetadataSources metadataSources = new MetadataSources(standardRegistry);
                metadataSources.addAnnotatedClass(Account.class);
                metadataSources.addAnnotatedClass(Card.class);
                metadataSources.addAnnotatedClass(CardStatus.class);
                metadataSources.addAnnotatedClass(Currency.class);
                metadataSources.addAnnotatedClass(IssuingBank.class);
                sessionFactory = metadataSources.buildMetadata().buildSessionFactory();
            } catch (Exception e) {
                logger.error("SessionFactory creation failed", e);
                throw new RuntimeException("SessionFactory creation failed", e);
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    private static Properties getHibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.connection.driver_class", PropertiesUtil.get("db.driver"));
        properties.put("hibernate.connection.url", PropertiesUtil.get("db.url"));
        properties.put("hibernate.connection.username", PropertiesUtil.get("db.username"));
        properties.put("hibernate.connection.password", PropertiesUtil.get("db.password"));
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.show_sql", "true");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        // HikariCP settings
        properties.put("hibernate.hikari.connectionTimeout", PropertiesUtil.get("hikari.connectionTimeout"));
        properties.put("hibernate.hikari.minimumIdle", PropertiesUtil.get("hikari.minimumIdle"));
        properties.put("hibernate.hikari.maximumPoolSize", PropertiesUtil.get("hikari.maximumPoolSize"));
        properties.put("hibernate.hikari.idleTimeout", PropertiesUtil.get("hikari.idleTimeout"));
        return properties;
    }
}