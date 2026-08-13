package com.sword.aluguelCarros.Security;

import com.sword.aluguelCarros.Service.JwtService;

// ATENÇÃO: Mudança de jakarta.servlet para javax.servlet
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // =====================================================
        // 1. Pega o Authorization Header
        // =====================================================

        String authorizationHeader =
                request.getHeader("Authorization");


        // =====================================================
        // 2. Verifica se existe um Bearer Token
        // =====================================================

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }


        // =====================================================
        // 3. Remove o "Bearer "
        // =====================================================

        String token = authorizationHeader.substring(7);


        // =====================================================
        // 4. Valida o JWT
        // =====================================================

        if (!jwtService.isTokenValid(token)) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter().write(
                    "Token invalido ou expirado"
            );

            return;
        }


        // =====================================================
        // 5. Extrai informações do JWT
        // =====================================================

        String email =
                jwtService.extractUsername(token);

        String role =
                jwtService.extractRole(token);


        // =====================================================
        // 6. Converte o role para uma Authority
        // =====================================================

        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority("ROLE_" + role);

        // =====================================================
        // 7. Cria a autenticação
        // =====================================================

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        Collections.singletonList(authority)
                );


        // =====================================================
        // 8. Coloca a autenticação no SecurityContext
        // =====================================================

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);


        // =====================================================
        // 9. Continua a requisição
        // =====================================================

        filterChain.doFilter(request, response);
    }
}