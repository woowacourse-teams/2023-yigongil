package com.yigongil.backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI publicApi() {
        Info info = new Info()
                .title("201 Created")
                .description("201 Created의 Open Api입니다.")
                .version("v1");

        SecurityScheme securityScheme = new SecurityScheme()
                .type(Type.HTTP)
                .scheme("Bearer")
                .name("github");

        Components components = new Components().addSecuritySchemes("token", securityScheme);

        return new OpenAPI()
                .info(info)
                .components(components);
    }
}
