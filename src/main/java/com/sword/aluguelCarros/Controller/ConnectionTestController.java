package com.sword.aluguelCarros.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/teste")
public class ConnectionTestController {
    // Endpoint de teste de conexão
    @GetMapping
    public ResponseEntity<String> teste() {
        return ResponseEntity.ok("O backend está rodando!");
    }
}
