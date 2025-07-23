package dev.nasserjr.jwtscaffold.infrastructure.exception;

public class RateLimitExceededException extends RuntimeException {

  private final String identifier;
  private final long retryAfterSeconds;

  public RateLimitExceededException(String message) {
    super(message);
    this.identifier = null;
    this.retryAfterSeconds = 0;
  }

  public RateLimitExceededException(String identifier, long retryAfterSeconds) {
    super(String.format("Rate limit exceeded for %s. Try again in %d seconds", identifier, retryAfterSeconds));
    this.identifier = identifier;
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public RateLimitExceededException(String message, Throwable cause) {
    super(message, cause);
    this.identifier = null;
    this.retryAfterSeconds = 0;
  }

  public String getIdentifier() {
    return identifier;
  }

  public long getRetryAfterSeconds() {
    return retryAfterSeconds;
  }
}