package dev.nasserjr.jwtscaffold.infrastructure.persistence.mapper;

import dev.nasserjr.jwtscaffold.domain.model.User;
import dev.nasserjr.jwtscaffold.infrastructure.persistence.entity.UserEntity;

public class UserMapper {
  public static UserEntity toEntity(User user) {
    UserEntity entity = new UserEntity();
    entity.setId(user.getId());
    entity.setEmail(user.getEmail());
    entity.setName(user.getName());
    entity.setPassword(user.getPassword());
    entity.setPhone(user.getPhone());
    return entity;
  }

  public static User toDomain(UserEntity entity) {
    User user = new User();
    user.setId(entity.getId());
    user.setEmail(entity.getEmail());
    user.setName(entity.getName());
    user.setPassword(entity.getPassword());
    user.setPhone(entity.getPhone());
    return user;
  }
}
