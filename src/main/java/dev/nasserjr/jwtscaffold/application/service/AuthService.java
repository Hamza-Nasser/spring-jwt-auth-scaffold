package dev.nasserjr.jwtscaffold.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.nasserjr.jwtscaffold.application.configservice.IJwtService;
import dev.nasserjr.jwtscaffold.domain.model.AuthenticatedUserPrincipal;
import dev.nasserjr.jwtscaffold.domain.model.User;
import dev.nasserjr.jwtscaffold.domain.repository.IUserRepository;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.entity.UserEntity;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.mapper.UserMapper;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterRequest;
import dev.nasserjr.jwtscaffold.interfaces.dto.auth.RegisterSuccessResponse;

@Service
public class AuthService implements IAuthService {

  private final IUserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final IJwtService jwtService;

  public AuthService(IUserRepository userRepository, PasswordEncoder passwordEncoder, IJwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
  }

  @Override
  public RegisterSuccessResponse register(RegisterRequest request) {

    // final Optional<UserEntity> user =
    // userRepository.findByEmail(request.getEmail());

    // if (user.isPresent()) {
    // throw new UserAlreadyExistsException("email", user.get().getEmail());
    // }

    UserEntity aboutToSaveUser = new UserEntity();

    aboutToSaveUser.setEmail(request.getEmail());
    aboutToSaveUser.setPassword(passwordEncoder.encode(request.getPassword()));
    aboutToSaveUser.setName(request.getName());
    aboutToSaveUser.setPhone(request.getPhone());

    UserEntity savedUser = userRepository.save(aboutToSaveUser);
    User userDto = UserMapper.toDomain(savedUser);

    final String token = jwtService.generateToken(new AuthenticatedUserPrincipal(userDto));

    return new RegisterSuccessResponse(token, userDto);
  }

}
