package dev.nasserjr.jwtscaffold.application.service;

import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterRequest;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterSuccessResponse;

public interface IAuthService {
  RegisterSuccessResponse register(RegisterRequest request);

  // User getUser();
}
