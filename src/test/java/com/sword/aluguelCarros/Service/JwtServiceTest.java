package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.Model.Enum.UserRole;
import com.sword.aluguelCarros.Model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private Usuario usuarioPadrao;

    @BeforeEach
    void setUp() {
        // Instanciamos o serviço diretamente, sem precisar de Mocks
        jwtService = new JwtService();

        // Criamos um usuário falso para testar a geração do token
        usuarioPadrao = new Usuario();
        usuarioPadrao.setId(1);
        usuarioPadrao.setNome("Admin da Silva");
        usuarioPadrao.setEmail("admin@teste.com");
        usuarioPadrao.setRole(UserRole.ADMIN);
    }

    @Test
    void deveGerarTokenValidoComAsClaimsCorretas() {
        // Ação: Pede para o serviço gerar o token
        String token = jwtService.generateToken(usuarioPadrao);

        // Verificação 1: O token não pode ser nulo ou vazio
        assertNotNull(token, "O token não deveria ser nulo");
        assertFalse(token.isEmpty(), "O token não deveria estar vazio");

        // Verificação 2: Decodificamos o token para garantir que os dados do usuário estão lá dentro
        String secretKey = "minha-chave-secreta-do-projeto-aluguel-carros-123456";
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Comparamos se o que tem dentro do token é igual ao usuário que passamos
        assertEquals("admin@teste.com", claims.getSubject());
        assertEquals(1, claims.get("id", Integer.class));
        assertEquals("Admin da Silva", claims.get("nome", String.class));
        assertEquals("ADMIN", claims.get("role", String.class));
    }
}