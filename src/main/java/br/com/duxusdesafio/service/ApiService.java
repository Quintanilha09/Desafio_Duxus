package br.com.duxusdesafio.service;

import br.com.duxusdesafio.exceptions.DataNotFoundException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.TimeNullPointerException;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service que possuirá as regras de negócio para o processamento dos dados
 * solicitados no desafio!
 *
 * @author carlosau
 */
@Service
public class ApiService {

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time daquela data
     */
    public List<String> timeDaData(LocalDate data, List<Time> todosOsTimes){
        return todosOsTimes.stream()
                .filter(time -> time.getData().equals(data))
                .findFirst()
                .map(time -> time.getComposicaoTime().stream()
                        .map(composicao -> composicao.getIntegrante().getNome())
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new DataNotFoundException("Data " + data + " não encontrada."));
    }

    /**
     * Vai retornar o integrante que tiver presente na maior quantidade de times
     * dentro do período
     */
    public Integrante integranteMaisUsado(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // 1. Filtrar os times
        List<Time> timesFiltrados = filtrarTimesPorPeriodo(dataInicial, dataFinal, todosOsTimes);

        // 2. Contar as aparições dos integrantes
        Map<Integrante, Long> contadorDeAparicoes = contarAparicoes(timesFiltrados);

        // 3. Encontrar o integrante mais usado
        return encontrarIntegranteMaisUsado(contadorDeAparicoes);
    }

    /**
     * Filtra os times de acordo com o período especificado.
     * Este método foi criado para simplificar o método 'integranteMaisUsado'
     * Seguindo o primeiro princípio do SOLID, Single Responsability Principle
     */
    private List<Time> filtrarTimesPorPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        if (todosOsTimes == null || todosOsTimes.isEmpty()) {
            throw new TimeNullPointerException("A lista de todos os times não pode ser nula ou vazia.");
        }

        return todosOsTimes.stream()
                .filter(time -> {
                    LocalDate dataTime = time.getData();
                    boolean depoisDaInicial = (dataInicial == null || !dataTime.isBefore(dataInicial));
                    boolean antesDaFinal = (dataFinal == null || !dataTime.isAfter(dataFinal));
                    return depoisDaInicial && antesDaFinal;
                })
                .collect(Collectors.toList());
    }

    /**
     * Conta as aparições dos integrantes nos times filtrados.
     * Este método foi criado para simplificar o método 'integranteMaisUsado'
     * Seguindo o primeiro princípio do SOLID, Single Responsability Principle
     */
    private Map<Integrante, Long> contarAparicoes(List<Time> timesFiltrados) {
        if (timesFiltrados == null || timesFiltrados.isEmpty()) {
            throw new TimeNullPointerException("A lista de times filtrados não pode ser nula ou vazia.");
        }

        return timesFiltrados.stream()
                .flatMap(time -> time.getComposicaoTime().stream())  // Achatar para ter uma lista de ComposicaoTime
                .collect(Collectors.groupingBy(
                        ComposicaoTime::getIntegrante,
                        Collectors.counting()
                ));
    }

    /**
     * Encontra o integrante mais usado com base na contagem de aparições.
     * Este método foi criado para simplificar o método 'integranteMaisUsado'
     * Seguindo o primeiro princípio do SOLID, Single Responsability Principle
     */
    private Integrante encontrarIntegranteMaisUsado(Map<Integrante, Long> contadorDeAparicoes) {
        if (contadorDeAparicoes == null || contadorDeAparicoes.isEmpty()) {
            throw new NotFoundException("Nenhuma aparição encontrada no mapa de contagem de aparições.");
        }

        return contadorDeAparicoes.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NotFoundException("Nenhum integrante encontrado após a verificação de aparições."));
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time mais comum
     * dentro do período
     */
    public List<String> timeMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar a função mais comum nos times dentro do período
     */
    public String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        // TODO Implementar método seguindo as instruções!
        return null;
    }


    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        // TODO Implementar método seguindo as instruções!
        return null;
    }

}
