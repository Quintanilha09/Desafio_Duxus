package br.com.duxusdesafio.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class TimeDto {

    @NotNull
    private LocalDate data;

    @NotNull
    private List<Long> idsIntegrantes;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<Long> getIdsIntegrantes() {
        return idsIntegrantes;
    }

    public void setIdsIntegrantes(List<Long> idsIntegrantes) {
        this.idsIntegrantes = idsIntegrantes;
    }

}
