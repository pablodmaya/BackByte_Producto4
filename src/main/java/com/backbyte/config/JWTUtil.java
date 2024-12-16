package com.backbyte.config;




import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component  // Marca la clase como un Bean de Spring
public class JWTUtil {

    private final Key key;
    private final long tokenExpirationTime;

    public JWTUtil() {
        String secretKey = "clave_secreta_super_segura_de_al_menos_32_caracteres";
        long expirationTime = 3600000; // 1 hora en milisegundos
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.tokenExpirationTime = expirationTime;
    }

    public String generateToken(String subject, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(subject);
        claims.put("roles", roles);
        return Jwts.builder()
                .setClaims(claims) // El identificador, por ejemplo el nombre de usuario y el rol
                .setIssuedAt(new Date()) // Fecha de emisión
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpirationTime)) // Fecha de expiración
                .signWith(key, SignatureAlgorithm.HS256) // Firma con clave secreta
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // Valida la firma y el contenido
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Captura cualquier excepción de validación
            return false;
        }
    }

    public String getSubjectFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
