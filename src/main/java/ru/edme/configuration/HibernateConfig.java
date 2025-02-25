package ru.edme.configuration;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.InputStream;

public class HibernateConfig {
    private static final Logger logger = LogManager.getLogger(HibernateConfig.class);
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            logger.info("Creating session factory");

            // Проверяем, есть ли hibernate.cfg.xml в classpath
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("hibernate.cfg.xml");
            if (inputStream == null) {
                throw new RuntimeException("hibernate.cfg.xml not found in classpath");
            }

            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        } catch (Throwable ex) {
            throw new RuntimeException("Failed to create sessionFactory: " + ex);
        }
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
