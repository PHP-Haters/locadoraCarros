package com.sword.aluguelCarros.Config;

import com.sword.aluguelCarros.Filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http

                // =================================================
                // Desabilita CSRF
                // =================================================

                .csrf(csrf -> csrf.disable())


                // =================================================
                // JWT é Stateless
                // =================================================

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =================================================
                // REGRAS DE AUTORIZAÇÃO
                // =================================================

                .authorizeHttpRequests(auth -> auth

                        // -----------------------------------------
                        // Rotas públicas
                        // -----------------------------------------

                        .requestMatchers(
                                "/usuario/login"
                        ).permitAll()

                        .requestMatchers(
                                "/usuario/cadastro"
                        ).permitAll()


                        // -----------------------------------------
                        // Rotas exclusivas do ADMIN
                        // -----------------------------------------

                        .requestMatchers(
                                "/carro/cadastrar"
                        ).hasRole("ADMIN")

                        .requestMatchers(
                                "/carro/excluir/**"
                        ).hasRole("ADMIN")


                        // -----------------------------------------
                        // Qualquer usuário autenticado
                        // -----------------------------------------

                        .anyRequest().authenticated()
                )


                // =================================================
                // Adiciona nosso filtro JWT
                // =================================================

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}