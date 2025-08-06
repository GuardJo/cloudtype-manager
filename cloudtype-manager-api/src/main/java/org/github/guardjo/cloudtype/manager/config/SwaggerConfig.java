package org.github.guardjo.cloudtype.manager.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        String jwtTokenSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtTokenSchemeName);

        Components components = new Components()
                .addSecuritySchemes(jwtTokenSchemeName, new SecurityScheme()
                        .name(jwtTokenSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .components(components)
                .info(apiInfo())
                .addSecurityItem(securityRequirement)
                .servers(List.of(apiServer()));
    }

    private Info apiInfo() {
        String description = "Cloudtype Manager 대시보드에서 사용되는 API 목록<br>"
                + "인증이 필요한 API는 **Authorize** 버튼을 눌러 JWT 토큰을 등록해주세요.<br><br>"
                + "➡️ **<a href='/oauth2/authorization/google' target='_blank'>인증 토큰 발급받기</a>** ⬅️<br>"
                + "<span style='color:red;'>(위 링크를 클릭하여 로그인 후 토큰을 발급받으세요.)</span>";
        
        return new Info()
                .title("Cloudtype Manager API")
                .description(description)
                .version("1.0.0");
    }

    private Server apiServer() {
        return new Server()
                .url("/");
    }
}
