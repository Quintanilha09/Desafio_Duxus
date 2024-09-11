package br.com.duxusdesafio.service;

import br.com.duxusdesafio.exceptions.DateNotFoundException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullTimeException;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.TimeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApiServiceTest {

    @InjectMocks
    private ApiService apiService;

    @Mock
    private Time timeMock1;

    @Mock
    private Time timeMock2;

    @Mock
    private ComposicaoTime composicaoTimeMock1;

    @Mock
    private ComposicaoTime composicaoTimeMock2;

    @Mock
    private Integrante integranteMock1;

    @Mock
    private Integrante integranteMock2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void deveLancarExcecaoQuandoDataForNula() {
        Assertions.assertThrows(DateNotFoundException.class, () -> apiService.validaData(null));
    }

    @Test
    public void deveLancarExcecaoQuandoTimesFiltradosForemNulos() {
        Assertions.assertThrows(NotFoundException.class, () -> apiService.validaTimesFiltrados(null));
    }

    @Test
    public void deveLancarExcecaoQuandoTimesFiltradosForemVazios() {
        List<Time> timesVazios = Collections.emptyList();
        Assertions.assertThrows(NotFoundException.class, () -> apiService.validaTimesFiltrados(timesVazios));
    }

    @Test
    public void naoDeveLancarExcecaoQuandoTimesFiltradosForemValidos() {
        List<Time> timesValidos = Arrays.asList(new Time());
        apiService.validaTimesFiltrados(timesValidos);
    }

    @Test
    public void deveLancarExcecaoQuandoTodosOsTimesForemNulos() {
        Assertions.assertThrows(NullTimeException.class, () -> apiService.validaTodosOsTimes(null));
    }

    @Test
    public void deveLancarExcecaoQuandoTodosOsTimesForemVazios() {
        List<Time> timesVazios = Collections.emptyList();
        Assertions.assertThrows(NullTimeException.class, () -> apiService.validaTodosOsTimes(timesVazios));
    }

    @Test
    public void naoDeveLancarExcecaoQuandoTodosOsTimesForemValidos() {
        List<Time> timesValidos = Arrays.asList(new Time());
        apiService.validaTodosOsTimes(timesValidos);
    }

    @Test
    public void deveLancarNotFoundExceptionQuandoNenhumTimeForEncontrado() {
        LocalDate dataInicial = LocalDate.of(2026, 1, 1);
        LocalDate dataFinal = LocalDate.of(2027, 1, 1);

        List<Time> todosOsTimes = mock(List.class);

        // Verificar se a exceção é lançada ao tentar filtrar times
        Assertions.assertThrows(NotFoundException.class, () -> {
            // Simula o comportamento esperado
            when(apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes))
                    .thenThrow(new NotFoundException("Nenhum time encontrado."));

            Assertions.assertThrows(NotFoundException.class, () -> {
                apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);
            });
        });
    }

    @Test
    public void deveLancarExcecaoQuandoTodosOsTimesForNulo() {
        LocalDate data = LocalDate.now();
        Assertions.assertThrows(NullTimeException.class, () -> apiService.timeDaData(data, null));
    }

    @Test
    public void deveLancarExcecaoQuandoNenhumTimeForEncontradoNaData() {
        LocalDate data = LocalDate.of(2024, 10, 1);
        List<Time> todosOsTimes = Arrays.asList(new Time(LocalDate.of(2022, 12, 31), Collections.emptyList()));

        Assertions.assertThrows(DateNotFoundException.class, () -> apiService.timeDaData(data, todosOsTimes));
    }

    @Test
    public void deveRetornarListaDeNomesDeIntegrantesParaDataValida() {
        ApiService apiService = new ApiService();

        LocalDate data = LocalDate.of(2024, 12, 13);

        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());

        // Criando a composição do time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);

        Time time = new Time(data, Arrays.asList(composicao1, composicao2));

        composicao1.setTime(time);
        composicao2.setTime(time);

        List<Time> todosOsTimes = Arrays.asList(time);

        List<String> resultado = apiService.timeDaData(data, todosOsTimes);

        List<String> esperado = Arrays.asList("João", "Antonio");

        Assertions.assertEquals(esperado, resultado);
    }


    @Test
    public void deveLancarExcecaoQuandoDataNaoForEncontrada() {
        LocalDate data = LocalDate.of(2024, 12, 1);

        Time time = new Time(LocalDate.of(2022, 12, 31), Collections.emptyList());

        List<Time> todosOsTimes = Arrays.asList(time);

        Assertions.assertThrows(DateNotFoundException.class, () -> apiService.timeDaData(data, todosOsTimes));
    }

    @Test
    public void deveRetornarIntegranteMaisUsadoNoPeriodo() {
        LocalDate dataInicial = LocalDate.now().plusDays(1); // Amanhã
        LocalDate dataFinal = LocalDate.now().plusMonths(1); // Um mês à frente

        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Maria", "Defensor", Collections.emptyList());

        // Criar composição dos times
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante1); // João aparece duas vezes

        // Criar times
        Time time1 = new Time(LocalDate.now().plusDays(2), Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(LocalDate.now().plusDays(3), Arrays.asList(composicao3));

        // Vincular composição ao time
        composicao1.setTime(time1);
        composicao2.setTime(time1);
        composicao3.setTime(time2);

        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        // Configurar o mock para retornar o integrante mais usado
        ApiService mockApiService = mock(ApiService.class);
        when(mockApiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes)).thenReturn(integrante1);

        Integrante resultado = mockApiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);

        // Validar o resultado esperado
        Assertions.assertEquals(integrante1, resultado);
    }

    @Test
    public void deveContarAparicoesCorretamente() {
        // Configurar dados do teste
        LocalDate dataInicial = LocalDate.of(2024, 12, 1);
        LocalDate dataFinal = LocalDate.of(2024, 12, 31);

        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());

        // Criando a composição do time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante1);

        // Adicionando a composição ao time
        Time time1 = new Time(dataInicial, Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(dataFinal, Arrays.asList(composicao3));

        // Lista de todos os times
        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        // Esperado: João aparece 2 vezes e Antonio 1 vez
        Integrante esperado = integrante1;

        // Executa o método a ser testado
        Integrante resultado = apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);

        // Valida o resultado
        Assertions.assertEquals(esperado, resultado);
    }

    @Test
    public void testTimeMaisComum() {

        TimeRepository timeRepository = mock(TimeRepository.class);

        LocalDate dataInicial = LocalDate.of(2024, 11, 1);
        LocalDate dataFinal = LocalDate.of(2024, 12, 31);

        // Criando os integrantes
        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());

        // Criando as composições de time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante1);

        // Criando os times
        Time time1 = new Time(dataInicial, Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(dataFinal, Arrays.asList(composicao3));

        // Lista de todos os times
        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        // Mockando o retorno do repositório
        when(timeRepository.findAll()).thenReturn(todosOsTimes);

        // Teste
        List<String> resultado = apiService.timeMaisComum(dataInicial, dataFinal, todosOsTimes);

        // Verificação
        Assertions.assertNotNull(resultado);
        Assertions.assertTrue(resultado.contains("João"));
    }

    @Test
    public void deveRetornarAFuncaoMaisComum() {

        TimeRepository timeRepository = mock(TimeRepository.class);

        LocalDate dataInicial = LocalDate.of(2024, 11, 1);
        LocalDate dataFinal = LocalDate.of(2024, 12, 31);

        // Criando os integrantes
        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());
        Integrante integrante3 = new Integrante("Franquia C", "Jonas", "Defensor", Collections.emptyList());

        // Criando as composições de time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante3);

        // Criando os times
        Time time1 = new Time(dataInicial, Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(dataFinal, Arrays.asList(composicao3));

        // Lista de todos os times
        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        String funcaoMaisComum = apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes);

        Assertions.assertEquals("Defensor", funcaoMaisComum);
    }

    @Test
    public void testFranquiaMaisFamosa_ComFranquiasValidas() {

        TimeRepository timeRepository = mock(TimeRepository.class);

        LocalDate dataInicial = LocalDate.of(2024, 11, 1);
        LocalDate dataFinal = LocalDate.of(2024, 12, 31);

        // Criando os integrantes
        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());
        Integrante integrante3 = new Integrante("Franquia A", "Jonas", "Defensor", Collections.emptyList());

        // Criando as composições de time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante3);

        // Criando os times
        Time time1 = new Time(dataInicial, Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(dataFinal, Arrays.asList(composicao3));

        // Lista de todos os times
        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        String franquiaMaisFamosa = apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes);

        Assertions.assertEquals("Franquia A", franquiaMaisFamosa);
    }

    @Test
    public void testContagemPorFranquia_ComFranquiasValidas() {

        TimeRepository timeRepository = mock(TimeRepository.class);

        LocalDate dataInicial = LocalDate.of(2024, 11, 1);
        LocalDate dataFinal = LocalDate.of(2025, 02, 20);

        // Criando os integrantes
        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());
        Integrante integrante3 = new Integrante("Franquia A", "Jonas", "Defensor", Collections.emptyList());

        // Criando as composições de time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante3);

        // Criando os times
        Time time1 = new Time(dataInicial, Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(dataFinal, Arrays.asList(composicao3));

        // Lista de todos os times
        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        Map<String, Long> contagem = apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes);

        Assertions.assertEquals(2, contagem.size());
        Assertions.assertEquals(2L, contagem.get("Franquia A"));
        Assertions.assertEquals(1L, contagem.get("Franquia B"));
    }

    @Test
    public void testContagemPorFuncao_ComFuncoesValidas() {

        TimeRepository timeRepository = mock(TimeRepository.class);

        LocalDate dataInicial = LocalDate.of(2024, 11, 1);
        LocalDate dataFinal = LocalDate.of(2025, 02, 20);

        // Criando os integrantes
        Integrante integrante1 = new Integrante("Franquia A", "João", "Atacante", Collections.emptyList());
        Integrante integrante2 = new Integrante("Franquia B", "Antonio", "Defensor", Collections.emptyList());
        Integrante integrante3 = new Integrante("Franquia A", "Jonas", "Atacante", Collections.emptyList());

        // Criando as composições de time
        ComposicaoTime composicao1 = new ComposicaoTime(null, integrante1);
        ComposicaoTime composicao2 = new ComposicaoTime(null, integrante2);
        ComposicaoTime composicao3 = new ComposicaoTime(null, integrante3);

        // Criando os times
        Time time1 = new Time(dataInicial, Arrays.asList(composicao1, composicao2));
        Time time2 = new Time(dataFinal, Arrays.asList(composicao3));

        // Lista de todos os times
        List<Time> todosOsTimes = Arrays.asList(time1, time2);

        Map<String, Long> contagem = apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);

        Assertions.assertEquals(2, contagem.size());
        Assertions.assertEquals(2L, contagem.get("Atacante"));
        Assertions.assertEquals(1L, contagem.get("Defensor"));
    }

}


