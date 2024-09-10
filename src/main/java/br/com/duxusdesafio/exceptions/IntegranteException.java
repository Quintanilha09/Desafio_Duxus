package br.com.duxusdesafio.exceptions;

public class IntegranteException extends RuntimeException {
  public IntegranteException() {
    super("Ocorreu um erro envolvendo Integrante");
  }

  public IntegranteException(String message) {
    super(message);
  }

  public IntegranteException(String message, Throwable cause) {
    super(message, cause);
  }

  public IntegranteException(Throwable cause) {
    super(cause);
  }
}
