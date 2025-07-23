package dev.nasserjr.jwtscaffold.interfaces.dto.auth;

import dev.nasserjr.jwtscaffold.domain.model.User;

public class RegisterSuccessResponse implements IRegisterResponse {
  private final String token;
  private final User user;

  public RegisterSuccessResponse(String token, User user) {
    this.token = token;
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public User getUser() {
    return user;
  }
}
