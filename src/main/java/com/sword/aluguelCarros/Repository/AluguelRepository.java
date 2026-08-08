package com.sword.aluguelCarros.Repository;

import com.sword.aluguelCarros.Model.Aluguel;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class AluguelRepository {
    // Estrutura em memória
    private final List<Aluguel> alugueis = new ArrayList<>();

    // Gerador de IDs
    private final AtomicInteger atomicInteger = new AtomicInteger(10);

    // Obtém a lista imutável de aluguéis
    public List<Aluguel> getAlugueis() {
        return Collections.unmodifiableList(alugueis);
    }

    // Busca um aluguel por ID
    public Aluguel getAluguel(Integer id) {
        for (Aluguel aluguel : alugueis) {
            if (aluguel.getId().equals(id)) {
                return aluguel;
            }
        }
        return null;
    }

    // Inicializa com dados de exemplo
    @PostConstruct
    public void init() {
        Aluguel aluguel = new Aluguel();
        aluguel.setId(1);
        aluguel.setUsuarioId(1);
        aluguel.setCarroId(1);
        aluguel.setDataInicio(LocalDate.now());
        aluguel.setDataFinal(LocalDate.now().plusDays(5));
        aluguel.setValorTotal(500.0);

        alugueis.add(aluguel);
    }

    // Salva um novo aluguel
    public Aluguel save(Aluguel aluguel) {
        atomicInteger.incrementAndGet();
        aluguel.setId(atomicInteger.get());
        alugueis.add(aluguel);
        return aluguel;
    }

    // Remove um aluguel por ID
    public void delete(Integer id) {
        for (Aluguel aluguel : alugueis) {
            if (aluguel.getId().equals(id)) {
                alugueis.remove(aluguel);
                return;
            }
        }
    }

    // Atualiza os dados de um aluguel
    public Aluguel update(Integer id, Aluguel aluguelUpdate) {
        for (Aluguel aluguel : alugueis) {
            if (aluguel.getId().equals(id)) {
                if (aluguelUpdate.getUsuarioId() != null) {
                    aluguel.setUsuarioId(aluguelUpdate.getUsuarioId());
                }
                if (aluguelUpdate.getCarroId() != null) {
                    aluguel.setCarroId(aluguelUpdate.getCarroId());
                }
                if (aluguelUpdate.getDataInicio() != null) {
                    aluguel.setDataInicio(aluguelUpdate.getDataInicio());
                }
                if (aluguelUpdate.getDataFinal() != null) {
                    aluguel.setDataFinal(aluguelUpdate.getDataFinal());
                }
                if (aluguelUpdate.getValorTotal() != null) {
                    aluguel.setValorTotal(aluguelUpdate.getValorTotal());
                }
                return aluguel;
            }
        }
        return null;
    }
}
