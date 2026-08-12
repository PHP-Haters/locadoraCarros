package com.sword.aluguelCarros.Repository;

import com.sword.aluguelCarros.Model.Aluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Integer> {
    List<Aluguel> findByUsuarioId(Integer usuario_id);
}
