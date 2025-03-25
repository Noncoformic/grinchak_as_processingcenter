package ru.edme.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.edme.configuration.AppConfig;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
public class AppConfigTest {

//    @Autowired
//    private DataSource dataSource;
//
//    @Test
//    public void testDataSource() {
//        assertNotNull(dataSource);
//
//    }

//    @Test
//    public void testEntityManagerFactory() {
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        EntityManagerFactory entityManagerFactory = (EntityManagerFactory) context.getBean(AppConfig.class).sessionFactory();
//        assertNotNull(entityManagerFactory);
//        context.close();
//    }


}
