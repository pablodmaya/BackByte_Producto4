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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityConfig {

    private final UsuarioService usuarioService;
    private final JWTUtil jwtUtil;  // Spring inyectará automáticamente el JWTUtil

    // Inyecta el UsuarioService y JWTUtil automáticamente
    @Autowired
    public SecurityConfig(UsuarioService usuarioService, JWTUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        http.csrf().disable()
                .authorizeRequests(auth -> {
                            try {
                                auth
                                        // Rutas públicas sin autenticación (permitidas sin restricciones)
                                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**").permitAll()

                                        // Rutas de la API que requieren JWT para rutas /api/token/**
                                        .requestMatchers("/api/token/**").authenticated()  // Exige un JWT para acceder a estas rutas

                                        // Rutas de registro y favicon que son públicas
                                        .requestMatchers("/login", "/registro", "/registro/registrarUsuario", "/favicon.ico").permitAll()

                                        // Rutas de admin que requieren el rol "admin"
                                        .requestMatchers("/user/**").hasRole("user")     // Rutas de usuario que requieren JWT
                                        .requestMatchers("/admin/**").hasRole("admin")

                                        // Otras rutas protegidas que deben estar autenticadas (con token o basic auth)
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
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(usuarioService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
