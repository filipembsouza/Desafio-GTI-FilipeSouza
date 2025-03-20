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
    
    private static final String OAUTH_SCHEME_NAME = "oauth2";
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Gestão de Visitas para Unidade Prisional")
                        .version("1.0")
                        .description("API REST para gerenciar o fluxo de visitas em uma unidade prisional")
                        .contact(new Contact()
                                .name("GTI SEAPE-DF")
                                .email("gti@seape.df.gov.br")))
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME_NAME, createOAuthScheme()))
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME_NAME));
    }
    
    private SecurityScheme createOAuthScheme() {
        OAuthFlows flows = new OAuthFlows()
                .clientCredentials(new OAuthFlow()
                        .tokenUrl("http://localhost:8080/oauth2/token")
                        .scopes(new Scopes()
                                .addString("api.read", "Acesso de leitura à API")
                                .addString("api.write", "Acesso de escrita à API")))
                .password(new OAuthFlow()
                        .tokenUrl("http://localhost:8080/oauth2/token")
                        .scopes(new Scopes()
                                .addString("api.read", "Acesso de leitura à API")
                                .addString("api.write", "Acesso de escrita à API")))
                .authorizationCode(new OAuthFlow()
                        .authorizationUrl("http://localhost:8080/oauth2/authorize")
                        .tokenUrl("http://localhost:8080/oauth2/token")
                        .scopes(new Scopes()
                                .addString("api.read", "Acesso de leitura à API")
                                .addString("api.write", "Acesso de escrita à API")));
        
        return new SecurityScheme()
                .type(SecurityScheme.Type.OAUTH2)
                .description("Autenticação OAuth2 usando o servidor de autorização")
                .flows(flows);
    }
}