package com.ing.stockexchangeservice.service;

import com.ing.stockexchangeservice.common.token.JwtEndpointAccessTokenGenerator;
import com.ing.stockexchangeservice.dto.security.LoginRequestDto;
import com.ing.stockexchangeservice.dto.security.SignupRequestDto;
import com.ing.stockexchangeservice.dto.security.TokenResponse;
import com.ing.stockexchangeservice.exception.security.InvalidLoginException;
import com.ing.stockexchangeservice.exception.security.UserAlreadyExistException;
import com.ing.stockexchangeservice.mapper.UserMapper;
import com.ing.stockexchangeservice.model.security.User;
import com.ing.stockexchangeservice.model.security.UserEntity;
import com.ing.stockexchangeservice.model.security.UserRole;
import com.ing.stockexchangeservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final JwtEndpointAccessTokenGenerator jwtEndpointAccessTokenGenerator;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(username)
        .map(userMapper::toUser)
        .orElseThrow(InvalidLoginException::new);
  }

  public User addUser(SignupRequestDto signupRequestDto) {
    Optional.ofNullable(userRepository.findByEmail(signupRequestDto.getEmail()))
        .orElseThrow(UserAlreadyExistException::new);

    User user =
        User.builder()
            .email(signupRequestDto.getEmail())
            .password(passwordEncoder.encode(signupRequestDto.getPassword()))
            .roles((Set<UserRole>) Collections.singleton(UserRole.ROLE_USER))
            .build();

    UserEntity savedUser = userRepository.save(userMapper.toUserEntity(user));
    return userMapper.toUser(savedUser);
  }

  public TokenResponse login(LoginRequestDto loginRequestDto) {
    User user =
        userRepository
            .findByEmail(loginRequestDto.getEmail())
            .map(userMapper::toUser)
            .orElseThrow(InvalidLoginException::new);

    if (passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword()) == false) {
      throw new InvalidLoginException();
    }

    return TokenResponse.builder()
        .accessToken(
            jwtEndpointAccessTokenGenerator.createAccessToken(
                user.getEmail(), user.getRolesAsString()))
        .refreshToken(
            jwtEndpointAccessTokenGenerator.createRefreshToken(
                user.getEmail(), user.getRolesAsString()))
        .build();
  }
}
