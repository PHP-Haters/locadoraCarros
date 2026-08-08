package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Service.AluguelService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/alugueis")
public class AluguelController {
    private final AluguelService aluguelService;

    public AluguelController(AluguelService aluguelService) {
        this.aluguelService = aluguelService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Aluguel>> findAll() {
        try {
            var result = aluguelService.findAll();
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/findById/{id}")
    public ResponseEntity<Aluguel> findById(@PathVariable Integer id) {
        try {
            var result = aluguelService.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Aluguel> save(@RequestBody Aluguel aluguel) {
        try {
            var result = aluguelService.save(aluguel);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            aluguelService.delete(id);
            return ResponseEntity.noContent().build(); // status 204
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build(); // status 400
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Aluguel> update(@PathVariable Integer id, @RequestBody Aluguel aluguelUpdate) {
        try {
            var result = aluguelService.update(id, aluguelUpdate);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }
}
