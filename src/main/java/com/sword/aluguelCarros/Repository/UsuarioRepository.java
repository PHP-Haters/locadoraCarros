package com.sword.aluguelCarros.Repository;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.sword.aluguelCarros.Model.Usuario;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository {
    // Estrutura que serve como um banco de dados em memória
    private final List<Usuario> usuarios = new ArrayList<>();

    // Estrutura para gerar id's
    private final AtomicInteger atomicInteger = new AtomicInteger(10);

    // obtem a lista imutável de usuarios
    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public Usuario getUsuario(Integer id) {
        // itera na lista de usuarios e verifica
        // se existe usuario com o id especificado
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    // Método para autenticação/login
    public Usuario getUsuarioPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail() != null && usuario.getEmail().equalsIgnoreCase(email)) {
                return usuario;
            }
        }
        return null;
    }

    // metodo que inicia o banco com dados
    @PostConstruct
    public void init() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Admin");
        usuario.setEmail("admin@email.com");
        usuario.setSenha("admin");
        usuario.setRole("ADMIN");

        usuarios.add(usuario);
    }

    public Usuario save(Usuario usuario) {
        // incrementa +1 no id e obtem o valor
        atomicInteger.incrementAndGet();
        usuario.setId(atomicInteger.get());
        usuarios.add(usuario);
        return usuario;
    }

    public void delete(Integer id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                usuarios.remove(usuario);
                return; // sair do loop
            }
        }
    }

    // Permite atualização no CRUD
    public Usuario update(Integer id, Usuario usuarioUpdate) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                if (usuarioUpdate.getNome() != null) {
                    usuario.setNome(usuarioUpdate.getNome());
                }
                if (usuarioUpdate.getEmail() != null) {
                    usuario.setEmail(usuarioUpdate.getEmail());
                }
                if (usuarioUpdate.getSenha() != null) {
                    usuario.setSenha(usuarioUpdate.getSenha());
                }
                if (usuarioUpdate.getRole() != null) {
                    usuario.setRole(usuarioUpdate.getRole());
                }
                return usuario;
            }
        }
        return null;
    }

}
