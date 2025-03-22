package gov.df.seape.sistema.visitas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class OpenApiConfig {

    private static final String SCOPE_API_READ = "api.read";
    private static final String SCOPE_API_WRITE = "api.write";
    private static final String DESC_ACESSO_LEITURA = "Acesso de leitura à API";
    private static final String DESC_ACESSO_ESCRITA = "Acesso de escrita à API";

    @Value("${spring.application.name:sistema-visitas}")
    private String applicationName;

    @Value("${springdoc.swagger-ui.oauth.token-url:http://localhost:8080/oauth2/token}")
    private String tokenUrl;

    @Value("${springdoc.swagger-ui.oauth.authorization-url:http://localhost:8080/oauth2/authorize}")
    private String authorizationUrl;

    @Value("${springdoc.swagger-ui.oauth.client-id:api-client}")
    private String clientId;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de Visitas para Unidade Prisional")
                        .description("API REST para gerenciar o fluxo de visitas em uma unidade prisional")
                        .version("1.0.0")
                        .license(new License().name("Governo do Distrito Federal").url("https://www.df.gov.br"))
                        .contact(new Contact()
                                .name("GTI SEAPE-DF")
                                .email("gti@seape.df.gov.br")
                                .url("https://www.seape.df.gov.br")))
                .servers(servers())
                .components(new Components()
                        .addSecuritySchemes("oauth2", createOAuthSecurityScheme())
                        .addSecuritySchemes("bearer-jwt", createBearerSecurityScheme()))
                .addSecurityItem(new SecurityRequirement()
                        // Uso das constantes aqui:
                        .addList("oauth2", Arrays.asList(SCOPE_API_READ, SCOPE_API_WRITE))
                        .addList("bearer-jwt"));
    }

    private List<Server> servers() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("Servidor de Desenvolvimento");

        return Arrays.asList(localServer);
    }

    private SecurityScheme createOAuthSecurityScheme() {
        OAuthFlows flows = new OAuthFlows()
                .password(new OAuthFlow()
                        .tokenUrl(tokenUrl)
                        .scopes(new io.swagger.v3.oas.models.security.Scopes()
                                // Uso das constantes aqui:
                                .addString(SCOPE_API_READ, DESC_ACESSO_LEITURA)
                                .addString(SCOPE_API_WRITE, DESC_ACESSO_ESCRITA)))
                .clientCredentials(new OAuthFlow()
                        .tokenUrl(tokenUrl)
                        .scopes(new io.swagger.v3.oas.models.security.Scopes()
                                .addString(SCOPE_API_READ, DESC_ACESSO_LEITURA)
                                .addString(SCOPE_API_WRITE, DESC_ACESSO_ESCRITA)))
                .authorizationCode(new OAuthFlow()
                        .authorizationUrl(authorizationUrl)
                        .tokenUrl(tokenUrl)
                        .scopes(new io.swagger.v3.oas.models.security.Scopes()
                                .addString(SCOPE_API_READ, DESC_ACESSO_LEITURA)
                                .addString(SCOPE_API_WRITE, DESC_ACESSO_ESCRITA)));

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("Autenticação OAuth2")
                .flows(flows);
    }

    private SecurityScheme createBearerSecurityScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Token JWT de autenticação");
    }
}
