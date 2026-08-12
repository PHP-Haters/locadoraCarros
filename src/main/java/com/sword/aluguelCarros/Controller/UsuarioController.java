package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Usuario>> findAll() {
        var result = usuarioService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Integer id) {
        var result = usuarioService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findByEmail")
    public ResponseEntity<Usuario> findByEmail(@RequestParam String email) {
        var result = usuarioService.findByEmail(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // O retorno de saveUsuario no UsuarioService é String
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Usuario usuario) {
        String result = usuarioService.saveUsuario(usuario);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    // Método de cadastro reutilizando a resposta String
    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastro(@RequestBody Usuario usuario) {
        String result = usuarioService.cadastrar(usuario);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Usuario> update(@PathVariable Integer id, @RequestBody Usuario usuarioUpdate) {
        var result = usuarioService.update(id, usuarioUpdate);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        usuarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Usuario usuario) {
        String tokenOuMensagem = usuarioService.login(usuario.getEmail(), usuario.getSenha());
        return ResponseEntity.ok(Map.of("token", tokenOuMensagem));
    }

    @GetMapping("/teste")
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("Backend conectado com sucesso!");
    }
}
