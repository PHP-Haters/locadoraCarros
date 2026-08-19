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

    
    @BeforeEach
    void setUp() {
        carroPadrao = new Carro();
        carroPadrao.setId(1);
        carroPadrao.setNome("Civic");
        carroPadrao.setMarca("Honda");
        carroPadrao.setValorDiaria(150.0f);
        carroPadrao.setDisponivel(true);
    }

    

    @Test
    void deveSalvarCarroComSucessoEDefinirDisponibilidade() {
        
        carroPadrao.setDisponivel(null); 
        when(carroRepository.save(any(Carro.class))).thenReturn(carroPadrao);

        
        String resultado = carroService.saveCarro(carroPadrao);

        
        assertEquals("Carro salvo com sucesso", resultado);
        assertTrue(carroPadrao.getDisponivel()); 
        verify(carroRepository, times(1)).save(carroPadrao);
    }

    @Test
    void deveLancarInvalidDataQuandoOcorrerDataIntegrityViolationException() {
        
        when(carroRepository.save(any(Carro.class)))
                .thenThrow(new DataIntegrityViolationException("Erro de constraint no banco"));

        
        assertThrows(GenericExceptions.InvalidData.class, () -> {
            carroService.saveCarro(carroPadrao);
        });
    }

    

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
        
        when(carroRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(GenericExceptions.NotFound.class, () -> {
            carroService.findById(2);
        });
    }

    

    @Test
    void deveRetornarListaDeCarros() {
        when(carroRepository.findAll()).thenReturn(List.of(carroPadrao));

        List<Carro> resultado = carroService.findAll();

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    void deveLancarExcecaoGeneralQuandoListaDeCarrosEstiverVazia() {
        when(carroRepository.findAll()).thenReturn(new ArrayList<>()); 

        assertThrows(GenericExceptions.NotFound.class, () -> {
            carroService.findAll();
        });
    }

    

    @Test
    void deveAtualizarApenasCamposPreenchidos() {
        
        Carro carroAtualizado = new Carro();
        carroAtualizado.setNome("Corolla");
        

        when(carroRepository.findById(1)).thenReturn(Optional.of(carroPadrao));
        when(carroRepository.save(any(Carro.class))).thenReturn(carroPadrao);

        
        Carro resultado = carroService.update(1, carroAtualizado);

        
        assertEquals("Corolla", resultado.getNome());
        assertEquals("Honda", resultado.getMarca());
        verify(carroRepository, times(1)).save(carroPadrao);
    }

    
    @Test
    void deveDeletarCarroComSucesso() {
        
        when(carroRepository.findById(1)).thenReturn(Optional.of(carroPadrao));
        doNothing().when(carroRepository).delete(carroPadrao);

        
        carroService.delete(1);

        
        verify(carroRepository, times(1)).delete(carroPadrao);
    }

    

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
