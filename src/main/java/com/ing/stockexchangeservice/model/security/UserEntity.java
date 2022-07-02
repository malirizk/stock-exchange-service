package com.ing.stockexchangeservice.model.security;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String email;

  private String password;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<UserRole> roles;
}
