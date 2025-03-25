package ru.edme.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Processing Center API")
                        .version("1.0")
                        .description("Документация API процессингового центра"))
                .externalDocs(new ExternalDocumentation()
                        .description("Репозиторий")
                        .url("https://bitbucket.edme.pro/projects/JAPP/repos/grinchak_as_nov1_processingcenter/browse"));
    }
}
