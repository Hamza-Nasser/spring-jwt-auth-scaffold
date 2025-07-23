package dev.nasserjr.jwtscaffold.interfaces.dto.auth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterFailResponse implements IRegisterResponse {

  @JsonProperty("errors")
  private List<String> errorMessages;

  public List<String> getErrorMessages() {
    return errorMessages;
  }

  public void setErrorMessages(List<String> errorMessages) {
    this.errorMessages = errorMessages;
  }

  public RegisterFailResponse(List<String> errorMessages) {
    this.errorMessages = errorMessages;
  }
}
