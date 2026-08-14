package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Service.CarroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SuppressWarnings("unused")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/carro")
public class CarroController {

    @Autowired
    private CarroService carroService;

    // Criar um novo carro
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> saveCarro(@RequestBody @Valid Carro carro) {
        String response = carroService.saveCarro(carro);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todos os carros
    @GetMapping
    public ResponseEntity<List<Carro>> findAll() {
        List<Carro> result = carroService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Buscar carro por ID
    @GetMapping("/{id}")
    public ResponseEntity<Carro> findById(@PathVariable Integer id) {
        Carro result = carroService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Listar carros disponíveis
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Carro>> findByDisponiveis() {
        List<Carro> result = carroService.findByDisponiveis();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // Atualizar carro
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Integer id, @RequestBody @Valid Carro carroAtualizado) {
        carroService.update(id, carroAtualizado);
        return new ResponseEntity<>("Carro atualizado com sucesso.", HttpStatus.OK);
    }

    // Deletar carro
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        carroService.delete(id);
        return ResponseEntity.noContent().build(); // status 204
    }
}