package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.exceptions.DateNotFoundException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullTimeException;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.response.FuncaoMaisComumResponse;
import br.com.duxusdesafio.response.TimeDaDataResponse;
import br.com.duxusdesafio.service.ApiService;
import br.com.duxusdesafio.service.TimeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiControllerTest {

    @InjectMocks
    private ApiController apiController;

    @Mock
    private ApiService apiService;

    @Mock
    private TimeService timeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRetornarTimesDaDataComSucesso() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate data = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());
        List<String> nomesIntegrantes = Arrays.asList("Integrante1", "Integrante2");

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.timeDaData(data, todosOsTimes)).thenReturn(nomesIntegrantes);

        ResponseEntity<Object> response = apiController.getTimesDaData(data);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        TimeDaDataResponse responseBody = (TimeDaDataResponse) response.getBody();

        Assertions.assertNotNull(responseBody);
        Assertions.assertEquals(data, responseBody.getData());
    }

    @Test
    void deveRetornarNotFoundQuandoDateNotFoundException() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate data = LocalDate.now();

        Mockito.when(timeService.listarTodosOsTimes()).thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getTimesDaData(data);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarNotFoundQuandoNullTimeException() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate data = LocalDate.now();

        Mockito.when(timeService.listarTodosOsTimes()).thenThrow(new NullTimeException("Data é nula"));

        ResponseEntity<Object> response = apiController.getTimesDaData(data);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data é nula", response.getBody());
    }

    @Test
    void deveRetornarNotFoundQuandoNotFoundException() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate data = LocalDate.now();

        Mockito.when(timeService.listarTodosOsTimes()).thenThrow(new NotFoundException("Nenhum time encontrado"));

        ResponseEntity<Object> response = apiController.getTimesDaData(data);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Nenhum time encontrado", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorQuandoOcorrerExcecaoGenerica() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate data = LocalDate.now();

        Mockito.when(timeService.listarTodosOsTimes()).thenThrow(new RuntimeException("Erro interno"));

        ResponseEntity<Object> response = apiController.getTimesDaData(data);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro interno", response.getBody());
    }

    @Test
    void deveRetornarIntegranteMaisUsadoComSucesso() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        Integrante integranteMaisUsado = new Integrante();

        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes)).thenReturn(integranteMaisUsado);

        ResponseEntity<Object> response = apiController.getIntegranteMaisUsado(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(integranteMaisUsado, response.getBody());
    }

    @Test
    void deveRetornarNotFoundException() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new NotFoundException("Integrante não encontrado"));

        ResponseEntity<Object> response = apiController.getIntegranteMaisUsado(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Integrante não encontrado", response.getBody());
    }

    @Test
    void deveRetornarDateNotFoundException() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getIntegranteMaisUsado(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenerica() throws DateNotFoundException, NullTimeException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = apiController.getIntegranteMaisUsado(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveRetornarTimeMaisComumComSucesso() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());
        List<String> nomesIntegrantes = Arrays.asList("Integrante 1", "Integrante 2");

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.timeMaisComum(dataInicial, dataFinal, todosOsTimes)).thenReturn(nomesIntegrantes);

        ResponseEntity<Object> response = apiController.getTimeMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(nomesIntegrantes, response.getBody());
    }

    @Test
    void deveRetornarNotFoundExceptionTimeMaisComum() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.timeMaisComum(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new NotFoundException("Time não encontrado"));

        ResponseEntity<Object> response = apiController.getTimeMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Time não encontrado", response.getBody());
    }

    @Test
    void deveRetornarDateNotFoundExceptionTimemaisComum() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.timeMaisComum(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getTimeMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaTimemaisComum() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.timeMaisComum(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = apiController.getTimeMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveRetornarFuncaoMaisComumComSucesso() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());
        String funcaoMaisComum = "Atacante";

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes)).thenReturn(funcaoMaisComum);

        ResponseEntity<Object> response = apiController.getFuncaoMaisComum(dataInicial, dataFinal);

        FuncaoMaisComumResponse expectedResponse = new FuncaoMaisComumResponse(funcaoMaisComum);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deveRetornarNotFoundExceptionFuncaoMaisComum() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new NotFoundException("Função não encontrada"));

        ResponseEntity<Object> response = apiController.getFuncaoMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Função não encontrada", response.getBody());
    }

    @Test
    void deveRetornarDateNotFoundExceptionFuncaoMaisComum() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getFuncaoMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaFuncaoMaisComum() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = apiController.getFuncaoMaisComum(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveRetornarFranquiaMaisFamosaComSucesso() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());
        String franquiaMaisFamosa = "Franquia A";

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes)).thenReturn(franquiaMaisFamosa);

        ResponseEntity<Object> response = apiController.getFranquiaMaisFamosa(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(franquiaMaisFamosa, response.getBody());
    }

    @Test
    void deveRetornarNotFoundExceptionFranquiaMaisFamosa() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new NotFoundException("Franquia não encontrada"));

        ResponseEntity<Object> response = apiController.getFranquiaMaisFamosa(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Franquia não encontrada", response.getBody());
    }

    @Test
    void deveRetornarDateNotFoundExceptionFranquiaMaisFamosa() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getFranquiaMaisFamosa(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaFranquiaMaisFamosa() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = apiController.getFranquiaMaisFamosa(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveRetornarContagemPorFranquiaComSucesso() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());
        Map<String, Long> contagemPorFranquia = new HashMap<>();
        contagemPorFranquia.put("Franquia A", 5L);
        contagemPorFranquia.put("Franquia B", 3L);

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes)).thenReturn(contagemPorFranquia);

        ResponseEntity<Object> response = apiController.getContagemPorFranquia(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(contagemPorFranquia, response.getBody());
    }

    @Test
    void deveRetornarNotFoundExceptionContagemPorFranquia() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new NotFoundException("Franquia não encontrada"));

        ResponseEntity<Object> response = apiController.getContagemPorFranquia(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Franquia não encontrada", response.getBody());
    }

    @Test
    void deveRetornarDateNotFoundExceptionContagemPorFranquia() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getContagemPorFranquia(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaContagemPorFranquia() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = apiController.getContagemPorFranquia(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveRetornarContagemPorFuncaoComSucesso() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());
        Map<String, Long> contagemPorFuncao = new HashMap<>();
        contagemPorFuncao.put("Função A", 5L);
        contagemPorFuncao.put("Função B", 3L);

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes)).thenReturn(contagemPorFuncao);

        ResponseEntity<Object> response = apiController.getContagemPorFuncao(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(contagemPorFuncao, response.getBody());
    }

    @Test
    void deveRetornarNotFoundExceptionContagemPorFuncao() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new NotFoundException("Função não encontrada"));

        ResponseEntity<Object> response = apiController.getContagemPorFuncao(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Função não encontrada", response.getBody());
    }

    @Test
    void deveRetornarDateNotFoundExceptionContagemPorFuncao() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = apiController.getContagemPorFuncao(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaContagemPorFuncao() throws DateNotFoundException, NotFoundException {
        LocalDate dataInicial = LocalDate.now().minusDays(10);
        LocalDate dataFinal = LocalDate.now();
        List<Time> todosOsTimes = Arrays.asList(new Time(), new Time());

        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(todosOsTimes);
        Mockito.when(apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = apiController.getContagemPorFuncao(dataInicial, dataFinal);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

}
