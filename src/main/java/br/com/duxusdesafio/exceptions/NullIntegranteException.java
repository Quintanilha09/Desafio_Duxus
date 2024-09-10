package br.com.duxusdesafio.exceptions;

public class NullIntegranteException extends RuntimeException {
    public NullIntegranteException() {
        super("O Integrante ou lista de Integrantes é nula");
    }

    public NullIntegranteException(String message) {
        super(message);
    }

    public NullIntegranteException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullIntegranteException(Throwable cause) {
        super(cause);
    }
}
