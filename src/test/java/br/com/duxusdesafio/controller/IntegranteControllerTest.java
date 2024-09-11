package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteDto;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullIntegranteException;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.service.IntegranteService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class IntegranteControllerTest {

    @InjectMocks
    private IntegranteController integranteController;

    @Mock
    private IntegranteService integranteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCadastrarIntegranteComSucesso() throws IntegranteException {
        IntegranteDto integranteDto = new IntegranteDto();
        Integrante integrante = new Integrante();

        Mockito.when(integranteService.cadastrarIntegrante(integranteDto)).thenReturn(integrante);

        ResponseEntity<Object> response = integranteController.cadastrarIntegrante(integranteDto);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(integrante, response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaIntegranteExceptionCadastrarIntegrante() throws IntegranteException {
        IntegranteDto integranteDto = new IntegranteDto();
        Mockito.when(integranteService.cadastrarIntegrante(integranteDto))
                .thenThrow(new IntegranteException("Erro ao cadastrar o integrante"));

        ResponseEntity<Object> response = integranteController.cadastrarIntegrante(integranteDto);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro ao cadastrar o integrante", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaCadastrarIntegrante() {
        IntegranteDto integranteDto = new IntegranteDto();
        Mockito.when(integranteService.cadastrarIntegrante(integranteDto))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = integranteController.cadastrarIntegrante(integranteDto);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveListarIntegrantesComSucesso() throws IntegranteException {
        Integrante integrante1 = new Integrante();
        Integrante integrante2 = new Integrante();
        List<Integrante> integrantes = Arrays.asList(integrante1, integrante2);

        Mockito.when(integranteService.listarIntegrantes()).thenReturn(integrantes);

        ResponseEntity<Object> response = integranteController.listarIntegrantes();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(integrantes, response.getBody());
    }

    @Test
    void deveRetornarNotFoundParaNullIntegranteExceptionListarIntegrantes() throws IntegranteException {
        Mockito.when(integranteService.listarIntegrantes())
                .thenThrow(new NullIntegranteException("Nenhum integrante encontrado"));

        ResponseEntity<Object> response = integranteController.listarIntegrantes();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Nenhum integrante encontrado", response.getBody());
    }

    @Test
    void deveRetornarNotFoundParaIntegranteExceptionListarIntegrantes() throws IntegranteException {
        Mockito.when(integranteService.listarIntegrantes())
                .thenThrow(new IntegranteException("Erro ao listar integrantes"));

        ResponseEntity<Object> response = integranteController.listarIntegrantes();

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Erro ao listar integrantes", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaListarIntegrantes() {
        Mockito.when(integranteService.listarIntegrantes())
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = integranteController.listarIntegrantes();

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveBuscarIntegrantePorIdComSucesso() throws NullIntegranteException {
        Long id = 1L;
        Integrante integrante = new Integrante();

        Mockito.when(integranteService.buscarIntegrantePorId(id)).thenReturn(Optional.of(integrante));

        ResponseEntity<Object> response = integranteController.buscarIntegrantePorId(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(integrante, response.getBody());
    }

    @Test
    void deveRetornarNotFoundParaNullIntegranteExceptionBuscarIntegrantePorId() throws NullIntegranteException {
        Long id = 1L;

        Mockito.when(integranteService.buscarIntegrantePorId(id))
                .thenReturn(Optional.empty());

        ResponseEntity<Object> response = integranteController.buscarIntegrantePorId(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Integrante não encontrado para o ID: " + id, response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaBuscarIntegrantePorId() {
        Long id = 1L;

        Mockito.when(integranteService.buscarIntegrantePorId(id))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = integranteController.buscarIntegrantePorId(id);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveAtualizarIntegranteComSucesso() throws NotFoundException {
        Long id = 1L;
        IntegranteDto integranteDto = new IntegranteDto();
        Integrante integranteAtualizado = new Integrante();

        Mockito.when(integranteService.atualizarIntegrante(id, integranteDto)).thenReturn(integranteAtualizado);

        ResponseEntity<Object> response = integranteController.atualizarIntegrante(id, integranteDto);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(integranteAtualizado, response.getBody());
    }

    @Test
    void deveRetornarNotFoundParaNotFoundExceptionNaAtualizacao() throws NotFoundException {
        Long id = 1L;
        IntegranteDto integranteDto = new IntegranteDto();

        Mockito.when(integranteService.atualizarIntegrante(id, integranteDto))
                .thenThrow(new NotFoundException("Integrante não encontrado"));

        ResponseEntity<Object> response = integranteController.atualizarIntegrante(id, integranteDto);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Integrante não encontrado", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaNaAtualizacao() {
        Long id = 1L;
        IntegranteDto integranteDto = new IntegranteDto();

        Mockito.when(integranteService.atualizarIntegrante(id, integranteDto))
                .thenThrow(new RuntimeException("Erro inesperado"));

        ResponseEntity<Object> response = integranteController.atualizarIntegrante(id, integranteDto);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

    @Test
    void deveDeletarIntegranteComSucesso() throws NotFoundException {
        Long id = 1L;
        ResponseEntity<Object> response = integranteController.deletarIntegrante(id);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Mockito.verify(integranteService, Mockito.times(1)).deletarIntegrante(id);
    }

    @Test
    void deveRetornarNotFoundParaNotFoundExceptionNaExclusao() throws NotFoundException {
        Long id = 1L;

        Mockito.doThrow(new NotFoundException("Integrante não encontrado")).when(integranteService).deletarIntegrante(id);

        ResponseEntity<Object> response = integranteController.deletarIntegrante(id);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertEquals("Integrante não encontrado", response.getBody());
    }

    @Test
    void deveRetornarInternalServerErrorParaExcecaoGenericaNaExclusao() {
        Long id = 1L;

        Mockito.doThrow(new RuntimeException("Erro inesperado")).when(integranteService).deletarIntegrante(id);

        ResponseEntity<Object> response = integranteController.deletarIntegrante(id);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Assertions.assertEquals("Erro inesperado", response.getBody());
    }

}
