package br.com.duxusdesafio.exceptions;

public class NullDateException extends RuntimeException {
  public NullDateException() {
    super("A data é nula");
  }

  public NullDateException(String message) {
    super(message);
  }

  public NullDateException(String message, Throwable cause) {
    super(message, cause);
  }

  public NullDateException(Throwable cause) {
    super(cause);
  }
}
