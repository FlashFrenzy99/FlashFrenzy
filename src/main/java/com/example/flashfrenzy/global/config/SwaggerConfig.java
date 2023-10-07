package com.example.flashfrenzy.global.config;

import com.example.flashfrenzy.global.util.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import java.util.ArrayList;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.COOKIE,
        name = JwtUtil.AUTHORIZATION_HEADER, description = "Auth Token"
)
@SecurityScheme(
        type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.COOKIE,
        name = JwtUtil.REFRESH_HEADER, description = "Auth Refresh Token"
)
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("v1-definition")
                .pathsToMatch("/api/**")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        // swagger 토큰 사용 설정
        List<SecurityRequirement> securityRequirementList = new ArrayList<>();
        securityRequirementList.add(new SecurityRequirement().addList(JwtUtil.AUTHORIZATION_HEADER));
        securityRequirementList.add(new SecurityRequirement().addList(JwtUtil.REFRESH_HEADER));

        // swagger 서버 설정
        List<Server> serverList = new ArrayList<>();
        Server local = new Server();
        local.setUrl("http://localhost:8080");
        local.setDescription("local");
//        Server deploy = new Server();
//        deploy.setUrl("https://");
//        deploy.setDescription("deploy");
        serverList.add(local);
//        serverList.add(deploy);

        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info().title("FlashFrenzy API")
                .version("v0.0.1")
                .description("FlashFrenzy 프로젝트 API 명세서입니다."));
        openAPI.security(securityRequirementList);
        openAPI.setServers(serverList);
        return openAPI;
    }
}
