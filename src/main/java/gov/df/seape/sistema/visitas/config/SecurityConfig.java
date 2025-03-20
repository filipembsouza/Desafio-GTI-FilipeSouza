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
 * 
 * Esta classe define a estratégia de segurança para a aplicação, incluindo:
 * - Configuração de filtros de segurança
 * - Gerenciamento de clientes OAuth2
 * - Definição de usuários em memória
 * - Configuração de codificação de senhas
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    /**
     * Configura o filtro de segurança para o servidor de autorização OAuth2.
     * 
     * Este método define as configurações de segurança específicas para o 
     * processo de autorização, incluindo tratamento de exceções de autenticação.
     * 
     * @param http Configuração de segurança HTTP
     * @return Cadeia de filtros de segurança para o servidor de autorização
     * @throws Exception Em caso de erro de configuração de segurança
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando filtro de segurança para servidor de autorização");
        
        // Aplica a configuração de segurança padrão para servidor de autorização
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        
        // Configura tratamento de exceções de autenticação
        http.exceptionHandling(exceptions -> 
            exceptions.authenticationEntryPoint(
                new LoginUrlAuthenticationEntryPoint("/login")
            )
        );
        
        return http.build();
    }

    /**
     * Configura o filtro de segurança padrão para a aplicação.
     * 
     * Define as regras de autorização para diferentes endpoints, 
     * configurações de CSRF e opções de login.
     * 
     * @param http Configuração de segurança HTTP
     * @return Cadeia de filtros de segurança padrão
     * @throws Exception Em caso de erro de configuração de segurança
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configurando filtro de segurança padrão");
        
        http.authorizeHttpRequests(authorize -> authorize
            // Endpoints públicos permitidos sem autenticação
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
            // Todos os outros endpoints requerem autenticação
            .anyRequest().authenticated()
        )
        // Desabilita CSRF para endpoints específicos
        .csrf(csrf -> csrf.ignoringRequestMatchers(
            "/h2-console/**", 
            "/swagger-ui/**", 
            "/v3/api-docs/**",
            "/oauth2/**"
        ))
        // Configura opções de cabeçalho para H2 Console
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
        // Configura login padrão
        .formLogin(Customizer.withDefaults());
        
        return http.build();
    }

    /**
     * Cria um repositório de clientes registrados para OAuth2.
     * 
     * Configura um cliente padrão com diferentes tipos de concessão de autorização,
     * escopos e configurações de token.
     * 
     * @return Repositório de clientes registrados
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        log.info("Configurando repositório de clientes registrados");
        
        // Cria um cliente com configurações detalhadas
        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("api-client")
            .clientSecret(passwordEncoder().encode("secret"))
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://localhost:8080/swagger-ui/oauth2-redirect.html")
            .scope("api.read")
            .scope("api.write")
            // Configurações de tempo de vida do token
            .tokenSettings(TokenSettings.builder()
                .accessTokenTimeToLive(Duration.ofMinutes(30))
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .build())
            // Configurações do cliente
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .build())
            .build();

        return new InMemoryRegisteredClientRepository(client);
    }

    /**
     * Configura o serviço de detalhes de usuário em memória.
     * 
     * Cria usuários padrão para teste e desenvolvimento:
     * - Usuário admin com papel ADMIN
     * - Usuário regular com papel USER
     * 
     * @return Gerenciador de detalhes de usuário
     */
    @Bean
    public UserDetailsService userDetailsService() {
        log.info("Configurando serviço de detalhes de usuário em memória");
        
        // Cria usuário administrador
        UserDetails adminUser = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin"))
            .roles("ADMIN")
            .build();

        // Cria usuário regular
        UserDetails regularUser = User.builder()
            .username("user")
            .password(passwordEncoder().encode("user"))
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(adminUser, regularUser);
    }

    /**
     * Cria um codificador de senhas seguro usando BCrypt.
     * 
     * Utiliza BCryptPasswordEncoder com força de hash de 12,
     * o que proporciona um bom equilíbrio entre segurança e desempenho.
     * 
     * @return Codificador de senhas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Configurando codificador de senhas BCrypt");
        return new BCryptPasswordEncoder(12);
    }

    /**
     * Configura as definições do servidor de autorização OAuth2.
     * 
     * Define o emissor (issuer) do servidor de autorização.
     * 
     * @return Configurações do servidor de autorização
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        log.info("Configurando definições do servidor de autorização");
        return AuthorizationServerSettings.builder()
            .issuer("http://localhost:8080")
            .build();
    }
}