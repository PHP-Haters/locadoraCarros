package com.sword.aluguelCarros.Repository;

import com.sword.aluguelCarros.Model.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Integer> {
    List<Carro> findByDisponivel(Boolean disponivel);
}
