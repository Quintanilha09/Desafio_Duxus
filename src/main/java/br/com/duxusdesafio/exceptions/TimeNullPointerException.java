package br.com.duxusdesafio.exceptions;

public class TimeNullPointerException extends RuntimeException {
    public TimeNullPointerException() {
        super("A lista de times é nula");
    }

    public TimeNullPointerException(String message) {
        super(message);
    }

    public TimeNullPointerException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeNullPointerException(Throwable cause) {
        super(cause);
    }
}
