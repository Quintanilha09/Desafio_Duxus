package br.com.duxusdesafio.controller;

import br.com.duxusdesafio.dto.TimeDto;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("time")
@RestController
public class TimeController {

    @Autowired
    TimeService timeService;

    @PostMapping(value = "cadastrar")
    public ResponseEntity<Time> cadastrarTime(@RequestBody TimeDto timeDto) {
        // Chama o serviço para cadastrar o time com os integrantes
        Time time = timeService.cadastrarTime(timeDto.getData(), timeDto.getIdsIntegrantes());
        return ResponseEntity.ok(time);
    }
}
