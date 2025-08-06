package com.example.ejercicio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    private static final String SWAGGER_UI_PATTERN = "/swagger-ui/**";
    private static final String API_DOCS_PATTERN = "/v3/api-docs/**";
    private static final String SWAGGER_UI_HTML = "/swagger-ui.html";
    private static final String ACTUATOR_PATTERN = "/actuator/**";
    private static final String API_PATTERN = "/api/**";
    private static final String CLIENTS_PATTERN = "/clientes/**";
    private static final String ACCOUNTS_PATTERN = "/cuentas/**";
    private static final String MOVEMENTS_PATTERN = "/movimientos/**";
    private static final String ALL_PATTERN = "/**";
    private static final String WILDCARD_ORIGIN = "*";
    
    private static final List<String> ALLOWED_METHODS = Arrays.asList("GET", "POST", "PUT", "DELETE");
    private static final List<String> ALLOWED_HEADERS = Arrays.asList("*");
    private static final List<String> ALLOWED_ORIGINS = Arrays.asList("*");
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(this::configureAuthorization)
            .build();
    }
    
    private void configureAuthorization(org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authz) {
        authz
            // Allow public access to documentation endpoints
            .requestMatchers(SWAGGER_UI_PATTERN, API_DOCS_PATTERN, SWAGGER_UI_HTML).permitAll()
            // Allow public access to actuator endpoints
            .requestMatchers(ACTUATOR_PATTERN).permitAll()
            // Allow public access to all API endpoints
            .requestMatchers(API_PATTERN).permitAll()
            // Allow public access to main controllers
            .requestMatchers(CLIENTS_PATTERN, ACCOUNTS_PATTERN, MOVEMENTS_PATTERN).permitAll()
            // Require authentication for any other endpoint
            .anyRequest().authenticated();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = createCorsConfiguration();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(ALL_PATTERN, configuration);
        return source;
    }
    
    private CorsConfiguration createCorsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(ALLOWED_ORIGINS);
        configuration.setAllowedMethods(ALLOWED_METHODS);
        configuration.setAllowedHeaders(ALLOWED_HEADERS);
        configuration.setAllowCredentials(true);
        return configuration;
    }
}
