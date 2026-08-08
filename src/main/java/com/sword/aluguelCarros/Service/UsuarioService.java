package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Repository.CarroRepository;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    private final UsuarioRepository usuarioRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.getUsuarios();
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.getUsuarioPorEmail(email);
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.getUsuario(id);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void delete(Integer id) {
        usuarioRepository.delete(id);
    }

    public Usuario update(Integer id, Usuario usuarioUpdate) {
        return usuarioRepository.update(id, usuarioUpdate);
    }
}
