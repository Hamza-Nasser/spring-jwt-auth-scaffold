package dev.nasserjr.jwtscaffold.infrastructure.persistence.adapter;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import dev.nasserjr.jwtscaffold.domain.repository.IUserRepository;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.entity.UserEntity;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.repository.SpringDataUserRepository;

@Repository
public class UserRepository implements IUserRepository {

  private final SpringDataUserRepository springDataUserRepository;

  public UserRepository(SpringDataUserRepository springDataUserRepository) {
    this.springDataUserRepository = springDataUserRepository;
  }

  @Override
  public Optional<UserEntity> findByEmail(String email) {
    return springDataUserRepository.findByEmail(email);
  }

  @Override
  public UserEntity save(UserEntity user) {
    return springDataUserRepository.save(user);
  }
}
