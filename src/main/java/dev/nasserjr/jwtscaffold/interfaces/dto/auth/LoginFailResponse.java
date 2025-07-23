package dev.nasserjr.jwtscaffold.interfaces.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginFailResponse implements ILoginResponse {

  @JsonProperty("message")
  private final String errorMessage;

  public LoginFailResponse(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

}
