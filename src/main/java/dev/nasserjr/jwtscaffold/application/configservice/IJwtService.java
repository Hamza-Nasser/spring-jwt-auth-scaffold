package dev.nasserjr.jwtscaffold.application.configservice;

import org.springframework.security.core.userdetails.UserDetails;

public interface IJwtService {
  String generateToken(UserDetails userDetails);

  boolean isTokenValid(String token, UserDetails userDetails);

  String extractEmail(String token);
}
