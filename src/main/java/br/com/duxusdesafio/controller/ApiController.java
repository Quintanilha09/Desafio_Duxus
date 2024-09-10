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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class ApiController {

    @Autowired
    private ApiService apiService;

    @Autowired
    private TimeService timeService;

    @GetMapping("/times/data")
    public ResponseEntity<Object> getTimesDaData(@RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            List<String> nomesIntegrantes = apiService.timeDaData(data, todosOsTimes);

            TimeDaDataResponse response = new TimeDaDataResponse(data, nomesIntegrantes);

            return ResponseEntity.ok(response);
        } catch (DateNotFoundException | NullTimeException | NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/integrante-mais-usado")
    public ResponseEntity<Object> getIntegranteMaisUsado(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            Integrante integranteMaisUsado = apiService.integranteMaisUsado(dataInicial, dataFinal, todosOsTimes);
            return ResponseEntity.ok(integranteMaisUsado);
        } catch (NotFoundException | DateNotFoundException | NullTimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/time-mais-comum")
    public ResponseEntity<Object> getTimeMaisComum(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            List<String> nomesIntegrantes = apiService.timeMaisComum(dataInicial, dataFinal, todosOsTimes);
            return ResponseEntity.ok(nomesIntegrantes);
        } catch (NotFoundException | DateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/funcao-mais-comum")
    public ResponseEntity<Object> getFuncaoMaisComum(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            String funcaoMaisComum = apiService.funcaoMaisComum(dataInicial, dataFinal, todosOsTimes);
            FuncaoMaisComumResponse response = new FuncaoMaisComumResponse(funcaoMaisComum); //response criada para mostrar dados específicos
            return ResponseEntity.ok(response);
        } catch (NotFoundException | DateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/franquia-mais-famosa")
    public ResponseEntity<Object> getFranquiaMaisFamosa(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            String franquiaMaisFamosa = apiService.franquiaMaisFamosa(dataInicial, dataFinal, todosOsTimes);
            return ResponseEntity.ok(franquiaMaisFamosa);
        } catch (NotFoundException | DateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/contagem-por-franquia")
    public ResponseEntity<Object> getContagemPorFranquia(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            Map<String, Long> contagemPorFranquia = apiService.contagemPorFranquia(dataInicial, dataFinal, todosOsTimes);
            return ResponseEntity.ok(contagemPorFranquia);
        } catch (NotFoundException | DateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/contagem-por-funcao")
    public ResponseEntity<Object> getContagemPorFuncao(
            @RequestParam("dataInicial") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicial,
            @RequestParam("dataFinal") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinal) {
        try {
            List<Time> todosOsTimes = timeService.listarTodosOsTimes();
            Map<String, Long> contagemPorFuncao = apiService.contagemPorFuncao(dataInicial, dataFinal, todosOsTimes);
            return ResponseEntity.ok(contagemPorFuncao);
        } catch (NotFoundException | DateNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
