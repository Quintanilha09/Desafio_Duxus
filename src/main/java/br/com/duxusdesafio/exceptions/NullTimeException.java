package br.com.duxusdesafio.exceptions;

public class NullTimeException extends RuntimeException {
    public NullTimeException() {
        super("A lista de times é nula");
    }

    public NullTimeException(String message) {
        super(message);
    }

    public NullTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullTimeException(Throwable cause) {
        super(cause);
    }
}
