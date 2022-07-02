package com.ing.stockexchangeservice.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserDto {

  private String id;
  private String name;
  private String email;
  private String password;
  private Set<String> roles;
}
