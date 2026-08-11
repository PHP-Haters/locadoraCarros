package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            JwtService jwtService) {

        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
    }


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

    // =========================
    // CADASTRO
    // =========================

    public Usuario cadastrar(Usuario usuario) {

        Usuario usuarioExistente =
                usuarioRepository.getUsuarioPorEmail(usuario.getEmail());

        if (usuarioExistente != null) {
            throw new RuntimeException("E-mail já cadastrado");
        }

        if (usuario.getRole() == null || usuario.getRole().isBlank()) {
            usuario.setRole("USER");
        }

        return usuarioRepository.save(usuario);
    }

    // =========================
    // LOGIN
    // =========================

    public String login(String email, String senha) {

        Usuario usuario =
                usuarioRepository.getUsuarioPorEmail(email);

        if (usuario == null) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        if (!usuario.getSenha().equals(senha)) {
            throw new RuntimeException("E-mail ou senha inválidos");
        }

        return jwtService.generateToken(usuario);
    }
}