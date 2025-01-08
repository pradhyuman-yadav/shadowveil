package com.shadowveil.videoplatform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ShadowVeil Video Streaming API")
                        .version("1.0")
                        .description("API documentation for the ShadowVeil Video Streaming platform")
                        .termsOfService("https://example.com/terms")
                        .contact(new Contact()
                                .name("Pradhyuman Yadav")
                                .email("pradhyuman999@gmail.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://opensource.org/licenses/Apache-2.0")));
    }
}
