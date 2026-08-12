package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Service.CarroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/carros")
public class CarroController {

    @Autowired
    private CarroService carroService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Carro>> findAll() {
        var result = carroService.findAll();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Carro> findById(@PathVariable Integer id) {
        var result = carroService.findById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // O retorno de carroService.saveCarro é String
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Carro carro) {
        String result = carroService.saveCarro(carro);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        carroService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Carro> update(@PathVariable Integer id,
                                        @RequestBody Carro carroUpdate) {
        var result = carroService.update(id, carroUpdate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Carro>> findByDisponiveis() {
        var result = carroService.findByDisponiveis();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
