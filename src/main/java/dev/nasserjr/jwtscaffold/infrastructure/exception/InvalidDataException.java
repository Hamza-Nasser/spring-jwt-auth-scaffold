package dev.nasserjr.jwtscaffold.infrastructure.exception;

import java.util.List;

public class InvalidDataException extends ValidationException {

  public InvalidDataException(String message) {
    super(message);
  }

  public InvalidDataException(List<String> errors) {
    super(errors);
  }

  public InvalidDataException(String message, Throwable cause) {
    super(message, cause);
  }
}