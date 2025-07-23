package dev.nasserjr.jwtscaffold.application.configservice;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import dev.nasserjr.jwtscaffold.domain.model.AuthenticatedUserPrincipal;
import dev.nasserjr.jwtscaffold.domain.model.User;
import dev.nasserjr.jwtscaffold.domain.repository.IUserRepository;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.mapper.UserMapper;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final IUserRepository userRepository;

  public UserDetailsServiceImpl(IUserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userRepository.findByEmail(username).map(UserMapper::toDomain);
    if (user.isEmpty()) {
      throw new UsernameNotFoundException("Email is not found");
    }

    return new AuthenticatedUserPrincipal(user.get());

  }
}
