package com.sword.aluguelCarros.Service;

import com.sword.aluguelCarros.ExceptionHandlers.GenericExceptions;
import com.sword.aluguelCarros.Model.Enum.UserRole;
import com.sword.aluguelCarros.Model.Usuario;
import com.sword.aluguelCarros.Repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private JwtService jwtService; // Mockamos o JwtService que você acabou de criar!

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioPadrao;

    @BeforeEach
    void setUp() {
        usuarioPadrao = new Usuario();
        usuarioPadrao.setId(1);
        usuarioPadrao.setNome("Teste da Silva");
        usuarioPadrao.setEmail("teste@email.com");
        usuarioPadrao.setSenha("senha123");
        usuarioPadrao.setRole(UserRole.USER);
    }

    // --- Testes para o método saveUsuario ---

    @Test
    void deveSalvarUsuarioEAtribuirRolePadraoSeForNulo() {
        // Preparação
        usuarioPadrao.setRole(null); // Forçamos nulo para testar o IF
        when(usuarioRepository.existsByEmail("teste@email.com")).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPadrao);

        // Ação
        String resultado = usuarioService.saveUsuario(usuarioPadrao);

        // Verificação
        assertEquals("Usuário salvo com sucesso", resultado);
        assertEquals(UserRole.USER, usuarioPadrao.getRole()); // Verifica se o service colocou a Role correta
        verify(usuarioRepository, times(1)).save(usuarioPadrao);
    }

    @Test
    void deveLancarAlreadyExistsSeEmailJaEstiverCadastrado() {
        // Simulamos que o banco disse que o e-mail JÁ existe
        when(usuarioRepository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThrows(GenericExceptions.AlreadyExists.class, () -> {
            usuarioService.saveUsuario(usuarioPadrao);
        });

        // Garante que o save() nunca foi chamado
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    // --- Testes para os métodos de Busca ---

    @Test
    void deveRetornarUsuarioPorId() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioPadrao));

        Usuario resultado = usuarioService.findById(1);

        assertNotNull(resultado);
        assertEquals("Teste da Silva", resultado.getNome());
    }

    @Test
    void deveRetornarUsuarioPorEmail() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuarioPadrao));

        Usuario resultado = usuarioService.findByEmail("teste@email.com");

        assertNotNull(resultado);
        assertEquals(1, resultado.getId());
    }

    @Test
    void deveLancarNotFoundQuandoUsuarioNaoExistir() {
        when(usuarioRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(GenericExceptions.NotFound.class, () -> {
            usuarioService.findById(99);
        });
    }

    @Test
    void deveRetornarListaDeUsuarios() {
        when(usuarioRepository.findAll()).thenReturn(List.of(usuarioPadrao));

        List<Usuario> resultado = usuarioService.findAll();

        assertFalse(resultado.isEmpty());
    }

    @Test
    void deveLancarExcecaoGeneralQuandoListaDeUsuariosVazia() {
        when(usuarioRepository.findAll()).thenReturn(new ArrayList<>());

        assertThrows(GenericExceptions.General.class, () -> {
            usuarioService.findAll();
        });
    }

    // --- Testes para o método update ---

    @Test
    void deveAtualizarUsuarioCorretamente() {
        Usuario novosDados = new Usuario();
        novosDados.setNome("Nome Atualizado");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioPadrao));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioPadrao);

        Usuario resultado = usuarioService.update(1, novosDados);

        assertEquals("Nome Atualizado", resultado.getNome());
        assertEquals("teste@email.com", resultado.getEmail()); // E-mail deve continuar o mesmo
    }

    @Test
    void deveLancarAlreadyExistsSeTentarAtualizarParaEmailQueJaExiste() {
        Usuario novosDados = new Usuario();
        novosDados.setEmail("novoemail@email.com");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioPadrao));
        // Simulamos que o novo e-mail já pertence a outra pessoa
        when(usuarioRepository.existsByEmail("novoemail@email.com")).thenReturn(true);

        assertThrows(GenericExceptions.AlreadyExists.class, () -> {
            usuarioService.update(1, novosDados);
        });
    }

    // --- Testes para o método delete ---

    @Test
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioPadrao));
        doNothing().when(usuarioRepository).delete(usuarioPadrao);

        usuarioService.delete(1);

        verify(usuarioRepository, times(1)).delete(usuarioPadrao);
    }

    // --- Testes para o método login ---

    @Test
    void deveFazerLoginComSucessoEGerarToken() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuarioPadrao));
        when(jwtService.generateToken(usuarioPadrao)).thenReturn("token.jwt.falso");

        String resultado = usuarioService.login("teste@email.com", "senha123");

        assertEquals("token.jwt.falso", resultado);
        verify(jwtService, times(1)).generateToken(usuarioPadrao);
    }

    @Test
    void deveLancarUnauthorizedSeEmailEstiverIncorreto() {
        when(usuarioRepository.findByEmail("email_errado@email.com")).thenReturn(Optional.empty());

        GenericExceptions.Unauthorized exception = assertThrows(GenericExceptions.Unauthorized.class, () -> {
            usuarioService.login("email_errado@email.com", "senha123");
        });

        assertEquals("E-mail ou senha inválidos.", exception.getMessage());
    }

    @Test
    void deveLancarUnauthorizedSeSenhaEstiverIncorreta() {
        when(usuarioRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuarioPadrao));

        GenericExceptions.Unauthorized exception = assertThrows(GenericExceptions.Unauthorized.class, () -> {
            usuarioService.login("teste@email.com", "senha_errada");
        });

        assertEquals("E-mail ou senha inválidos.", exception.getMessage());
    }
}