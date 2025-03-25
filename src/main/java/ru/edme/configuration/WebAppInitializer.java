//package ru.edme.configuration;
//
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
//
//public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
//
//    @Override
//    protected Class<?>[] getRootConfigClasses() {
//        return new Class[]{AppConfig.class}; // Корневая конфигурация
//    }
//
//    @Override
//    protected Class<?>[] getServletConfigClasses() {
//        return new Class[]{WebConfig.class}; // Spring MVC
//    }
//
//    @Override
//    protected String[] getServletMappings() {
//        return new String[]{"/"}; // Обрабатывает все запросы
//    }
//}
//
