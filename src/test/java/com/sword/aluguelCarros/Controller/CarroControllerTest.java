package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Carro;
import com.sword.aluguelCarros.Service.CarroService;
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
class CarroControllerTest {

    @Mock
    private CarroService carroService;

    @InjectMocks
    private CarroController carroController;

    @Test
    void deveSalvarCarro() {

        Carro carro = new Carro();

        when(carroService.saveCarro(carro))
                .thenReturn("Carro salvo com sucesso.");

        ResponseEntity<String> response =
                carroController.saveCarro(carro);

        assertEquals(201, response.getStatusCode().value());
        assertEquals("Carro salvo com sucesso.", response.getBody());

        verify(carroService).saveCarro(carro);
    }

    @Test
    void deveListarTodosOsCarros() {

        Carro carro1 = new Carro();
        Carro carro2 = new Carro();

        List<Carro> carros = List.of(carro1, carro2);

        when(carroService.findAll())
                .thenReturn(carros);

        ResponseEntity<List<Carro>> response =
                carroController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());

        verify(carroService).findAll();
    }

    @Test
    void deveBuscarCarroPorId() {

        Integer id = 1;
        Carro carro = new Carro();

        when(carroService.findById(id))
                .thenReturn(carro);

        ResponseEntity<Carro> response =
                carroController.findById(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(carro, response.getBody());

        verify(carroService).findById(id);
    }

    @Test
    void deveListarCarrosDisponiveis() {

        Carro carro1 = new Carro();
        Carro carro2 = new Carro();

        when(carroService.findByDisponiveis())
                .thenReturn(List.of(carro1, carro2));

        ResponseEntity<List<Carro>> response =
                carroController.findByDisponiveis();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());

        verify(carroService).findByDisponiveis();
    }

    @Test
    void deveAtualizarCarro() {

        Integer id = 1;
        Carro carro = new Carro();

        ResponseEntity<String> response =
                carroController.update(id, carro);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(
                "Carro atualizado com sucesso.",
                response.getBody()
        );

        verify(carroService).update(id, carro);
    }

    @Test
    void deveDeletarCarro() {

        Integer id = 1;

        doNothing().when(carroService)
                .delete(id);

        ResponseEntity<?> response =
                carroController.delete(id);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(carroService).delete(id);
    }
}