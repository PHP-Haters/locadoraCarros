package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.Model.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "minha-chave-secreta-do-projeto-aluguel-carros-123456";

    private static final long EXPIRATION_TIME =
            86400000; // 24 horas

    private final SecretKey key =
            Keys.hmacShaKeyFor(
                    SECRET_KEY.getBytes(StandardCharsets.UTF_8)
            );


    // =========================================================
    // GERAÇÃO DO TOKEN
    // =========================================================

    public String generateToken(Usuario usuario) {

        return Jwts.builder()
                .subject(usuario.getEmail())
                .claim("id", usuario.getId())
                .claim("nome", usuario.getNome())
                .claim("role", usuario.getRole())
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )
                .signWith(key)
                .compact();
    }


    // =========================================================
    // EXTRAIR EMAIL
    // =========================================================

    public String extractUsername(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // =========================================================
    // EXTRAIR ROLE
    // =========================================================

    public String extractRole(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }


    // =========================================================
    // VALIDAR TOKEN
    // =========================================================

    public boolean isTokenValid(String token) {

        try {

            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }
}