package gov.df.seape.sistema.visitas.config;

import java.time.Duration;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuração centralizada de segurança para o Sistema de Visitas Prisionais.
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    // Constantes para manter padronização e evitar literais duplicados
    private static final String ISSUER_URL = "http://localhost:8080";
    private static final String CLIENT_ID = "api-client";
    private static final String CLIENT_SECRET = "secret";
    private static final String REDIRECT_URI_SWAGGER = "http://localhost:8080/swagger-ui/oauth2-redirect.html";
    private static final String SCOPE_READ = "api.read";
    private static final String SCOPE_WRITE = "api.write";

    /**
     * 1) Filtro de segurança para o Servidor de Autorização (endpoints /oauth2/authorize, /oauth2/token etc.).
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando filtro de segurança para servidor de autorização");

        // Aplica as configurações padrão do Authorization Server
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // Define como tratar falhas de autenticação (redireciona para /login)
        http.exceptionHandling(exceptions ->
                exceptions.authenticationEntryPoint(
                        new LoginUrlAuthenticationEntryPoint("/login")
                )
        );

        return http.build();
    }

    /**
     * 2) Filtro de segurança geral (acesso aos endpoints da aplicação).
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando filtro de segurança padrão (Resource Server + Web)");

        http
            .authorizeHttpRequests(authorize -> authorize
                // Endpoints públicos que dispensam autenticação
                .requestMatchers(
                    "/public/**",
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/v3/api-docs.yaml",
                    "/diagnostico/**",
                    "/actuator/**"
                ).permitAll()
                // Demais endpoints requerem autenticação
                .anyRequest().authenticated()
            )
            // Desabilita CSRF para alguns endpoints (ex.: H2, Swagger)
            .csrf(csrf -> csrf.ignoringRequestMatchers(
                    "/h2-console/**",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/oauth2/**"
            ))
            // Permite o uso do H2 Console sem problemas de 'frameOptions'
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            
            // Ativa o Resource Server para aceitar e validar tokens JWT
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))

            // Permite login via formulário para usuários que não estejam usando token
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Repositório de clientes OAuth2 (clientId, clientSecret, scopes, etc.) em memória.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        log.info("Configurando repositório de clientes registrados");

        // Configura um client "api-client" com várias grant types
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId(CLIENT_ID)
            .clientSecret(passwordEncoder().encode(CLIENT_SECRET))  //  Senha fixa e simples usada apenas em DESAFIO/DEMO
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri(REDIRECT_URI_SWAGGER)
            .scope(SCOPE_READ)
            .scope(SCOPE_WRITE)
            // Configura tempos de vida dos tokens
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(30))
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .build())
            // Desativa a tela de consentimento (para teste)
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build())
            .build();

        return new InMemoryRegisteredClientRepository(client);
    }

    /**
     * Define usuários em memória para testes (admin / user).
     */
    @Bean
    public UserDetailsService userDetailsService() {
        log.info("Configurando serviço de detalhes de usuário em memória");

        UserDetails adminUser = User.builder()
            .username("admin")
            // ATENÇÃO: Uso de senha simples para fins de DEMO
            .password(passwordEncoder().encode("admin"))  //  Senha fixa e simples usada apenas em DESAFIO/DEMO
            .roles("ADMIN")
            .build();

        UserDetails regularUser = User.builder()
            .username("user")
            // ATENÇÃO: Uso de senha simples para fins de DEMO
            .password(passwordEncoder().encode("user")) //  Senha fixa e simples usada apenas em DESAFIO/DEMO
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(adminUser, regularUser);
    }

    /**
     * Cria um codificador de senha (BCrypt) com custo de 12.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configurando codificador de senhas BCrypt");
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Configurações gerais do Authorization Server (issuer, etc.).
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        log.info("Configurando definições do servidor de autorização");
        return AuthorizationServerSettings.builder()
            .issuer(ISSUER_URL)
            .build();
    }
}