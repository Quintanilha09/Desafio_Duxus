package br.com.duxusdesafio.exceptions;

public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException() {
        super("Data não encontrada");
    }

    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataNotFoundException(Throwable cause) {
        super(cause);
    }
}
