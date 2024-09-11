package br.com.duxusdesafio.service;

import br.com.duxusdesafio.exceptions.DateNotFoundException;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullTimeException;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TimeServiceTest {

    @InjectMocks
    private TimeService timeService;

    @Mock
    private IntegranteRepository integranteRepository;

    @Mock
    private TimeRepository timeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarDateNotFoundExceptionQuandoDataForNula() {
        LocalDate data = null;
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        DateNotFoundException exception = Assertions.assertThrows(DateNotFoundException.class, () ->
                timeService.cadastrarTime(data, idsIntegrantes));

        Assertions.assertEquals("A data do time não pode ser nula.", exception.getMessage());
        Mockito.verify(timeRepository, Mockito.never()).save(Mockito.any(Time.class));
    }

    @Test
    void deveLancarDateNotFoundExceptionQuandoDataForAnteriorAoHoje() {
        LocalDate data = LocalDate.now().minusDays(1);
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        DateNotFoundException exception = Assertions.assertThrows(DateNotFoundException.class, () ->
                timeService.cadastrarTime(data, idsIntegrantes));

        Assertions.assertEquals("A data do time não pode ser anterior à data de hoje.", exception.getMessage());
        Mockito.verify(timeRepository, Mockito.never()).save(Mockito.any(Time.class));
    }

    @Test
    void deveLancarIntegranteExceptionQuandoIdsIntegrantesForNulo() {
        LocalDate data = LocalDate.now();
        List<Long> idsIntegrantes = null;

        IntegranteException exception = Assertions.assertThrows(IntegranteException.class, () ->
                timeService.cadastrarTime(data, idsIntegrantes));

        Assertions.assertEquals("A lista de IDs de integrantes não pode ser nula ou vazia.", exception.getMessage());
        Mockito.verify(timeRepository, Mockito.never()).save(Mockito.any(Time.class));
    }

    @Test
    void deveLancarIntegranteExceptionQuandoIdsIntegrantesForVazio() {
        LocalDate data = LocalDate.now();
        List<Long> idsIntegrantes = Collections.emptyList();

        IntegranteException exception = Assertions.assertThrows(IntegranteException.class, () ->
                timeService.cadastrarTime(data, idsIntegrantes));

        Assertions.assertEquals("A lista de IDs de integrantes não pode ser nula ou vazia.", exception.getMessage());
        Mockito.verify(timeRepository, Mockito.never()).save(Mockito.any(Time.class));
    }

    @Test
    void deveLancarIntegranteExceptionQuandoAlgunsIdsDeIntegrantesSaoInvalidos() {
        LocalDate data = LocalDate.now();
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        Integrante integrante1 = new Integrante();
        integrante1.setId(1L);

        // Simula que apenas o ID 1 é válido
        Mockito.when(integranteRepository.findAllById(idsIntegrantes)).thenReturn(Arrays.asList(integrante1));

        IntegranteException exception = Assertions.assertThrows(IntegranteException.class, () ->
                timeService.cadastrarTime(data, idsIntegrantes));

        Assertions.assertEquals("Um ou mais IDs de integrantes são inválidos.", exception.getMessage());
        Mockito.verify(timeRepository, Mockito.never()).save(Mockito.any(Time.class));
    }

    @Test
    void deveSalvarTimeQuandoDadosForemValidos() {
        LocalDate data = LocalDate.now().plusDays(1);
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        Integrante integrante1 = new Integrante();
        integrante1.setId(1L);

        Integrante integrante2 = new Integrante();
        integrante2.setId(2L);

        // Simula que os IDs são válidos
        Mockito.when(integranteRepository.findAllById(idsIntegrantes)).thenReturn(Arrays.asList(integrante1, integrante2));

        Time timeEsperado = new Time();
        timeEsperado.setData(data);
        timeEsperado.setComposicaoTime(Arrays.asList(
                new ComposicaoTime(timeEsperado, integrante1),
                new ComposicaoTime(timeEsperado, integrante2)
        ));

        Mockito.when(timeRepository.save(Mockito.any(Time.class))).thenReturn(timeEsperado);

        Time timeResultado = timeService.cadastrarTime(data, idsIntegrantes);

        Assertions.assertEquals(timeEsperado, timeResultado);
        Mockito.verify(timeRepository, Mockito.times(1)).save(Mockito.any(Time.class));
    }

    @Test
    void deveLancarNotFoundExceptionQuandoNaoExistiremTimes() {
        Mockito.when(timeRepository.findAll()).thenReturn(Collections.emptyList());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                timeService.listarTodosOsTimes());

        Assertions.assertEquals("Nenhum time encontrado.", exception.getMessage());
        Mockito.verify(timeRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deveRetornarListaDeTimesQuandoExistiremTimes() {
        Time time1 = new Time();
        Time time2 = new Time();
        Mockito.when(timeRepository.findAll()).thenReturn(Arrays.asList(time1, time2));

        List<Time> times = timeService.listarTodosOsTimes();

        Assertions.assertNotNull(times);
        Assertions.assertEquals(2, times.size());
        Assertions.assertTrue(times.contains(time1));
        Assertions.assertTrue(times.contains(time2));
        Mockito.verify(timeRepository, Mockito.times(1)).findAll();
    }

    @Test
    void deveLancarNotFoundExceptionQuandoTimeNaoForEncontradoPorId() {
        Long id = 1L;
        Mockito.when(timeRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () ->
                timeService.buscarTimePorId(id));

        Assertions.assertEquals("Time não encontrado com o ID: " + id, exception.getMessage());
        Mockito.verify(timeRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deveRetornarTimeQuandoTimeForEncontradoPorId() {
        Long id = 1L;
        Time time = new Time();
        Mockito.when(timeRepository.findById(id)).thenReturn(Optional.of(time));

        Time result = timeService.buscarTimePorId(id);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(time, result);
        Mockito.verify(timeRepository, Mockito.times(1)).findById(id);
    }

    @Test
    void deveLancarNullTimeExceptionQuandoDataForNula() {
        Long id = 1L;
        LocalDate data = null;
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        NullTimeException exception = Assertions.assertThrows(NullTimeException.class, () ->
                timeService.atualizarTime(id, data, idsIntegrantes));

        Assertions.assertEquals("A data do time não pode ser nula.", exception.getMessage());
    }

    @Test
    void deveLancarIntegranteExceptionQuandoIdsIntegrantesForNulaOuContiverValoresNulos() {
        Long id = 1L;
        LocalDate data = LocalDate.now();

        List<Long> idsIntegrantesNulo = null;
        IntegranteException exception = Assertions.assertThrows(IntegranteException.class, () ->
                timeService.atualizarTime(id, data, idsIntegrantesNulo));
        Assertions.assertEquals("A lista de IDs de integrantes não pode ser nula ou conter valores nulos.", exception.getMessage());

        List<Long> idsIntegrantesComNulo = Arrays.asList(1L, null);
        exception = Assertions.assertThrows(IntegranteException.class, () ->
                timeService.atualizarTime(id, data, idsIntegrantesComNulo));
        Assertions.assertEquals("A lista de IDs de integrantes não pode ser nula ou conter valores nulos.", exception.getMessage());
    }

    @Test
    void deveLancarIntegranteExceptionQuandoIdsIntegrantesInexistem() {
        Long id = 1L;
        LocalDate data = LocalDate.now();
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        Time timeExistente = new Time();
        Mockito.when(timeRepository.findById(id)).thenReturn(Optional.of(timeExistente));
        Mockito.when(integranteRepository.findAllById(idsIntegrantes)).thenReturn(Collections.singletonList(new Integrante()));

        IntegranteException exception = Assertions.assertThrows(IntegranteException.class, () ->
                timeService.atualizarTime(id, data, idsIntegrantes));

        Assertions.assertEquals("Um ou mais IDs de integrantes são inválidos.", exception.getMessage());
    }

    @Test
    void deveAtualizarTimeComSucesso() {
        Long id = 1L;
        LocalDate data = LocalDate.now().plusDays(1);
        List<Long> idsIntegrantes = Arrays.asList(1L, 2L);

        Time timeExistente = new Time();
        Integrante integrante1 = new Integrante();
        Integrante integrante2 = new Integrante();
        List<Integrante> integrantes = Arrays.asList(integrante1, integrante2);

        Mockito.when(timeRepository.findById(id)).thenReturn(Optional.of(timeExistente));
        Mockito.when(integranteRepository.findAllById(idsIntegrantes)).thenReturn(integrantes);
        Mockito.when(timeRepository.save(timeExistente)).thenReturn(timeExistente);

        Time timeAtualizado = timeService.atualizarTime(id, data, idsIntegrantes);

        Assertions.assertNotNull(timeAtualizado);
        Assertions.assertEquals(data, timeAtualizado.getData());
        Assertions.assertEquals(2, timeAtualizado.getComposicaoTime().size());
        Assertions.assertTrue(timeAtualizado.getComposicaoTime().stream()
                .allMatch(composicao -> integrantes.contains(composicao.getIntegrante())));
        Mockito.verify(timeRepository, Mockito.times(1)).save(timeExistente);
    }

    @Test
    void deveDeletarTimeComSucesso() throws NotFoundException {
        Long id = 1L;
        Time time = new Time();
        Mockito.when(timeRepository.findById(id)).thenReturn(java.util.Optional.of(time));

        // Chama o método
        timeService.deletarTime(id);

        // Verifica se o método delete foi chamado
        Mockito.verify(timeRepository, Mockito.times(1)).delete(time);
    }

    @Test
    void deveLancarNotFoundExceptionQuandoTimeNaoForEncontrado() {
        Long id = 1L;
        Mockito.when(timeRepository.findById(id)).thenReturn(java.util.Optional.empty());

        // Verifica se a exceção é lançada
        Assertions.assertThrows(NotFoundException.class, () -> timeService.deletarTime(id));

        Mockito.verify(timeRepository, Mockito.never()).delete(Mockito.any(Time.class));
    }

}
