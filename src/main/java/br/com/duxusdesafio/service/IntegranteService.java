package br.com.duxusdesafio.service;

import br.com.duxusdesafio.dto.IntegranteDto;
import br.com.duxusdesafio.exceptions.IntegranteException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullIntegranteException;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.repository.IntegranteRepository;
import br.com.duxusdesafio.repository.TimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IntegranteService {

    @Autowired
    private IntegranteRepository integranteRepository;

    @Autowired
    private TimeRepository timeRepository;

    public void validaIntegrantes(List<Integrante> integrantes) {
        if (integrantes == null || integrantes.isEmpty()) {
            throw new NullIntegranteException("A lista de integrantes é nula");
        }
    }

    public Integrante cadastrarIntegrante(IntegranteDto integranteDto) throws IntegranteException{
        Integrante integrante = new Integrante();
        integrante.setFranquia(integranteDto.getFranquia());
        integrante.setNome(integranteDto.getNome());
        integrante.setFuncao(integranteDto.getFuncao());

        return integranteRepository.save(integrante);
    }

    public List<Integrante> listarIntegrantes() {
        List<Integrante> integrantes = integranteRepository.findAll();
        validaIntegrantes(integrantes);
        return integranteRepository.findAll();
    }

    public Optional<Integrante> buscarIntegrantePorId(Long id) {
        if (id == null) {
            throw new NullIntegranteException("O id do integrante é nulo");
        }
        return integranteRepository.findById(id);
    }

    public Integrante atualizarIntegrante(Long id, IntegranteDto integranteDto) {
        Integrante integrante = integranteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Integrante não encontrado"));

        integrante.setFranquia(integranteDto.getFranquia());
        integrante.setNome(integranteDto.getNome());
        integrante.setFuncao(integranteDto.getFuncao());

        return integranteRepository.save(integrante);
    }

    public String deletarIntegrante(Long id) {
        if (!integranteRepository.existsById(id)) {
            throw new NotFoundException("Integrante não encontrado");
        }
        integranteRepository.deleteById(id);
        return "Integrante deletado com sucesso!";
    }

}
