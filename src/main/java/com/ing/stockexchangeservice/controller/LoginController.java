package com.ing.stockexchangeservice.controller;

import com.ing.stockexchangeservice.dto.security.LoginRequestDto;
import com.ing.stockexchangeservice.dto.security.TokenResponse;
import com.ing.stockexchangeservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
  private final UserService userService;

  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public TokenResponse login(@RequestBody LoginRequestDto loginRequestDto) {
    return userService.login(loginRequestDto);
  }
}
