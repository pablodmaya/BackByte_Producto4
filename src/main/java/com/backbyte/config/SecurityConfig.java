package com.backbyte.config;

import com.backbyte.service.UsuarioService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final UsuarioService usuarioService;

    private final JWTUtil jwtUtil;  // Spring inyectará automáticamente el JWTUtil

    // Inyecta el UsuarioService, que implementa UserDetailsService
    public SecurityConfig(UsuarioService usuarioService, JWTUtil jwtUtil) {

        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable()
                .authorizeHttpRequests(auth -> {
                    try {
                        auth
                                .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**").permitAll()
                                // Rutas de la API que requieren JWT para rutas /api/token/**
                                .requestMatchers("/token/**").authenticated()  // Exige un JWT para acceder a estas rutas
                                .requestMatchers("/registro", "/registro/registrarUsuario", "/favicon.ico").permitAll()
                                .requestMatchers("/madrid", "/barcelona", "/bilbao", "/sevilla", "/valencia","/vehiculos").permitAll()
                                .requestMatchers("/api/*").permitAll()
                                .requestMatchers("/admin/**").hasRole("admin")
                                .requestMatchers("/user/**").hasRole("user")
                                .anyRequest().authenticated()
                                .and()
                                .exceptionHandling()
                                .authenticationEntryPoint((request, response, authException) -> {
                                    // Devuelve 401 Unauthorized para errores de autenticación
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {
                                    // Devuelve 403 Forbidden para errores de autorización
                                    response.setContentType("application/json");
                                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                    response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"" + accessDeniedException.getMessage() + "\"}");
                                });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                )
                .httpBasic().disable()
                .formLogin().disable()

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // Generar el token JWT una vez que la autenticación es exitosa
                            String username = authentication.getName();

                            List<String> roles = authentication.getAuthorities().stream()
                                    .map(GrantedAuthority::getAuthority) // Convertir las autoridades a String (e.g., "ROLE_USER")
                                    .collect(Collectors.toList());

                            String token = jwtUtil.generateToken(username, roles);

                            // Configurar la respuesta JSON con el token JWT
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"token\": \"" + token + "\"}");
                        })
                )

                .logout()
                .logoutUrl("/logout")  // URL para cerrar sesión
                .logoutSuccessUrl("/home")  // Redirigir al login con parámetro de logout
                .invalidateHttpSession(true)  // Invalida la sesión
                .clearAuthentication(true)   // Elimina la autenticación
                .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configuración de AuthenticationManager usando el AuthenticationManagerBuilder
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}