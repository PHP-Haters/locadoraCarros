package com.sword.aluguelCarros.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sword.aluguelCarros.Model.Aluguel;
import com.sword.aluguelCarros.Service.AluguelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AluguelControllerTest {

    @Mock
    private AluguelService aluguelService;

    @InjectMocks
    private AluguelController aluguelController;

    @Test
    void deveSalvarAluguel() {

        Aluguel aluguel = new Aluguel();

        when(aluguelService.saveAluguel(aluguel))
                .thenReturn("Aluguel salvo com sucesso.");

        ResponseEntity<String> response =
                aluguelController.saveAluguel(aluguel);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Aluguel salvo com sucesso.", response.getBody());

        verify(aluguelService).saveAluguel(aluguel);
    }

    @Test
    void deveListarTodosOsAlugueis() {

        Aluguel aluguel1 = new Aluguel();
        Aluguel aluguel2 = new Aluguel();

        List<Aluguel> alugueis = List.of(aluguel1, aluguel2);

        when(aluguelService.findAll())
                .thenReturn(alugueis);

        ResponseEntity<List<Aluguel>> response =
                aluguelController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());

        verify(aluguelService).findAll();
    }

    @Test
    void deveBuscarAluguelPorId() {

        Integer id = 1;
        Aluguel aluguel = new Aluguel();

        when(aluguelService.findById(id))
                .thenReturn(aluguel);

        ResponseEntity<Aluguel> response =
                aluguelController.findById(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(aluguel, response.getBody());

        verify(aluguelService).findById(id);
    }

    @Test
    void deveBuscarAlugueisPorUsuario() {

        Integer usuarioId = 1;

        Aluguel aluguel1 = new Aluguel();
        Aluguel aluguel2 = new Aluguel();

        when(aluguelService.findByUsuarioId(usuarioId))
                .thenReturn(List.of(aluguel1, aluguel2));

        ResponseEntity<List<Aluguel>> response =
                aluguelController.findByUsuarioId(usuarioId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());

        verify(aluguelService).findByUsuarioId(usuarioId);
    }

    @Test
    void deveAtualizarAluguel() {

        Integer id = 1;
        Aluguel aluguel = new Aluguel();

        ResponseEntity<String> response =
                aluguelController.update(id, aluguel);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(
                "Aluguel atualizado com sucesso.",
                response.getBody()
        );

        verify(aluguelService).update(id, aluguel);
    }

    @Test
    void deveDeletarAluguel() {

        Integer id = 1;

        doNothing().when(aluguelService)
                .delete(id);

        ResponseEntity<?> response =
                aluguelController.delete(id);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(aluguelService).delete(id);
    }
}