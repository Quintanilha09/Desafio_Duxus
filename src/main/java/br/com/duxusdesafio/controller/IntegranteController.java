package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.IntegranteDto;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import br.com.duxusdesafio.service.IntegranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("integrante")
public class IntegranteController {

    @Autowired
    private IntegranteService integranteService;

    @PostMapping(value = "cadastrar")
    public ResponseEntity<Integrante> cadastrarIntegrante(@Valid @RequestBody IntegranteDto integranteDto) {
        Integrante integrante = integranteService.cadastrarIntegrante(integranteDto);
        return new ResponseEntity<>(integrante, HttpStatus.CREATED);
    }

    @GetMapping(value = "listar")
    public ResponseEntity<List<Integrante>> listarIntegrantes() {
        List<Integrante> integrantes = integranteService.listarIntegrantes();
        return new ResponseEntity<>(integrantes, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Integrante> buscarIntegrantePorId(@PathVariable Long id) {
        Optional<Integrante> integranteOpt = integranteService.buscarIntegrantePorId(id);
        return integranteOpt.map(integrante -> new ResponseEntity<>(integrante, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("atualizar/{id}")
    public ResponseEntity<Integrante> atualizarIntegrante(@PathVariable Long id, @RequestBody IntegranteDto integranteDto) {
        try {
            Integrante integrante = integranteService.atualizarIntegrante(id, integranteDto);
            return new ResponseEntity<>(integrante, HttpStatus.OK);
        } catch (IntegranteException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("excluir/{id}")
    public ResponseEntity<Void> deletarIntegrante(@PathVariable Long id) {
        try {
            integranteService.deletarIntegrante(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IntegranteException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
