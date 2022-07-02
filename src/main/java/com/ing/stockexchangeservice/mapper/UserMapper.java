package com.ing.stockexchangeservice.mapper;

import com.ing.stockexchangeservice.model.security.User;
import com.ing.stockexchangeservice.model.security.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  User toUser(UserEntity userEntity);

  UserEntity toUserEntity(User user);
}
