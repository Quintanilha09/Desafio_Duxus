package br.com.duxusdesafio.response;

import java.time.LocalDate;
import java.util.List;

public class TimeDaDataResponse {

    private LocalDate data;
    private List<String> integrantes;

    public TimeDaDataResponse(LocalDate data, List<String> integrantes) {
        this.data = data;
        this.integrantes = integrantes;
    }

    public LocalDate getData() {
        return data;
    }

    public List<String> getIntegrantes() {
        return integrantes;
    }

}
