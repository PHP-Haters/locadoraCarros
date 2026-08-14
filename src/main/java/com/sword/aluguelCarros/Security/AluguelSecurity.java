package com.sword.aluguelCarros.Security;

import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.AluguelService;
import com.sword.aluguelCarros.Service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("aluguelSecurity")
public class AluguelSecurity {

    private final AluguelService aluguelService;
    private final UsuarioService usuarioService;

    public AluguelSecurity(
            AluguelService aluguelService,
            UsuarioService usuarioService
    ) {
        this.aluguelService = aluguelService;
        this.usuarioService = usuarioService;
    }

    // ADMIN pode acessar qualquer aluguel.
    // USER só pode acessar o próprio aluguel.
    public boolean podeAcessarAluguel(
            Integer id,
            Authentication authentication
    ) {

        if (isAdmin(authentication)) {
            return true;
        }

        Aluguel aluguel = aluguelService.findById(id);

        Usuario usuarioLogado =
                usuarioService.findByEmail(authentication.getName());

        return aluguel.getUsuarioId()
                .equals(usuarioLogado.getId());
    }

    // ADMIN pode acessar aluguéis de qualquer usuário.
    // USER só pode acessar os próprios aluguéis.
    public boolean podeAcessarAlugueisDoUsuario(
            Integer usuarioId,
            Authentication authentication
    ) {

        if (isAdmin(authentication)) {
            return true;
        }

        Usuario usuarioLogado =
                usuarioService.findByEmail(authentication.getName());

        return usuarioId.equals(usuarioLogado.getId());
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );
    }
}