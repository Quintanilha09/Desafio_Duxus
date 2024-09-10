package br.com.duxusdesafio.exceptions;

public class NullTimeException extends RuntimeException {
    public NullTimeException() {
        super("O Time ou lista de Time é nula");
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
