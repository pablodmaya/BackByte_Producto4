package com.backbyte.config;

import com.backbyte.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final UsuarioService usuarioService;

    // Inyecta el UsuarioService, que implementa UserDetailsService
    public SecurityConfig(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/registro", "/registro/registrarUsuario", "/favicon.ico").permitAll()
                        .requestMatchers("/madrid", "/barcelona", "/bilbao", "/sevilla", "/valencia","/vehiculos").permitAll()
                        .requestMatchers("/admin/**").hasRole("admin")
                        .requestMatchers("/user/**").hasRole("user")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .loginProcessingUrl("/login")
                        .successHandler((request, response, authentication) -> {
                            // Redirección según el rol
                            boolean redirected = false;
                            for (var authority : authentication.getAuthorities()) {
                                if (authority.getAuthority().equals("ROLE_admin")) {
                                    response.sendRedirect("/admin/home");
                                    redirected = true;
                                    break; // Salimos del bucle
                                } else if (authority.getAuthority().equals("ROLE_user")) {
                                    response.sendRedirect("/user/home");
                                    redirected = true;
                                    break; // Salimos del bucle
                                }
                            }
                            if (!redirected) {
                                response.sendRedirect("/home"); // Redirección por defecto
                            }
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