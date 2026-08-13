package com.sword.aluguelCarros.Security;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("usuarioSecurity")
public class UsuarioSecurity {

    private final UsuarioService usuarioService;

    public UsuarioSecurity(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // ADMIN pode acessar qualquer usuário.
    // USER só pode acessar o próprio usuário através do ID.
    public boolean podeAcessarUsuario(
            Integer id,
            Authentication authentication
    ) {

        if (isAdmin(authentication)) {
            return true;
        }

        Usuario usuario = usuarioService.findById(id);

        return usuario.getEmail().equals(authentication.getName());
    }

    // ADMIN pode buscar qualquer usuário por email.
    // USER só pode buscar a si próprio.
    public boolean podeAcessarUsuarioPorEmail(
            String email,
            Authentication authentication
    ) {

        if (isAdmin(authentication)) {
            return true;
        }

        return authentication.getName().equals(email);
    }

    // Método auxiliar para não repetir a verificação
    private boolean isAdmin(Authentication authentication) {

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );
    }
}