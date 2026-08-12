package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Service.AluguelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/aluguel")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    // Criar um novo aluguel
    @PostMapping
    public ResponseEntity<String> saveAluguel(@RequestBody @Valid Aluguel aluguel) {
        String response = aluguelService.saveAluguel(aluguel);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todos os aluguéis
    @GetMapping
    public ResponseEntity<List<Aluguel>> findAll() {
        List<Aluguel> result = aluguelService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Buscar aluguel por ID
    @GetMapping("/{id}")
    public ResponseEntity<Aluguel> findById(@PathVariable Integer id) {
        Aluguel result = aluguelService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Listar todos os aluguéis de um usuário
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Aluguel>> findByUsuarioId(@PathVariable Integer usuarioId) {
        List<Aluguel> result = aluguelService.findByUsuarioId(usuarioId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Atualizar aluguel
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody @Valid Aluguel aluguelAtualizado) {
        aluguelService.update(id, aluguelAtualizado);
        return new ResponseEntity<>("Aluguel atualizado com sucesso.", HttpStatus.OK);
    }

    // Deletar aluguel
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        aluguelService.delete(id);
        return ResponseEntity.noContent().build(); // status 204
    }
}