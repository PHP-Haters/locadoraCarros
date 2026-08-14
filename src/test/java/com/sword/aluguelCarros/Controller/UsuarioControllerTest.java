package com.sword.aluguelCarros.Controller;

import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void deveCadastrarUsuario() {

        Usuario usuario = new Usuario();

        when(usuarioService.cadastrar(usuario))
                .thenReturn("Usuário cadastrado com sucesso.");

        ResponseEntity<String> response =
                usuarioController.cadastro(usuario);

        assertEquals(201, response.getStatusCode().value());
        assertEquals(
                "Usuário cadastrado com sucesso.",
                response.getBody()
        );

        verify(usuarioService).cadastrar(usuario);
    }

    @Test
    void deveRealizarLogin() {

        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("123456");

        when(usuarioService.login(
                "teste@email.com",
                "123456"
        )).thenReturn("token-jwt");

        ResponseEntity<Map<String, String>> response =
                usuarioController.login(usuario);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(
                "token-jwt",
                response.getBody().get("token")
        );

        verify(usuarioService)
                .login("teste@email.com", "123456");
    }

    @Test
    void deveListarTodosOsUsuarios() {

        Usuario usuario1 = new Usuario();
        Usuario usuario2 = new Usuario();

        when(usuarioService.findAll())
                .thenReturn(List.of(usuario1, usuario2));

        ResponseEntity<List<Usuario>> response =
                usuarioController.findAll();

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        verify(usuarioService).findAll();
    }

    @Test
    void deveBuscarUsuarioPorId() {

        Integer id = 1;
        Usuario usuario = new Usuario();

        when(usuarioService.findById(id))
                .thenReturn(usuario);

        ResponseEntity<Usuario> response =
                usuarioController.findById(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuario, response.getBody());

        verify(usuarioService).findById(id);
    }

    @Test
    void deveBuscarUsuarioPorEmail() {

        String email = "teste@email.com";
        Usuario usuario = new Usuario();

        when(usuarioService.findByEmail(email))
                .thenReturn(usuario);

        ResponseEntity<Usuario> response =
                usuarioController.findByEmail(email);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(usuario, response.getBody());

        verify(usuarioService).findByEmail(email);
    }

    @Test
    void deveAtualizarUsuario() {

        Integer id = 1;
        Usuario usuario = new Usuario();

        ResponseEntity<String> response =
                usuarioController.update(id, usuario);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(
                "Usuário atualizado com sucesso.",
                response.getBody()
        );

        verify(usuarioService).update(id, usuario);
    }

    @Test
    void deveDeletarUsuario() {

        Integer id = 1;

        doNothing().when(usuarioService)
                .delete(id);

        ResponseEntity<?> response =
                usuarioController.delete(id);

        assertEquals(204, response.getStatusCode().value());
        assertNull(response.getBody());

        verify(usuarioService).delete(id);
    }
}