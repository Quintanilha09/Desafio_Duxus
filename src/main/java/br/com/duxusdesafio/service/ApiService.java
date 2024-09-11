package br.com.duxusdesafio.service;

import br.com.duxusdesafio.exceptions.DateNotFoundException;
import br.com.duxusdesafio.exceptions.NotFoundException;
import br.com.duxusdesafio.exceptions.NullTimeException;
import br.com.duxusdesafio.model.ComposicaoTime;
import br.com.duxusdesafio.model.Integrante;
import br.com.duxusdesafio.model.Time;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public void validaData(LocalDate data) {
        if (data == null) {
            throw new DateNotFoundException("A data do time não pode ser nula ou anterior a 2024.");
        }
    }

    public void validaData(LocalDate dataInicial, LocalDate dataFinal) {
        if (dataInicial == null || dataFinal == null) {
            throw new DateNotFoundException("As datas de início e fim não podem ser nulas.");
        }
    }

    public void validaTimesFiltrados(List<Time> timesFiltrados) {
        if (timesFiltrados == null || timesFiltrados.isEmpty()) {
            throw new NotFoundException("Nenhum time encontrado no período especificado.");
        }
    }

    public void validaTodosOsTimes(List<Time> times) {
        if (times == null || times.isEmpty()) {
            throw new NullTimeException("A lista de todos os times não pode ser nula ou vazia.");
        }
    }

    /**
     * Vai retornar uma lista com os nomes dos integrantes do time daquela data
     */
    public List<String> timeDaData(LocalDate data, List<Time> todosOsTimes){
        validaTodosOsTimes(todosOsTimes);
        validaData(data);
        return todosOsTimes.stream()
                .filter(time -> time.getData().equals(data))
                .findFirst()
                .map(time -> time.getComposicaoTime().stream()
                        .map(composicao -> composicao.getIntegrante().getNome())
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new DateNotFoundException("Data " + data + " não encontrada."));
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
     * Este método foi criado para simplificar todos os métodos que necessitam de filtragem por período
     * Seguindo o primeiro princípio do SOLID, Single Responsability Principle
     */
    private List<Time> filtrarTimesPorPeriodo(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        validaTodosOsTimes(todosOsTimes);
        validaData(dataInicial, dataFinal);

        List<Time> timesFiltrados = todosOsTimes.stream()
                .filter(time -> {
                    LocalDate dataTime = time.getData();
                    boolean depoisDaInicial = (dataInicial == null || !dataTime.isBefore(dataInicial));
                    boolean antesDaFinal = (dataFinal == null || !dataTime.isAfter(dataFinal));
                    return depoisDaInicial && antesDaFinal;
                })
                .collect(Collectors.toList());
        if (timesFiltrados.isEmpty()) {
            throw new NotFoundException("Nenhum time encontrado no período especificado.");
        }
        return timesFiltrados;
    }

    /**
     * Conta as aparições dos integrantes nos times filtrados.
     * Este método foi criado para simplificar o método 'integranteMaisUsado'
     * Seguindo o primeiro princípio do SOLID, Single Responsability Principle
     */
    private Map<Integrante, Long> contarAparicoes(List<Time> timesFiltrados) {
        validaTimesFiltrados(timesFiltrados);

        Map<Integrante, Long> contadorDeAparicoes = timesFiltrados.stream()
                .flatMap(time -> time.getComposicaoTime().stream())
                .collect(Collectors.groupingBy(
                        ComposicaoTime::getIntegrante,
                        Collectors.counting()
                ));

        if (contadorDeAparicoes.isEmpty()) {
            throw new NotFoundException("Nenhum integrante encontrado na contagem de aparições.");
        }

        return contadorDeAparicoes;
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
        List<Time> timesFiltrados = filtrarTimesPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        validaTimesFiltrados(timesFiltrados);
        validaData(dataInicial, dataFinal);

        if (timesFiltrados.isEmpty()) {
            throw new NotFoundException("Nenhum time encontrado no período especificado.");
        }

        Time timeMaisComum = timesFiltrados.stream()
                .collect(Collectors.groupingBy(time -> time, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NotFoundException("Nenhum time mais comum encontrado."));

        return timeMaisComum.getComposicaoTime().stream()
                .map(composicao -> composicao.getIntegrante().getNome())
                .collect(Collectors.toList());
    }

    /**
     * Vai retornar a função mais comum nos times dentro do período
     */
    public String funcaoMaisComum(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        List<Time> timesFiltrados = filtrarTimesPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        validaTimesFiltrados(timesFiltrados);
        validaData(dataInicial, dataFinal);

        return timesFiltrados.stream()
                .flatMap(time -> time.getComposicaoTime().stream())  // Obtém a composição de cada time
                .map(composicao -> composicao.getIntegrante().getFuncao())  // Mapeia para a função do integrante
                .collect(Collectors.groupingBy(funcao -> funcao, Collectors.counting()))  // Conta as ocorrências de cada função
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())  // Encontra a função mais comum
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NotFoundException("Nenhuma função comum encontrada no período especificado."));
    }

    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public String franquiaMaisFamosa(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> timesFiltrados = filtrarTimesPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        validaTimesFiltrados(timesFiltrados);
        validaData(dataInicial, dataFinal);

        return timesFiltrados.stream()
                .collect(Collectors.groupingBy(
                        time -> time.getComposicaoTime().stream()
                                .map(composicao -> composicao.getIntegrante().getFranquia())
                                .findFirst()
                                .orElseThrow(() -> new NotFoundException("Nenhuma franquia encontrada para esse time.")),
                        Collectors.counting()
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new NotFoundException("Nenhuma franquia mais famosa encontrada no período especificado."));
    }


    /**
     * Vai retornar o nome da Franquia mais comum nos times dentro do período
     */
    public Map<String, Long> contagemPorFranquia(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes){
        List<Time> timesFiltrados = filtrarTimesPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        validaTimesFiltrados(timesFiltrados);
        validaData(dataInicial, dataFinal);

        Map<String, Long> contagemPorFranquia = timesFiltrados.stream()
                .flatMap(time -> time.getComposicaoTime().stream())
                .collect(Collectors.groupingBy(
                        composicao -> composicao.getIntegrante().getFranquia(),
                        Collectors.counting()
                ));

        if (contagemPorFranquia.isEmpty()) {
            throw new NotFoundException("Nenhuma franquia encontrada no período especificado.");
        }

        return contagemPorFranquia;
    }

    /**
     * Vai retornar o número (quantidade) de Funções dentro do período
     */
    public Map<String, Long> contagemPorFuncao(LocalDate dataInicial, LocalDate dataFinal, List<Time> todosOsTimes) {
        List<Time> timesFiltrados = filtrarTimesPorPeriodo(dataInicial, dataFinal, todosOsTimes);
        validaTimesFiltrados(timesFiltrados);
        validaData(dataInicial, dataFinal);

        Map<String, Long> contagemPorFuncao = timesFiltrados.stream()
                .flatMap(time -> time.getComposicaoTime().stream())
                .collect(Collectors.groupingBy(
                        composicao -> composicao.getIntegrante().getFuncao(),
                        Collectors.counting()
                ));

        if (contagemPorFuncao.isEmpty()) {
            throw new NotFoundException("Nenhuma função encontrada no período especificado.");
        }

        return contagemPorFuncao;
    }

}
