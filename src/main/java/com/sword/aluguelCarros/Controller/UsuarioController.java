package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService carroService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Usuario>> findAll() {
        try {
            var result = usuarioService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Integer id) {
        try {
            var result = usuarioService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Usuario> save(
            @RequestBody Usuario usuario) {
        try {
            var result = usuarioService.save(usuario);
            return new ResponseEntity<>(result,
                    HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(null,
                    HttpStatus.BAD_REQUEST);
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

}
