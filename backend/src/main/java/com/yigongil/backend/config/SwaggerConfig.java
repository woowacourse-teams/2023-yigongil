package com.yigongil.backend.config;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile(value = {"local", "prod", "dev"})
@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    private final String serverUrl;

    public SwaggerConfig(@Value("${swagger.server-url}") String serverUrl) {
        this.serverUrl = serverUrl;
    }

    @Bean
    public OpenAPI publicApi() {
        Info info = new Info()
                .title("201 Created")
                .description("201 Created의 Open Api입니다.")
                .version("v1");

        SecurityScheme securityScheme = new SecurityScheme()
                .name("Authorization")
                .type(Type.HTTP)
                .in(HEADER)
                .bearerFormat("JWT")
                .scheme("Bearer");

        Components components = new Components().addSecuritySchemes("token", securityScheme);

        Server server = new Server();
        server.setUrl(serverUrl);

        return new OpenAPI()
                .info(info)
                .components(components)
                .addServersItem(server);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/api", "/v1/swagger-ui/index.html");
    }
}
