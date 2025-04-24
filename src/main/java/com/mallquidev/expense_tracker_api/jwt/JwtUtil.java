package com.mallquidev.expense_tracker_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {
    //6
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private int expiration;


    public String generateToken(Authentication authentication) {
        UserDetails mainUser = (UserDetails) authentication.getPrincipal(); // obtiene el usuario autenticado
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // genera la clave secreta en base al 'secret'
        return Jwts.builder() // comienza la construcción del token
                .setSubject(mainUser.getUsername()) // establece el username como subject del token
                .setIssuedAt(new Date()) // fecha de creación del token
                .setExpiration(new Date(new Date().getTime() + expiration * 1000L)) // fecha de expiración
                .signWith(key, SignatureAlgorithm.HS256) // firma el token con la clave y algoritmo HS256
                .compact(); // genera el string final del token
    }

    //Decodifcamos el token para obtner el cliam(sub, exp, etc)
    public Claims extractAllClaims(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); // genera la clave a partir del secret
        return Jwts.parserBuilder() // comienza la construcción del parser
                .setSigningKey(key) // establece la clave para validar la firma del token
                .build() // construye el parser
                .parseClaimsJws(token) // analiza y valida el token
                .getBody(); // devuelve el cuerpo del token (claims)
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getIssuedAt().before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
