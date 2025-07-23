package dev.nasserjr.jwtscaffold.infrastructure.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.nasserjr.jwtscaffold.infrastructure.persistence.entity.UserEntity;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByEmail(String email);
}
