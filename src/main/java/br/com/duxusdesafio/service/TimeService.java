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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeService {

    @Autowired
    private IntegranteRepository integranteRepository;

    @Autowired
    private TimeRepository timeRepository;

    public Time cadastrarTime(LocalDate data, List<Long> idsIntegrantes) {
        if (data == null) {
            throw new DateNotFoundException("A data do time não pode ser nula.");
        }

        if (data.isBefore(LocalDate.now())) {
            throw new DateNotFoundException("A data do time não pode ser anterior à data de hoje.");
        }

        if (idsIntegrantes == null || idsIntegrantes.isEmpty()) {
            throw new IntegranteException("A lista de IDs de integrantes não pode ser nula ou vazia.");
        }

        Time time = new Time();
        time.setData(data);

        List<Integrante> integrantes = integranteRepository.findAllById(idsIntegrantes);

        if (integrantes.size() != idsIntegrantes.size()) {
            throw new IntegranteException("Um ou mais IDs de integrantes são inválidos.");
        }

        List<ComposicaoTime> composicoes = integrantes.stream()
                .map(integrante -> new ComposicaoTime(time, integrante))
                .collect(Collectors.toList());

        time.setComposicaoTime(composicoes);

        return timeRepository.save(time);
    }

    public List<Time> listarTodosOsTimes() {
        List<Time> times = timeRepository.findAll();
        if (times.isEmpty()) {
            throw new NotFoundException("Nenhum time encontrado.");
        }
        return times;
    }

    public Time buscarTimePorId(Long id) {
        return timeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Time não encontrado com o ID: " + id));
    }

    public Time atualizarTime(Long id, LocalDate data, List<Long> idsIntegrantes) {
        if (data == null) {
            throw new NullTimeException("A data do time não pode ser nula.");
        }
        if (idsIntegrantes == null || idsIntegrantes.contains(null)) {
            throw new IntegranteException("A lista de IDs de integrantes não pode ser nula ou conter valores nulos.");
        }

        Time timeExistente = buscarTimePorId(id);
        timeExistente.setData(data);

        List<Integrante> integrantes = integranteRepository.findAllById(idsIntegrantes);
        if (integrantes.size() != idsIntegrantes.size()) {
            throw new IntegranteException("Um ou mais IDs de integrantes são inválidos.");
        }

        List<ComposicaoTime> composicoes = integrantes.stream()
                .map(integrante -> new ComposicaoTime(timeExistente, integrante))
                .collect(Collectors.toList());

        timeExistente.setComposicaoTime(composicoes);

        return timeRepository.save(timeExistente);
    }

    public void deletarTime(Long id) throws NotFoundException{
        Time time = buscarTimePorId(id);
        timeRepository.delete(time);
    }
}
