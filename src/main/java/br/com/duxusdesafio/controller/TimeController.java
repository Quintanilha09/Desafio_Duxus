package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.TimeDto;
import br.com.duxusdesafio.exceptions.DateNotFoundException;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullTimeException;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("time")
@RestController
public class TimeController {

    @Autowired
    TimeService timeService;

    @PostMapping(value = "cadastrar")
    public ResponseEntity<Object> cadastrarTime(@RequestBody TimeDto timeDto) {
        try {
            Time time = timeService.cadastrarTime(timeDto.getData(), timeDto.getIdsIntegrantes());
            return new ResponseEntity<>(time, HttpStatus.CREATED);
        } catch (DateNotFoundException | NullTimeException | IntegranteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping(value = "listar")
    public ResponseEntity<Object> listarTodosOsTimes() {
        try {
            List<Time> times = timeService.listarTodosOsTimes();
            return ResponseEntity.ok(times);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> buscarTimePorId(@PathVariable Long id) {
        try {
            Time time = timeService.buscarTimePorId(id);
            return ResponseEntity.ok(time);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "atualizar/{id}")
    public ResponseEntity<Object> atualizarTime(@PathVariable Long id, @RequestBody TimeDto timeDto) {
        try {
            Time timeAtualizado = timeService.atualizarTime(id, timeDto.getData(), timeDto.getIdsIntegrantes());
            return ResponseEntity.ok(timeAtualizado);
        } catch (NotFoundException | IntegranteException | NullTimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(value = "deletar/{id}")
    public ResponseEntity<Object> deletarTime(@PathVariable Long id) {
        try {
            timeService.deletarTime(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
