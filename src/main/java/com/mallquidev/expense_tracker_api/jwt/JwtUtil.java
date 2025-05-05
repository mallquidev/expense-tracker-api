package com.mallquidev.expense_tracker_api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    //generamos el token
    public String generateToken(Authentication authentication) {
        //casteamos el usuario autenticado con userDetails para poder acceder a mas info
        UserDetails mainUser = (UserDetails) authentication.getPrincipal();
        //creamos una clave secreta
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()//iniciamos el proceso para crear el token los claims
                .setSubject(mainUser.getUsername())//establece el nombre de usuario subject
                .setIssuedAt(new Date()) //fecha y hora en que se emite el token
                .setExpiration(new Date(new Date().getTime() + expiration * 1000L))//fecha de exp del token
                .signWith(key, SignatureAlgorithm.HS256)//firma el token con la clave y usa algoritmo HS256
                .compact();//finaliza y convierte el token en un string compact
    }

    //decodificamos el token
    public Claims extractAllClaims(String token) {
        //creamos la clave secreta
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)//pasamos la clave para validar la firma del token
                .build()//Construimos el parser(analizador)
                .parseClaimsJws(token)//Analizamos y verificamos el token recibido
                .getBody();//Obtenemos solo el cuerpo del JWT, es decir, los columns (sub, exp, etc)
    }

    public String getUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = getUsername(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

}
