package dev.nasserjr.jwtscaffold.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.nasserjr.jwtscaffold.infrastructure.persistence.adapter.UserRepository;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.repository.SpringDataUserRepository;

@Configuration
public class AppConfig {

  @Bean
  public UserRepository createUserRepositoryBean(SpringDataUserRepository jpa) {
    return new UserRepository(jpa);
  }
}
