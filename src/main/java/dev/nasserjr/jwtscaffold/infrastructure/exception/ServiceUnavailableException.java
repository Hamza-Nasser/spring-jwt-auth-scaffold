package dev.nasserjr.jwtscaffold.infrastructure.exception;

public class ServiceUnavailableException extends RuntimeException {

  private final String serviceName;

  public ServiceUnavailableException(String message) {
    super(message);
    this.serviceName = null;
  }

  public ServiceUnavailableException(String serviceName, String message) {
    super(message);
    this.serviceName = serviceName;
  }

  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
    this.serviceName = null;
  }

  public String getServiceName() {
    return serviceName;
  }
}
