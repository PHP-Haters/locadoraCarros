package com.sword.aluguelCarros.Repository;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import com.sword.aluguelCarros.Model.Carro;
import org.springframework.stereotype.Repository;

public class CarroRepository {
    // Estrutura que serve como um banco de dados em memória
    private final List<Carro> carros = new ArrayList<>();

    // Estrutura para gerar id's
    private final AtomicInteger atomicInteger = new AtomicInteger(10);

    // obtem a lista imutável de carros
    public List<Carro> getCarros() {
        return Collections.unmodifiableList(carros);
    }

    public Carro getCarro(Integer id) {
        // itera na lista de carros e verifica
        // se existe carro com o id especificado
        for (Carro carro : carros) {
            if (carro.getId().equals(id)) {
                return carro;
            }
        }
        return null;
    }

    // metodo que inicia o banco com dados
    @PostConstruct
    public void init() {
        Carro carro = new Carro();
        carro.setId(1);
        carro.setNome("Siena");
        carro.setMarca("Fiat");

        carros.add(carro);
    }

    public Carro save(Carro carro) {
        // incrementa +1 no id e obtem o valor
        atomicInteger.incrementAndGet();
        carro.setId(atomicInteger.get());
        carros.add(carro);
        return carro;
    }

    public void delete(Integer id) {
        for (Carro carro : carros) {
            if (carro.getId().equals(id)) {
                carros.remove(carro);
                return; // sair do loop
            }
        }
    }

    public Carro update(Integer id, Carro carroUpdate) {
        for (Carro carro : carros) {
            if (carro.getId().equals(id)) {
                // nome e marca podem correr risco de ser null
                // interessante validar antes de fazer o SET
                carro.setNome(carroUpdate.getNome());
                carro.setMarca(carroUpdate.getMarca());
                return carro;
            }
        }
        return null;
    }
}
