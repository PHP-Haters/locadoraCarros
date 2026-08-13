package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Cadastro de usuário
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody @Valid Usuario usuario) {
        String response = usuarioService.cadastrar(usuario);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Login de usuário
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Usuario usuario) {
        String tokenOuMensagem = usuarioService.login(usuario.getEmail(), usuario.getSenha());
        return ResponseEntity.ok(Map.of("token", tokenOuMensagem));
    }

    // Listar todos os usuários
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        List<Usuario> result = usuarioService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Buscar usuário por ID
    @PreAuthorize("@usuarioSecurity.podeAcessarUsuario(#id, authentication)")
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Integer id) {
        Usuario result = usuarioService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Buscar usuário por E-mail
    @PreAuthorize("@usuarioSecurity.podeAcessarUsuarioPorEmail(#email, authentication)")
    @GetMapping("/email")
    public ResponseEntity<Usuario> findByEmail(@RequestParam String email) {
        Usuario result = usuarioService.findByEmail(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Atualizar usuário
    @PreAuthorize("@usuarioSecurity.podeAcessarUsuario(#id, authentication)")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(
            @PathVariable Integer id,
            @RequestBody @Valid Usuario usuarioAtualizado
    ) {
        usuarioService.update(id, usuarioAtualizado);

        return new ResponseEntity<>(
                "Usuário atualizado com sucesso.",
                HttpStatus.OK
        );
    }

    // Deletar usuário
    @PreAuthorize("@usuarioSecurity.podeAcessarUsuario(#id, authentication)")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
