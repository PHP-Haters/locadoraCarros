package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Service.AluguelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {

    @Autowired
    private AluguelService aluguelService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Aluguel>> findAll() {
        var result = aluguelService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Aluguel> findById(@PathVariable Integer id) {
        var result = aluguelService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // O retorno de saveAluguel no AluguelService é String
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Aluguel aluguel) {
        String result = aluguelService.saveAluguel(aluguel);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Aluguel> update(@PathVariable Integer id, @RequestBody Aluguel aluguelUpdate) {
        var result = aluguelService.update(id, aluguelUpdate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        aluguelService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Aluguel>> findByUsuarioId(@PathVariable Integer usuarioId) {
        var result = aluguelService.findByUsuarioId(usuarioId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
