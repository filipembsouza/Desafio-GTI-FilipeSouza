package gov.df.seape.sistema.visitas.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.Scopes;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // Nome do esquema de segurança no Swagger
    private static final String OAUTH_SCHEME_NAME = "oauth2";

    // URLs do Authorization Server
    private static final String TOKEN_URL = "http://localhost:8080/oauth2/token";
    private static final String AUTHORIZATION_URL = "http://localhost:8080/oauth2/authorize";

    // Scopes e descrições
    private static final String SCOPE_READ = "api.read";
    private static final String DESC_READ = "Acesso de leitura à API";
    private static final String SCOPE_WRITE = "api.write";
    private static final String DESC_WRITE = "Acesso de escrita à API";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de Visitas para Unidade Prisional")
                        .version("1.0")
                        .description("API REST para gerenciar o fluxo de visitas em uma unidade prisional")
                        .contact(new Contact()
                                .name("GTI SEAPE-DF")
                                .email("gti@seape.df.gov.br"))
                )
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme())
                )
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }

    /**
     * Cria a configuração do esquema de segurança OAuth2 para o Swagger/OpenAPI.
     */
    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = new OAuthFlows()
                // Fluxo Client Credentials
                .clientCredentials(new OAuthFlow()
                        .tokenUrl(TOKEN_URL)
                        .scopes(new Scopes()
                                .addString(SCOPE_READ, DESC_READ)
                                .addString(SCOPE_WRITE, DESC_WRITE)
                        )
                )
                // Fluxo Password
                .password(new OAuthFlow()
                        .tokenUrl(TOKEN_URL)
                        .scopes(new Scopes()
                                .addString(SCOPE_READ, DESC_READ)
                                .addString(SCOPE_WRITE, DESC_WRITE)
                        )
                )
                // Fluxo Authorization Code
                .authorizationCode(new OAuthFlow()
                        .authorizationUrl(AUTHORIZATION_URL)
                        .tokenUrl(TOKEN_URL)
                        .scopes(new Scopes()
                                .addString(SCOPE_READ, DESC_READ)
                                .addString(SCOPE_WRITE, DESC_WRITE)
                        )
                );

        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("Autenticação OAuth2 usando o Servidor de Autorização")
                .flows(flows);
    }
}
