package dev.nasserjr.jwtscaffold.infrastructure.exception;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeException {

  private final List<String> errors;

  public ValidationException(String message) {
    super(message);
    this.errors = List.of(message);
  }

  public ValidationException(List<String> errors) {
    super(String.join(", ", errors));
    this.errors = new ArrayList<>(errors);
  }

  public ValidationException(String message, Throwable cause) {
    super(message, cause);
    this.errors = List.of(message);
  }

  public List<String> getErrors() {
    return Collections.unmodifiableList(errors);
  }
}