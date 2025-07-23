package dev.nasserjr.jwtscaffold.domain.repository;

import java.util.Optional;

import dev.nasserjr.jwtscaffold.infrastructure.persistence.entity.UserEntity;

public interface IUserRepository {
  Optional<UserEntity> findByEmail(String email);

  UserEntity save(UserEntity user);
}
