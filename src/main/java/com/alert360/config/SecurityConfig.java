package com.alert360.config;

import com.alert360.security.jwt.JwtAuthenticationFilter;
import com.alert360.security.services.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. GESTION CORS ET CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // Requetes OPTIONS (Pre-flight CORS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. ENDPOINTS PUBLICS & SWAGGER
                        .requestMatchers("/api/v1/auth/**", "/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/actualites", "/api/actualites/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/contenus", "/api/contenus/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories", "/api/categories/**").permitAll()

                        // 3. SIGNALEMENTS (/api/signalements)
                        .requestMatchers(HttpMethod.GET, "/api/signalements", "/api/signalements/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/signalements", "/api/signalements/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "ADMIN", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/api/signalements", "/api/signalements/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "ADMIN", "ROLE_ADMIN")

                        // Changement de statut (ex: /api/signalements/123/statut ou /api/signalements/statut)
                        .requestMatchers(HttpMethod.PATCH, "/api/signalements/*/statut", "/api/signalements/statut/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")

                        // Assignation de structure (ex: /api/signalements/123/assigner ou /api/signalements/assigner/**)
                        .requestMatchers(HttpMethod.PATCH, "/api/signalements/*/assigner", "/api/signalements/assigner/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")

                        // Assignation d'agent (ex: /api/signalements/123/assigner-agent ou /api/signalements/assigner-agent/**)
                        .requestMatchers(HttpMethod.PATCH, "/api/signalements/*/assigner-agent", "/api/signalements/assigner-agent/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/api/signalements", "/api/signalements/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 4. PREUVES DE RESOLUTION (/api/preuves)
                        .requestMatchers(HttpMethod.GET, "/api/preuves", "/api/preuves/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/preuves", "/api/preuves/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/preuves", "/api/preuves/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/preuves", "/api/preuves/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 5. ACTIONS CITOYENNES (/api/actions)
                        .requestMatchers(HttpMethod.GET, "/api/actions", "/api/actions/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/actions", "/api/actions/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/actions", "/api/actions/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/actions", "/api/actions/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 6. GESTION DES CITOYENS (/api/citoyens)
                        .requestMatchers(HttpMethod.POST, "/api/citoyens").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/citoyens", "/api/citoyens/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/citoyens", "/api/citoyens/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/citoyens/*/badges", "/api/citoyens/badges/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/citoyens", "/api/citoyens/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 7. GESTION DES AGENTS DE STRUCTURE (/api/agents)
                        .requestMatchers("/api/agents", "/api/agents/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")

                        // 8. GESTION DES STRUCTURES COMPETENTES (/api/structures)
                        .requestMatchers(HttpMethod.GET, "/api/structures", "/api/structures/**")
                        .hasAnyAuthority("CITOYEN", "ROLE_CITOYEN", "STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/structures", "/api/structures/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/structures", "/api/structures/**")
                        .hasAnyAuthority("STRUCTURE", "ROLE_STRUCTURE", "ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/structures", "/api/structures/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 9. CONTENUS & ACTUALITES
                        .requestMatchers(HttpMethod.POST, "/api/actualites/**", "/api/contenus/**", "/api/categories/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/actualites/**", "/api/contenus/**", "/api/categories/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/actualites/**", "/api/contenus/**", "/api/categories/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        // 10. ADMINISTRATION
                        .requestMatchers("/api/admin/**", "/api/utilisateurs/**")
                        .hasAnyAuthority("ADMIN", "ROLE_ADMIN")

                        .anyRequest().authenticated()
                );

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}