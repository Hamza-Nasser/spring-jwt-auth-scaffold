package dev.nasserjr.jwtscaffold.interfaces.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.nasserjr.jwtscaffold.application.configservice.IJwtService;
import dev.nasserjr.jwtscaffold.application.service.IAuthService;
import dev.nasserjr.jwtscaffold.application.service.authvalidation.IRegistrationValidationService;
import dev.nasserjr.jwtscaffold.domain.model.AuthenticatedUserPrincipal;
import dev.nasserjr.jwtscaffold.domain.model.User;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.ILoginResponse;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.IRegisterResponse;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.LoginFailResponse;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.LoginRequest;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.LoginSuccessResponse;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterFailResponse;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

  final private IAuthService authService;

  final private IRegistrationValidationService registerValidationService;

  final private AuthenticationManager authenticationManager;

  final private IJwtService jwtService;

  public AuthController(IAuthService authService, AuthenticationManager authenticationManager, IJwtService jwtService,
      IRegistrationValidationService registrationValidationService) {
    this.authService = authService;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.registerValidationService = registrationValidationService;
  }

  @PostMapping("/register")
  public ResponseEntity<IRegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest,
      BindingResult result) {

    if (result.hasErrors()) {
      List<String> errorMessages = result.getFieldErrors()
          .stream()
          .map(error -> error.getField() + ": " + error.getDefaultMessage())
          .collect(Collectors.toList());

      result.getGlobalErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));

      RegisterFailResponse response = new RegisterFailResponse(errorMessages);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    registerValidationService.validateRegistrationRequest(registerRequest);

    IRegisterResponse registerResponse = authService.register(registerRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<ILoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {

    // TODO: move all this logic to auth service
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
      if (!authentication.isAuthenticated()) {
        return ResponseEntity.status(401).body(new LoginFailResponse("Authentication failed"));
      }

      final AuthenticatedUserPrincipal authenticatedUser = (AuthenticatedUserPrincipal) authentication.getPrincipal();
      String token = jwtService.generateToken(authenticatedUser);
      return ResponseEntity.ok(new LoginSuccessResponse(token, authenticatedUser.getUser()));

    } catch (Exception e) {
      System.out.println(e);
      return ResponseEntity.status(401).body(new LoginFailResponse("Error authenticating"));
    }

  }

  @GetMapping("/user")
  public ResponseEntity<User> getUser() {
    final AuthenticatedUserPrincipal authenticatedUser = (AuthenticatedUserPrincipal) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();

    return ResponseEntity.ok(authenticatedUser.getUser());
  }

}
