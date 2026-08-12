package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.ExceptionHandlers.GenericExceptions;
import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Repository.AluguelRepository;
import com.sword.aluguelCarros.Repository.CarroRepository;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private CarroRepository carroRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private AluguelService aluguelService;

    private Aluguel aluguelPadrao;
    private Carro carroPadrao;

    @BeforeEach
    void setUp() {
        // Preparando um Carro padrão para os testes
        carroPadrao = new Carro();
        carroPadrao.setId(10);
        carroPadrao.setDisponivel(true);
        carroPadrao.setValorDiaria(150.0f); // Colocando 'f' para evitar o erro de Double vs Float

        // Preparando um Aluguel padrão para os testes
        aluguelPadrao = new Aluguel();
        aluguelPadrao.setId(1);
        aluguelPadrao.setUsuarioId(5);
        aluguelPadrao.setCarroId(10);
        aluguelPadrao.setDataInicio(LocalDate.now());
        aluguelPadrao.setDataFinal(LocalDate.now().plusDays(2)); // Aluguel de 2 dias
    }

    // --- Testes para o método saveAluguel ---

    @Test
    void deveSalvarAluguelComSucessoECalcularValorTotal() {
        // Preparação
        when(usuarioRepository.existsById(5)).thenReturn(true);
        when(carroRepository.findById(10)).thenReturn(Optional.of(carroPadrao));

        // Ação
        String resultado = aluguelService.saveAluguel(aluguelPadrao);

        // Verificação
        assertEquals("Aluguel realizado com sucesso", resultado);
        assertEquals(300.0, aluguelPadrao.getValorTotal()); // 2 dias * 150.0
        assertFalse(carroPadrao.getDisponivel()); // O carro deve ficar indisponível

        // Verifica se o carro e o aluguel foram salvos no banco
        verify(carroRepository, times(1)).save(carroPadrao);
        verify(aluguelRepository, times(1)).save(aluguelPadrao);
    }

    @Test
    void deveLancarExcecaoSeUsuarioNaoExistir() {
        // Simulando que o usuário não existe no banco
        when(usuarioRepository.existsById(5)).thenReturn(false);

        GenericExceptions.InvalidData exception = assertThrows(GenericExceptions.InvalidData.class, () -> {
            aluguelService.saveAluguel(aluguelPadrao);
        });

        assertEquals("Usuário informado não existe.", exception.getMessage());
        verify(aluguelRepository, never()).save(any()); // Garante que não tentou salvar
    }

    @Test
    void deveLancarExcecaoSeCarroNaoEstiverDisponivel() {
        carroPadrao.setDisponivel(false); // Carro já está alugado

        when(usuarioRepository.existsById(5)).thenReturn(true);
        when(carroRepository.findById(10)).thenReturn(Optional.of(carroPadrao));

        GenericExceptions.InvalidData exception = assertThrows(GenericExceptions.InvalidData.class, () -> {
            aluguelService.saveAluguel(aluguelPadrao);
        });

        assertEquals("Este carro não está disponível para aluguel.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSeDataFinalForAnteriorADataInicio() {
        // Colocando a data final antes da data de início
        aluguelPadrao.setDataFinal(LocalDate.now().minusDays(1));

        when(usuarioRepository.existsById(5)).thenReturn(true);
        when(carroRepository.findById(10)).thenReturn(Optional.of(carroPadrao));

        GenericExceptions.InvalidData exception = assertThrows(GenericExceptions.InvalidData.class, () -> {
            aluguelService.saveAluguel(aluguelPadrao);
        });

        assertEquals("A data final deve ser posterior à data de início.", exception.getMessage());
    }

    // --- Testes para o método findAll ---

    @Test
    void deveRetornarListaDeAlugueis() {
        when(aluguelRepository.findAll()).thenReturn(List.of(aluguelPadrao));

        List<Aluguel> resultado = aluguelService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    // --- Testes para o método update ---

    @Test
    void deveAtualizarAluguelERecalcularValor() {
        // Aluguel que vem na requisição com novas datas (agora 4 dias)
        Aluguel novosDados = new Aluguel();
        novosDados.setDataInicio(LocalDate.now());
        novosDados.setDataFinal(LocalDate.now().plusDays(4));

        when(aluguelRepository.findById(1)).thenReturn(Optional.of(aluguelPadrao));
        when(carroRepository.findById(10)).thenReturn(Optional.of(carroPadrao));
        when(aluguelRepository.save(any(Aluguel.class))).thenReturn(aluguelPadrao);

        // Ação
        Aluguel resultado = aluguelService.update(1, novosDados);

        // Verificação: 4 dias * 150 = 600
        assertEquals(600.0, resultado.getValorTotal());
        verify(aluguelRepository, times(1)).save(aluguelPadrao);
    }

    // --- Testes para o método delete ---

    @Test
    void deveDeletarAluguelETornarCarroDisponivelNovamente() {
        // Quando o delete é chamado, o carro do aluguel (id 10) deve voltar a ficar disponível
        carroPadrao.setDisponivel(false);

        when(aluguelRepository.findById(1)).thenReturn(Optional.of(aluguelPadrao));
        when(carroRepository.findById(10)).thenReturn(Optional.of(carroPadrao));
        doNothing().when(aluguelRepository).delete(aluguelPadrao);

        // Ação
        aluguelService.delete(1);

        // Verificação
        assertTrue(carroPadrao.getDisponivel()); // Carro voltou a ficar livre
        verify(carroRepository, times(1)).save(carroPadrao); // Salvou o status do carro
        verify(aluguelRepository, times(1)).delete(aluguelPadrao); // Deletou o aluguel
    }

    // --- Testes para o método findByUsuarioId ---

    @Test
    void deveRetornarAlugueisDoUsuario() {
        when(aluguelRepository.findByUsuarioId(5)).thenReturn(List.of(aluguelPadrao));

        List<Aluguel> resultado = aluguelService.findByUsuarioId(5);

        assertFalse(resultado.isEmpty());
        assertEquals(5, resultado.get(0).getUsuarioId());
    }

    @Test
    void deveLancarExcecaoSeNaoEncontrarAlugueisParaUsuario() {
        when(aluguelRepository.findByUsuarioId(99)).thenReturn(new ArrayList<>());

        assertThrows(GenericExceptions.NotFound.class, () -> {
            aluguelService.findByUsuarioId(99);
        });
    }
}