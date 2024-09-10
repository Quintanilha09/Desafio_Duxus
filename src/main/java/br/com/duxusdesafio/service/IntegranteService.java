package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.IntegranteDto;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class IntegranteService {

    @Autowired
    private IntegranteRepository integranteRepository;

    @Autowired
    private TimeRepository timeRepository;

    public Integrante cadastrarIntegrante(IntegranteDto integranteDto) {

        Optional<Integrante> existente = integranteRepository.findByNome(integranteDto.getNome());
        if (existente.isPresent()) {
            throw new IntegranteException("Integrante com o mesmo nome já existe");
        }

        Integrante integrante = new Integrante();
        integrante.setFranquia(integranteDto.getFranquia());
        integrante.setNome(integranteDto.getNome());
        integrante.setFuncao(integranteDto.getFuncao());

        return integranteRepository.save(integrante);
    }

    public List<Integrante> listarIntegrantes() {
        return integranteRepository.findAll();
    }

    public Optional<Integrante> buscarIntegrantePorId(Long id) {
        return integranteRepository.findById(id);
    }

    public Integrante atualizarIntegrante(Long id, IntegranteDto integranteDto) {
        Optional<Integrante> integranteOpt = integranteRepository.findById(id);
        if (!integranteOpt.isPresent()) {
            throw new NotFoundException("Integrante não encontrado");
        }

        Integrante integrante = integranteOpt.get();
        integrante.setFranquia(integranteDto.getFranquia());
        integrante.setNome(integranteDto.getNome());
        integrante.setFuncao(integranteDto.getFuncao());

        return integranteRepository.save(integrante);
    }

    public String deletarIntegrante(Long id) {
        if (!integranteRepository.existsById(id)) {
            throw new IntegranteException("Integrante não encontrado");
        }
        integranteRepository.deleteById(id);
        return "Integrante deletado com sucesso!";
    }

}
