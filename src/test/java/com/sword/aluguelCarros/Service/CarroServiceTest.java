package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.ExceptionHandlers.GenericExceptions;
import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Repository.CarroRepository;
import com.sword.aluguelCarros.Service.CarroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarroServiceTest {

    @Mock
    private CarroRepository carroRepository;

    @InjectMocks
    private CarroService carroService;

    private Carro carroPadrao;

    // O @BeforeEach roda antes de cada teste, preparando os dados
    @BeforeEach
    void setUp() {
        carroPadrao = new Carro();
        carroPadrao.setId(1);
        carroPadrao.setNome("Civic");
        carroPadrao.setMarca("Honda");
        carroPadrao.setValorDiaria(150.0f);
        carroPadrao.setDisponivel(true);
    }

    // --- Testes para o método saveCarro ---

    @Test
    void deveSalvarCarroComSucessoEDefinirDisponibilidade() {
        // Preparação
        carroPadrao.setDisponivel(null); // Forçando nulo para testar o IF da linha 20
        when(carroRepository.save(any(Carro.class))).thenReturn(carroPadrao);

        // Ação
        String resultado = carroService.saveCarro(carroPadrao);

        // Verificação
        assertEquals("Carro salvo com sucesso", resultado);
        assertTrue(carroPadrao.getDisponivel()); // Verifica se o service alterou para true
        verify(carroRepository, times(1)).save(carroPadrao);
    }

    @Test
    void deveLancarInvalidDataQuandoOcorrerDataIntegrityViolationException() {
        // Preparação: O mock agora lança uma exceção quando tentamos salvar
        when(carroRepository.save(any(Carro.class)))
                .thenThrow(new DataIntegrityViolationException("Erro de constraint no banco"));

        // Ação & Verificação usando assertThrows
        assertThrows(GenericExceptions.InvalidData.class, () -> {
            carroService.saveCarro(carroPadrao);
        });
    }

    // --- Testes para o método findById ---

    @Test
    void deveEncontrarCarroPorId() {
        when(carroRepository.findById(1)).thenReturn(Optional.of(carroPadrao));

        Carro resultado = carroService.findById(1);

        assertNotNull(resultado);
        assertEquals("Civic", resultado.getNome());
        verify(carroRepository, times(1)).findById(1);
    }

    @Test
    void deveLancarNotFoundQuandoCarroNaoExistir() {
        // Simulando que o banco não achou nada
        when(carroRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(GenericExceptions.NotFound.class, () -> {
            carroService.findById(2);
        });
    }

    // --- Testes para o método findAll ---

    @Test
    void deveRetornarListaDeCarros() {
        when(carroRepository.findAll()).thenReturn(List.of(carroPadrao));

        List<Carro> resultado = carroService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveLancarExcecaoGeneralQuandoListaDeCarrosEstiverVazia() {
        when(carroRepository.findAll()).thenReturn(new ArrayList<>()); // Lista vazia

        assertThrows(GenericExceptions.General.class, () -> {
            carroService.findAll();
        });
    }

    // --- Testes para o método update ---

    @Test
    void deveAtualizarApenasCamposPreenchidos() {
        // Preparação: O carro que vem na requisição com apenas o nome alterado
        Carro carroAtualizado = new Carro();
        carroAtualizado.setNome("Corolla");
        // Marca, Valor e Disponivel estão nulos

        when(carroRepository.findById(1)).thenReturn(Optional.of(carroPadrao));
        when(carroRepository.save(any(Carro.class))).thenReturn(carroPadrao);

        // Ação
        Carro resultado = carroService.update(1, carroAtualizado);

        // Verificação: O nome deve mudar, mas a marca deve continuar "Honda"
        assertEquals("Corolla", resultado.getNome());
        assertEquals("Honda", resultado.getMarca());
        verify(carroRepository, times(1)).save(carroPadrao);
    }

    // --- Testes para o método delete ---

    @Test
    void deveDeletarCarroComSucesso() {
        // Preparação: O findById vai ser chamado dentro do delete, então precisamos mocká-lo
        when(carroRepository.findById(1)).thenReturn(Optional.of(carroPadrao));
        doNothing().when(carroRepository).delete(carroPadrao);

        // Ação
        carroService.delete(1);

        // Verificação
        verify(carroRepository, times(1)).delete(carroPadrao);
    }

    // --- Testes para o método findByDisponiveis ---

    @Test
    void deveRetornarApenasCarrosDisponiveis() {
        when(carroRepository.findByDisponivel(true)).thenReturn(List.of(carroPadrao));

        List<Carro> resultado = carroService.findByDisponiveis();

        assertFalse(resultado.isEmpty());
        verify(carroRepository, times(1)).findByDisponivel(true);
    }

    @Test
    void deveLancarNotFoundQuandoNaoHouverCarrosDisponiveis() {
        when(carroRepository.findByDisponivel(true)).thenReturn(new ArrayList<>());

        assertThrows(GenericExceptions.NotFound.class, () -> {
            carroService.findByDisponiveis();
        });
    }
}