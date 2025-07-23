package dev.nasserjr.jwtscaffold.infrastructure.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterFailResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(InvalidDataException.class)
  public ResponseEntity<RegisterFailResponse> handleInvalidDataException(InvalidDataException e) {
    log.warn("Invalid data provided: {}", e.getMessage());
    RegisterFailResponse response = new RegisterFailResponse(e.getErrors());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<RegisterFailResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
    log.warn("User already exists: {}", e.getMessage());
    RegisterFailResponse response = new RegisterFailResponse(List.of(e.getMessage()));
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  @ExceptionHandler(RateLimitExceededException.class)
  public ResponseEntity<RegisterFailResponse> handleRateLimitExceededException(RateLimitExceededException e) {
    log.warn("Rate limit exceeded: {}", e.getMessage());
    RegisterFailResponse response = new RegisterFailResponse(List.of(e.getMessage()));

    HttpHeaders headers = new HttpHeaders();
    if (e.getRetryAfterSeconds() > 0) {
      headers.add("Retry-After", String.valueOf(e.getRetryAfterSeconds()));
    }

    return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
        .headers(headers)
        .body(response);
  }

  @ExceptionHandler(ServiceUnavailableException.class)
  public ResponseEntity<RegisterFailResponse> handleServiceUnavailableException(ServiceUnavailableException e) {
    log.error("Service unavailable: {}", e.getMessage());
    RegisterFailResponse response = new RegisterFailResponse(List.of(e.getMessage()));
    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<RegisterFailResponse> handleValidationException(MethodArgumentNotValidException e) {
    List<String> errors = e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.toList());

    // Add global errors
    e.getBindingResult()
        .getGlobalErrors()
        .forEach(error -> errors.add(error.getDefaultMessage()));

    log.warn("Bean validation failed: {}", String.join(", ", errors));
    RegisterFailResponse response = new RegisterFailResponse(errors);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<RegisterFailResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    String rootMessage = getRootCauseMessage(ex).toLowerCase();

    if (rootMessage.contains("uk_user_phone")) {
      RegisterFailResponse response = new RegisterFailResponse(List.of("Phone number already exists."));
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(response);
    }
    if (rootMessage.contains("uk_user_email")) {
      RegisterFailResponse response = new RegisterFailResponse(List.of("Email already exists."));
      return ResponseEntity.status(HttpStatus.CONFLICT)
          .body(response);
    }

    RegisterFailResponse response = new RegisterFailResponse(List.of("A data integrity violation occurred."));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(response);
  }

  private String getRootCauseMessage(Throwable throwable) {
    Throwable cause = throwable;
    while (cause.getCause() != null && cause != cause.getCause()) {
      cause = cause.getCause();
    }
    return cause.getMessage();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<RegisterFailResponse> handleGenericException(Exception e) {
    log.error("Unexpected error occurred", e);
    RegisterFailResponse response = new RegisterFailResponse(
        List.of("An unexpected error occurred. Please try again later."));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}