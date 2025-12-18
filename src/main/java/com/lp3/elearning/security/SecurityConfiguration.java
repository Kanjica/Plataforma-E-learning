package com.lp3.elearning.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final SecurityFilter securityFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfiguration(SecurityFilter securityFilter, CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.securityFilter = securityFilter;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS habilitado
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        
                        // 2. Instrutores (Perfil Público)
                        .requestMatchers(HttpMethod.GET, "/instructors/**").permitAll()

                        // 3. Cursos e Categorias (Leitura Pública)
                        .requestMatchers(HttpMethod.GET, "/courses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/courses/search").permitAll() 
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        
                        // 4. Gestão de Cursos (Apenas Instrutores)
                        .requestMatchers(HttpMethod.POST, "/courses/**").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/courses/**").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/courses/**").hasRole("INSTRUCTOR")
                        
                        // 5. Módulos e Aulas (Gestão)
                        .requestMatchers(HttpMethod.POST, "/courses/*/modules/**").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.PUT, "/courses/*/modules/**").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.DELETE, "/courses/*/modules/**").hasRole("INSTRUCTOR")

                        // 6. Área do Aluno (Matrícula, Progresso, Dashboard)
                        .requestMatchers("/students/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/enrollments/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/enrollments/*/certificate").hasRole("STUDENT")
                        
                        // 7. Fórum e Reviews (Alunos e Instrutores)
                        .requestMatchers("/topics/**").authenticated()
                        .requestMatchers("/courses/*/reviews/**").authenticated()
                        .requestMatchers("/users/**").authenticated()

                        // Qualquer outra coisa precisa de autenticação
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception
                    .authenticationEntryPoint(customAuthenticationEntryPoint) 
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Frontend Angular
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}