package br.com.duxusdesafio.exceptions;

public class DateNotFoundException extends RuntimeException{
    public DateNotFoundException() {
        super("Data não encontrada");
    }

    public DateNotFoundException(String message) {
        super(message);
    }

    public DateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateNotFoundException(Throwable cause) {
        super(cause);
    }
}
