package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.ExceptionHandlers.GenericExceptions;
import com.sword.aluguelCarros.Model.Enum.UserRole;
import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired(required = false)
    private JwtService jwtService;

    public String saveUsuario(Usuario novoUsuario) {
        try {
            if (usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
                throw new GenericExceptions.AlreadyExists("E-mail já cadastrado no sistema.");
            }

            if (novoUsuario.getRole() == null) {
                novoUsuario.setRole(UserRole.USER);
            }

            usuarioRepository.save(novoUsuario);
            return "Usuário salvo com sucesso";
        }
        catch (GenericExceptions.AlreadyExists ex) {
            throw ex;
        }
        catch (DataIntegrityViolationException ex) {
            throw new GenericExceptions.InvalidData(
                    "Dados inválidos para o usuário: " + ex.getMessage()
            );
        }
        catch (Exception ex) {
            throw new GenericExceptions.General(
                    "Erro inesperado ao salvar o usuário: " + ex.getMessage()
            );
        }
    }

    public List<Usuario> findAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        if (usuarios.isEmpty()) {
            throw new GenericExceptions.General(
                    "Não existem usuários cadastrados."
            );
        } else {
            return usuarios;
        }
    }

    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new GenericExceptions.NotFound("Usuário não encontrado."));
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new GenericExceptions.NotFound("Usuário não encontrado com o e-mail informado."));
    }

    public Usuario update(Integer id, Usuario novoUsuario) {
        Usuario update = findById(id);

        if (novoUsuario.getNome() != null) {
            update.setNome(novoUsuario.getNome());
        }
        if (novoUsuario.getEmail() != null) {
            // Se o e-mail mudou, valida se o novo e-mail já pertence a outro usuário
            if (!update.getEmail().equals(novoUsuario.getEmail()) && usuarioRepository.existsByEmail(novoUsuario.getEmail())) {
                throw new GenericExceptions.AlreadyExists("E-mail já em uso por outro usuário.");
            }
            update.setEmail(novoUsuario.getEmail());
        }
        if (novoUsuario.getSenha() != null) {
            update.setSenha(novoUsuario.getSenha());
        }
        if (novoUsuario.getRole() != null) {
            update.setRole(novoUsuario.getRole());
        }

        return usuarioRepository.save(update);
    }

    public void delete(Integer id) {
        Usuario delete = findById(id);
        usuarioRepository.delete(delete);
    }

    public String cadastrar(Usuario usuario) {
        return saveUsuario(usuario);
    }

    public String login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new GenericExceptions.Unauthorized("E-mail ou senha inválidos."));

        if (!usuario.getSenha().equals(senha)) {
            throw new GenericExceptions.Unauthorized("E-mail ou senha inválidos.");
        }

        if (jwtService != null) {
            return jwtService.generateToken(usuario);
        }

        return "Login efetuado com sucesso";
    }
}