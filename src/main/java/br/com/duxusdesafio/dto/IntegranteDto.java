package br.com.duxusdesafio.dto;

import javax.validation.constraints.NotNull;

public class IntegranteDto {

        @NotNull
        private String franquia;

        @NotNull
        private String nome;

        @NotNull
        private String funcao;

        public @NotNull String getFranquia() {
                return franquia;
        }

        public void setFranquia(@NotNull String franquia) {
                this.franquia = franquia;
        }

        public @NotNull String getNome() {
                return nome;
        }

        public void setNome(@NotNull String nome) {
                this.nome = nome;
        }

        public @NotNull String getFuncao() {
                return funcao;
        }

        public void setFuncao(@NotNull String funcao) {
                this.funcao = funcao;
        }

}
