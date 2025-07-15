package org.github.guardjo.cloudtype.manager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(apiServer()));
    }
    
    private Info apiInfo() {
        return new Info()
                .title("Cloudtype Manager API")
                .description("Cloudtype Manager 대시보드에서 사용되는 API 목록")
                .version("1.0.0");
    }

    private Server apiServer() {
        return new Server()
                .url("/");
    }
}
