package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.ExceptionHandlers.GenericExceptions;
import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Repository.AluguelRepository;
import com.sword.aluguelCarros.Repository.CarroRepository;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AluguelService {

    @Autowired
    private AluguelRepository aluguelRepository;

    @Autowired
    private CarroRepository carroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String saveAluguel(Aluguel novoAluguel) {
        try {
            // 1. Validações de existência e disponibilidade
            if (!usuarioRepository.existsById(novoAluguel.getUsuarioId())) {
                throw new GenericExceptions.InvalidData("Usuário informado não existe.");
            }

            Carro carro = carroRepository.findById(novoAluguel.getCarroId())
                    .orElseThrow(() -> new GenericExceptions.NotFound("Carro informado não existe."));

            if (Boolean.FALSE.equals(carro.getDisponivel())) {
                throw new GenericExceptions.InvalidData("Este carro não está disponível para aluguel.");
            }

            // 2. Validação de datas
            if (novoAluguel.getDataInicio() == null || novoAluguel.getDataFinal() == null) {
                throw new GenericExceptions.InvalidData("As datas de início e fim são obrigatórias.");
            }
            if (novoAluguel.getDataFinal().isBefore(novoAluguel.getDataInicio())) {
                throw new GenericExceptions.InvalidData("A data final deve ser posterior à data de início.");
            }

            // 3. Cálculo do valor total
            long dias = ChronoUnit.DAYS.between(novoAluguel.getDataInicio(), novoAluguel.getDataFinal());
            if (dias == 0) dias = 1;

            double precoDiaria = (carro.getValorDiaria() != null) ? carro.getValorDiaria() : 100.0;
            novoAluguel.setValorTotal(dias * precoDiaria);

            // 4. Atualiza o status do carro e salva o aluguel
            carro.setDisponivel(false);
            carroRepository.save(carro);

            aluguelRepository.save(novoAluguel);
            return "Aluguel realizado com sucesso";

        } catch (GenericExceptions.InvalidData | GenericExceptions.NotFound ex) {
            throw ex;
        } catch (DataIntegrityViolationException ex) {
            throw new GenericExceptions.InvalidData(
                    "Dados inválidos para o aluguel: " + ex.getMessage()
            );
        } catch (Exception ex) {
            throw new GenericExceptions.General(
                    "Erro inesperado ao salvar o aluguel: " + ex.getMessage()
            );
        }
    }

    public List<Aluguel> findAll() {
        List<Aluguel> alugueis = aluguelRepository.findAll();
        if (alugueis.isEmpty()) {
            throw new GenericExceptions.General(
                    "Não existem aluguéis cadastrados."
            );
        } else {
            return alugueis;
        }
    }

    public Aluguel findById(Integer id) {
        return aluguelRepository.findById(id)
                .orElseThrow(() -> new GenericExceptions.NotFound("Aluguel não encontrado."));
    }

    public Aluguel update(Integer id, Aluguel novoAluguel) {
        Aluguel update = findById(id);

        if (novoAluguel.getDataInicio() != null) {
            update.setDataInicio(novoAluguel.getDataInicio());
        }
        if (novoAluguel.getDataFinal() != null) {
            update.setDataFinal(novoAluguel.getDataFinal());
        }
        if (novoAluguel.getUsuarioId() != null) {
            update.setUsuarioId(novoAluguel.getUsuarioId());
        }
        if (novoAluguel.getCarroId() != null) {
            update.setCarroId(novoAluguel.getCarroId());
        }

        // Recalcula o valor total se as datas forem válidas
        if (update.getDataInicio() != null && update.getDataFinal() != null) {
            long dias = ChronoUnit.DAYS.between(update.getDataInicio(), update.getDataFinal());
            if (dias == 0) dias = 1;

            Carro carro = carroRepository.findById(update.getCarroId()).orElse(null);
            double precoDiaria = (carro != null && carro.getValorDiaria() != null) ? carro.getValorDiaria() : 100.0;
            update.setValorTotal(dias * precoDiaria);
        }

        return aluguelRepository.save(update);
    }

    public void delete(Integer id) {
        Aluguel aluguel = findById(id);

        // Devolve a disponibilidade do carro ao cancelar/deletar o aluguel
        carroRepository.findById(aluguel.getCarroId()).ifPresent(carro -> {
            carro.setDisponivel(true);
            carroRepository.save(carro);
        });

        aluguelRepository.delete(aluguel);
    }

    public List<Aluguel> findByUsuarioId(Integer usuarioId) {
        List<Aluguel> alugueis = aluguelRepository.findByUsuarioId(usuarioId);
        if (alugueis.isEmpty()) {
            throw new GenericExceptions.NotFound("Nenhum aluguel encontrado para o usuário informado.");
        }
        return alugueis;
    }
}
