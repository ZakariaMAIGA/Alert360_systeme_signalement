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
@EnableMethodSecurity // Permet d'utiliser @PreAuthorize si besoin
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

                        // Autoriser toutes les requêtes de pré-vérification CORS (OPTIONS)
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 2. ENDPOINTS PUBLICS & DOCUMENTATION SWAGGER
                        .requestMatchers("/api/v1/auth/**", "/api/auth/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/actualites/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/contenus/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").permitAll()

                        // 3. SIGNALEMENTS (/api/signalements)
                        .requestMatchers(HttpMethod.GET, "/api/signalements/**").hasAnyRole("CITOYEN", "STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/signalements/**").hasAnyRole("CITOYEN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/signalements/**").hasAnyRole("CITOYEN", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/signalements/*/statut").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/signalements/*/assigner").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/signalements/**").hasRole("ADMIN")

                        // 4. PREUVES DE RESOLUTION (/api/preuves)
                        .requestMatchers(HttpMethod.GET, "/api/preuves/**").hasAnyRole("CITOYEN", "STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/preuves/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/preuves/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/preuves/**").hasRole("ADMIN")

                        // 5. ACTIONS CITOYENNES (/api/actions)
                        .requestMatchers(HttpMethod.GET, "/api/actions/**").hasAnyRole("CITOYEN", "STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/actions/**").hasAnyRole("CITOYEN", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/actions/**").hasAnyRole("CITOYEN", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/actions/**").hasRole("ADMIN")

                        // 6. GESTION DES CITOYENS (/api/citoyens)
                        .requestMatchers(HttpMethod.POST, "/api/citoyens").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/citoyens/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/citoyens/**").hasAnyRole("CITOYEN", "ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/citoyens/*/badges").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/citoyens/**").hasRole("ADMIN")

                        // 7. GESTION DES AGENTS DE STRUCTURE (/api/agents)
                        .requestMatchers(HttpMethod.GET, "/api/agents/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/agents/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/agents/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/agents/**").hasAnyRole("STRUCTURE", "ADMIN")

                        // 8. GESTION DES STRUCTURES COMPETENTES (/api/structures)
                        .requestMatchers(HttpMethod.GET, "/api/structures/**").hasAnyRole("CITOYEN", "STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/structures/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/structures/**").hasAnyRole("STRUCTURE", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/structures/**").hasRole("ADMIN")

                        // 9. CONTENUS & ACTUALITES
                        .requestMatchers(HttpMethod.POST, "/api/actualites/**", "/api/contenus/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/actualites/**", "/api/contenus/**", "/api/categories/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/actualites/**", "/api/contenus/**", "/api/categories/**").hasRole("ADMIN")

                        // 10. ADMINISTRATION
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/utilisateurs/**").hasRole("ADMIN")

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