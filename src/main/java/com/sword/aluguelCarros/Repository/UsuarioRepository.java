package com.sword.aluguelCarros.Repository;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.sword.aluguelCarros.Model.Usuario;
import org.springframework.stereotype.Repository;

public class UsuarioRepository {
    // Estrutura que serve como um banco de dados em memória
    private final List<Usuario> usuarios = new ArrayList<>();

    // Estrutura para gerar id's
    private final AtomicInteger atomicInteger = new AtomicInteger(10);

    // obtem a lista imutável de carros
    public List<Usuario> getUsuario() {
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

    // metodo que inicia o banco com dados
    @PostConstruct
    public void init() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("");
        usuario.setEmail("");
        usuario.setSenha("");
        usuario.setRole("");

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
}
