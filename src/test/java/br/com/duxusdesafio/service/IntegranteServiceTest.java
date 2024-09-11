package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.IntegranteDto;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullIntegranteException;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.repository.IntegranteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class IntegranteServiceTest {

    @InjectMocks
    private IntegranteService integranteService;

    @Mock
    private IntegranteRepository integranteRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarExcecaoQuandoListaDeIntegrantesForNula() {
        List<Integrante> integrantes = null;

        Assertions.assertThrows(NullIntegranteException.class, () -> {
            integranteService.validaIntegrantes(integrantes);
        });
    }

    @Test
    void deveCadastrarIntegranteComSucesso() throws IntegranteException {
        // Dados de entrada
        IntegranteDto integranteDto = new IntegranteDto();
        integranteDto.setFranquia("Franquia Exemplo");
        integranteDto.setNome("Nome Exemplo");
        integranteDto.setFuncao("Função Exemplo");

        // Mock do comportamento do repositório
        Integrante integranteSalvo = new Integrante();
        integranteSalvo.setId(1L);
        integranteSalvo.setFranquia(integranteDto.getFranquia());
        integranteSalvo.setNome(integranteDto.getNome());
        integranteSalvo.setFuncao(integranteDto.getFuncao());

        Mockito.when(integranteRepository.save(Mockito.any(Integrante.class))).thenReturn(integranteSalvo);

        Integrante resultado = integranteService.cadastrarIntegrante(integranteDto);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(integranteSalvo.getId(), resultado.getId());
        Assertions.assertEquals(integranteSalvo.getFranquia(), resultado.getFranquia());
        Assertions.assertEquals(integranteSalvo.getNome(), resultado.getNome());
        Assertions.assertEquals(integranteSalvo.getFuncao(), resultado.getFuncao());

    }

    @Test
    void deveListarIntegrantesComSucesso() {
        Integrante integrante1 = new Integrante();
        integrante1.setId(1L);
        integrante1.setNome("Integrante 1");

        Integrante integrante2 = new Integrante();
        integrante2.setId(2L);
        integrante2.setNome("Integrante 2");

        List<Integrante> integrantesMock = Arrays.asList(integrante1, integrante2);

        Mockito.when(integranteRepository.findAll()).thenReturn(integrantesMock);

        List<Integrante> integrantes = integranteService.listarIntegrantes();

        // Verifica o resultado
        Assertions.assertNotNull(integrantes);
        Assertions.assertEquals(2, integrantes.size());
        Assertions.assertEquals("Integrante 1", integrantes.get(0).getNome());
        Assertions.assertEquals("Integrante 2", integrantes.get(1).getNome());
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        NullIntegranteException exception = Assertions.assertThrows(NullIntegranteException.class, () ->
                integranteService.buscarIntegrantePorId(null));

        Assertions.assertEquals("O id do integrante é nulo", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoIdForVazio() {
        NullIntegranteException exception = Assertions.assertThrows(NullIntegranteException.class, () ->
                integranteService.buscarIntegrantePorId(null));

        Assertions.assertEquals("O id do integrante é nulo", exception.getMessage());
    }

    @Test
    void deveRetornarIntegranteQuandoIdForValido() {
        Integrante integrante = new Integrante();
        integrante.setId(1L);
        integrante.setNome("Integrante Teste");

        Mockito.when(integranteRepository.findById(1L)).thenReturn(Optional.of(integrante));

        Optional<Integrante> resultado = integranteService.buscarIntegrantePorId(1L);

        Assertions.assertTrue(resultado.isPresent());
        Assertions.assertEquals("Integrante Teste", resultado.get().getNome());
    }

    @Test
    void deveLancarNotFoundExceptionAoAtualizarQuandoIdNaoExistir() {
        Long id = 1L;
        IntegranteDto integranteDto = new IntegranteDto();

        Mockito.when(integranteRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                integranteService.atualizarIntegrante(id, integranteDto));

        Assertions.assertEquals("Integrante não encontrado", exception.getMessage());
        Mockito.verify(integranteRepository, Mockito.never()).save(Mockito.any(Integrante.class));
    }

    @Test
    void deveAtualizarIntegranteQuandoIdExistir() {
        Long id = 1L;
        IntegranteDto integranteDto = new IntegranteDto();
        integranteDto.setNome("Nome Atualizado");
        integranteDto.setFranquia("Franquia Atualizada");
        integranteDto.setFuncao("Funcao Atualizada");

        Integrante integrante = new Integrante();
        integrante.setId(id);
        integrante.setNome("Nome Antigo");
        integrante.setFranquia("Franquia Antiga");
        integrante.setFuncao("Funcao Antiga");

        Mockito.when(integranteRepository.findById(id)).thenReturn(Optional.of(integrante));
        Mockito.when(integranteRepository.save(Mockito.any(Integrante.class))).thenReturn(integrante);

        Integrante resultado = integranteService.atualizarIntegrante(id, integranteDto);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals("Nome Atualizado", resultado.getNome());
        Assertions.assertEquals("Franquia Atualizada", resultado.getFranquia());
        Assertions.assertEquals("Funcao Atualizada", resultado.getFuncao());

        Mockito.verify(integranteRepository, Mockito.times(1)).findById(id);
        Mockito.verify(integranteRepository, Mockito.times(1)).save(integrante);
    }

    @Test
    void deveLancarNotFoundExceptionAoDeletarQuandoIdNaoExistir() {
        Long id = 1L;

        // Simula que o integrante não existe no repositório
        Mockito.when(integranteRepository.existsById(id)).thenReturn(false);

        // Verifica se a exceção é lançada quando o integrante não é encontrado
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                integranteService.deletarIntegrante(id));

        Assertions.assertEquals("Integrante não encontrado", exception.getMessage());
        Mockito.verify(integranteRepository, Mockito.never()).deleteById(id);
    }

    @Test
    void deveDeletarIntegranteQuandoIdExistir() {
        Long id = 1L;

        Mockito.when(integranteRepository.existsById(id)).thenReturn(true);

        String resultado = integranteService.deletarIntegrante(id);

        Assertions.assertEquals("Integrante deletado com sucesso!", resultado);

        Mockito.verify(integranteRepository, Mockito.times(1)).deleteById(id);
    }

}
