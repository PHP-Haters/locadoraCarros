package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Usuario>> findAll() {
        try {
            var result = usuarioService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Integer id) {
        try {
            var result = usuarioService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Usuario> save(@RequestBody Usuario usuario) {
        try {
            var result = usuarioService.save(usuario);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario usuarioUpdate) {
        try {
            var result = usuarioService.update(id, usuarioUpdate);
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.noContent().build(); // status 204
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build(); // status 400
        }
    }

    // cadastro
    @PostMapping("/cadastro")
    public ResponseEntity<Usuario> cadastro(
            @RequestBody Usuario usuario) {

        try {
            Usuario novoUsuario =
                    usuarioService.cadastrar(usuario);

            return new ResponseEntity<>(
                    novoUsuario,
                    HttpStatus.CREATED
            );

        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // logn
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Usuario usuario) {

        try {
            String token =
                    usuarioService.login(
                            usuario.getEmail(),
                            usuario.getSenha()
                    );

            return ResponseEntity.ok(
                    Map.of("token", token)
            );

        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
