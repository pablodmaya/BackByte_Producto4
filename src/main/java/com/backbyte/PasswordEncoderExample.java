package com.backbyte;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderExample {
    public static void main(String[] args) {
        // Crear una instancia de BCryptPasswordEncoder
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // La contraseña que deseas encriptar
        String rawPassword = "user";

        // Encriptar la contraseña
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Mostrar la contraseña encriptada
        System.out.println("Contraseña encriptada: " + encodedPassword);
    }
}