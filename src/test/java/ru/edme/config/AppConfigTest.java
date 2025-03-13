package ru.edme.config;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.edme.configuration.AppConfig;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppConfigTest {

    @Autowired
    private DataSource dataSource;

    @Test
    public void testDataSource() {
        assertNotNull(dataSource);

    }

    @Test
    public void testEntityManagerFactory() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) context.getBean(AppConfig.class).sessionFactory();
        assertNotNull(entityManagerFactory);
        context.close();
    }


}
