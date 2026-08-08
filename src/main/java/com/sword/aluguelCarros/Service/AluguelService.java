package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Repository.AluguelRepository;
import com.sword.aluguelCarros.Repository.CarroRepository;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AluguelService {
    private final AluguelRepository aluguelRepository;
    private final CarroRepository carroRepository;
    private final UsuarioRepository usuarioRepository;

    public AluguelService(AluguelRepository aluguelRepository,
                          CarroRepository carroRepository,
                          UsuarioRepository usuarioRepository) {
        this.aluguelRepository = aluguelRepository;
        this.carroRepository = carroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Aluguel> findAll() {
        return aluguelRepository.getAlugueis();
    }

    public Aluguel findById(Integer id) {
        return aluguelRepository.getAluguel(id);
    }

    public Aluguel save(Aluguel aluguel) {
        // 1. Valida se o Usuário existe
        if (usuarioRepository.getUsuario(aluguel.getUsuarioId()) == null) {
            throw new IllegalArgumentException("Usuário informado não existe.");
        }

        // 2. Valida se o Carro existe
        Carro carro = carroRepository.getCarro(aluguel.getCarroId());
        if (carro == null) {
            throw new IllegalArgumentException("Carro informado não existe.");
        }

        // 3. Valida se o Carro está disponível
        if (carro.getDisponivel() != null && !carro.getDisponivel()) {
            throw new IllegalArgumentException("Este carro não está disponível para aluguel.");
        }

        // 4. Valida as datas
        if (aluguel.getDataInicio() == null || aluguel.getDataFinal() == null) {
            throw new IllegalArgumentException("As datas de início e fim são obrigatórias.");
        }
        if (aluguel.getDataFinal().isBefore(aluguel.getDataInicio())) {
            throw new IllegalArgumentException("A data final deve ser posterior à data de início.");
        }

        // 5. Regra de Negócio: Calcula automaticamente o valor total
        long dias = ChronoUnit.DAYS.between(aluguel.getDataInicio(), aluguel.getDataFinal());
        if (dias == 0) dias = 1; // Mínimo de 1 diária

        double precoDiaria = (carro.getValorDiaria() != null) ? carro.getValorDiaria() : 100.0;
        aluguel.setValorTotal(dias * precoDiaria);

        // 6. Atualiza o status do carro para indisponível
        carro.setDisponivel(false);

        return aluguelRepository.save(aluguel);
    }

    public void delete(Integer id) {
        Aluguel aluguel = aluguelRepository.getAluguel(id);
        if (aluguel != null) {
            // Regra de Negócio: Liberar o carro quando o aluguel for cancelado/excluído
            Carro carro = carroRepository.getCarro(aluguel.getCarroId());
            if (carro != null) {
                carro.setDisponivel(true);
            }
            aluguelRepository.delete(id);
        }
    }

    public Aluguel update(Integer id, Aluguel aluguelUpdate) {
        return aluguelRepository.update(id, aluguelUpdate);
    }

}
