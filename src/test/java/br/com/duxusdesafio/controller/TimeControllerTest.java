package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.TimeDto;
import br.com.duxusdesafio.exceptions.DateNotFoundException;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullTimeException;
import br.com.duxusdesafio.model.Time;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeControllerTest {

    @InjectMocks
    private TimeController timeController;

    @Mock
    private TimeService timeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCadastrarTime_Success() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));
        Time time = new Time();
        Mockito.when(timeService.cadastrarTime(Mockito.
                any(LocalDate.class), Mockito.anyList())).thenReturn(time);

        ResponseEntity<Object> response = timeController.cadastrarTime(timeDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(time, response.getBody());
    }

    @Test
    public void testCadastrarTime_DateNotFoundException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));
        Mockito.when(timeService.cadastrarTime(Mockito.
                any(LocalDate.class), Mockito.anyList())).thenThrow(
                new DateNotFoundException("Data não encontrada"));

        ResponseEntity<Object> response = timeController.cadastrarTime(timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Data não encontrada", response.getBody());
    }

    @Test
    public void testCadastrarTime_NullTimeException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));
        Mockito.when(timeService.cadastrarTime(Mockito.
                any(LocalDate.class), Mockito.anyList())).thenThrow(
                new NullTimeException("Tempo nulo"));

        ResponseEntity<Object> response = timeController.cadastrarTime(timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tempo nulo", response.getBody());
    }

    @Test
    public void testCadastrarTime_IntegranteException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));
        Mockito.when(timeService.cadastrarTime(Mockito.
                any(LocalDate.class), Mockito.anyList())).thenThrow(
                new IntegranteException("Integrante inválido"));

        ResponseEntity<Object> response = timeController.cadastrarTime(timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Integrante inválido", response.getBody());
    }

    @Test
    public void testCadastrarTime_GenericException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));
        Mockito.when(timeService.cadastrarTime(Mockito.
                any(LocalDate.class), Mockito.anyList())).thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = timeController.cadastrarTime(timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    public void testListarTodosOsTimes_Success() {
        List<Time> times = Arrays.asList(new Time(), new Time());
        Mockito.when(timeService.listarTodosOsTimes()).thenReturn(times);

        ResponseEntity<Object> response = timeController.listarTodosOsTimes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(times, response.getBody());
    }

    @Test
    public void testListarTodosOsTimes_NotFoundException() {
        Mockito.when(timeService.listarTodosOsTimes()).
                thenThrow(new NotFoundException("Nenhum time encontrado"));

        ResponseEntity<Object> response = timeController.listarTodosOsTimes();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Nenhum time encontrado", response.getBody());
    }

    @Test
    public void testBuscarTimePorId_Success() {
        Time time = new Time();
        Mockito.when(timeService.buscarTimePorId(Mockito.anyLong())).thenReturn(time);

        ResponseEntity<Object> response = timeController.buscarTimePorId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(time, response.getBody());
    }

    @Test
    public void testBuscarTimePorId_NotFoundException() {
        Mockito.when(timeService.buscarTimePorId(Mockito.anyLong())).
                thenThrow(new NotFoundException("Time não encontrado"));

        ResponseEntity<Object> response = timeController.buscarTimePorId(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Time não encontrado", response.getBody());
    }

    @Test
    public void testAtualizarTime_Success() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));

        Time timeAtualizado = new Time();
        Mockito.when(timeService.atualizarTime(
                Mockito.anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.anyList()
        )).thenReturn(timeAtualizado);

        ResponseEntity<Object> response = timeController.atualizarTime(1L, timeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(timeAtualizado, response.getBody());
    }
    @Test
    public void testAtualizarTime_NotFoundException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));

        Mockito.when(timeService.atualizarTime(
                Mockito.anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.anyList()
        )).thenThrow(new NotFoundException("Time não encontrado"));

        ResponseEntity<Object> response = timeController.atualizarTime(1L, timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Time não encontrado", response.getBody().toString());
    }

    @Test
    public void testAtualizarTime_IntegranteException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));

        Mockito.when(timeService.atualizarTime(
                Mockito.anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.anyList()
        )).thenThrow(new IntegranteException("Integrante inválido"));

        ResponseEntity<Object> response = timeController.atualizarTime(1L, timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Integrante inválido", response.getBody().toString());
    }

    @Test
    public void testAtualizarTime_NullTimeException() {
        TimeDto timeDto = new TimeDto();
        timeDto.setData(LocalDate.parse("2024-09-15"));
        timeDto.setIdsIntegrantes(Arrays.asList(1L, 2L, 3L));

        Mockito.when(timeService.atualizarTime(
                Mockito.anyLong(),
                Mockito.any(LocalDate.class),
                Mockito.anyList()
        )).thenThrow(new NullTimeException("Tempo nulo"));

        ResponseEntity<Object> response = timeController.atualizarTime(1L, timeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Tempo nulo", response.getBody().toString());
    }

    @Test
    public void testDeletarTime_Success() {
        // Configuração do mock para o caso de sucesso
        Mockito.doNothing().when(timeService).deletarTime(Mockito.anyLong());

        // Chamada ao método do controlador
        ResponseEntity<Object> response = timeController.deletarTime(1L);

        // Verificações
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void testDeletarTime_NotFoundException() {
        // Configuração do mock para lançar NotFoundException
        Mockito.doThrow(new NotFoundException("Time não encontrado")).when(timeService).deletarTime(Mockito.anyLong());

        // Chamada ao método do controlador
        ResponseEntity<Object> response = timeController.deletarTime(1L);

        // Verificações
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Time não encontrado", response.getBody());
    }


}
