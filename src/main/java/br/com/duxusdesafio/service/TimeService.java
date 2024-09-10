package br.com.duxusdesafio.service;

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
        Time time = new Time();
        time.setData(data);

        List<Integrante> integrantes = integranteRepository.findAllById(idsIntegrantes);

        List<ComposicaoTime> composicoes = integrantes.stream()
                .map(integrante -> new ComposicaoTime(time, integrante))
                .collect(Collectors.toList());

        time.setComposicaoTime(composicoes);

        return timeRepository.save(time);
    }
}
