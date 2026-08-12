package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.ExceptionHandlers.GenericExceptions;
import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Repository.CarroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarroService {

    @Autowired
    private CarroRepository carroRepository;

    public String saveCarro(Carro novoCarro) {
        try {
            if (novoCarro.getDisponivel() == null) {
                novoCarro.setDisponivel(true);
            }

            carroRepository.save(novoCarro);
            return "Carro salvo com sucesso";
        }
        catch (DataIntegrityViolationException ex) {
            throw new GenericExceptions.InvalidData(
                    "Dados inválidos para o carro: " + ex.getMessage()
            );
        }
        catch (Exception ex) {
            throw new GenericExceptions.General(
                    "Erro inesperado ao salvar o carro: " + ex.getMessage()
            );
        }
    }

    public List<Carro> findAll() {
        List<Carro> carros = carroRepository.findAll();
        if (carros.isEmpty()) {
            throw new GenericExceptions.General(
                    "Não existem carros cadastrados."
            );
        } else {
            return carros;
        }
    }

    public Carro findById(Integer id) {
        return carroRepository.findById(id)
                .orElseThrow(() -> new GenericExceptions.NotFound("Carro não encontrado."));
    }

    public Carro update(Integer id, Carro novoCarro) {
        Carro update = findById(id);

        if (novoCarro.getNome() != null) {
            update.setNome(novoCarro.getNome());
        }
        if (novoCarro.getMarca() != null) {
            update.setMarca(novoCarro.getMarca());
        }
        if (novoCarro.getValorDiaria() != null) {
            update.setValorDiaria(novoCarro.getValorDiaria());
        }
        if (novoCarro.getDisponivel() != null) {
            update.setDisponivel(novoCarro.getDisponivel());
        }

        return carroRepository.save(update);
    }

    public void delete(Integer id) {
        Carro delete = findById(id);
        carroRepository.delete(delete);
    }

    public List<Carro> findByDisponiveis() {
        List<Carro> disponiveis = carroRepository.findByDisponivel(true);
        if (disponiveis.isEmpty()) {
            throw new GenericExceptions.NotFound("Nenhum carro disponível no momento.");
        }
        return disponiveis;
    }
}