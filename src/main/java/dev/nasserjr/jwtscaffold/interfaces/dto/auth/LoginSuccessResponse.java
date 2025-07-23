package dev.nasserjr.jwtscaffold.interfaces.dto.auth;

import dev.nasserjr.jwtscaffold.domain.model.User;

public class LoginSuccessResponse implements ILoginResponse {
  private final String token;
  private final User user;

  public LoginSuccessResponse(String token, User user) {
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
