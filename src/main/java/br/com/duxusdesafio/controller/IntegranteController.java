package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteDto;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullIntegranteException;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.service.IntegranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("integrante")
public class IntegranteController {

    @Autowired
    private IntegranteService integranteService;

    @PostMapping(value = "cadastrar")
    public ResponseEntity<Object> cadastrarIntegrante(@Valid @RequestBody IntegranteDto integranteDto) {
        try {
            Integrante integrante = integranteService.cadastrarIntegrante(integranteDto);
            return new ResponseEntity<>(integrante, HttpStatus.CREATED);
        } catch (IntegranteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "listar")
    public ResponseEntity<Object> listarIntegrantes() {
        try {
            List<Integrante> integrantes = integranteService.listarIntegrantes();
            return new ResponseEntity<>(integrantes, HttpStatus.OK);
        } catch (NullIntegranteException | IntegranteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarIntegrantePorId(@PathVariable Long id) {
        try {
            Integrante integrante = integranteService.buscarIntegrantePorId(id)
                    .orElseThrow(() -> new NullIntegranteException("Integrante não encontrado para o ID: " + id));
            return new ResponseEntity<>(integrante, HttpStatus.OK);
        } catch (NullIntegranteException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Object> atualizarIntegrante(@PathVariable Long id, @RequestBody IntegranteDto integranteDto) {
        try {
            Integrante integrante = integranteService.atualizarIntegrante(id, integranteDto);
            return new ResponseEntity<>(integrante, HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Object> deletarIntegrante(@PathVariable Long id) {
        try {
            integranteService.deletarIntegrante(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
