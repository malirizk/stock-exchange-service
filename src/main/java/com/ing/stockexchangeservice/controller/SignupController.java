package com.ing.stockexchangeservice.controller;

import com.ing.stockexchangeservice.dto.security.SignupRequestDto;
import com.ing.stockexchangeservice.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SignupController {

  private final UserService userService;

  public SignupController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.OK)
  public void signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
    userService.addUser(signupRequestDto);
  }
}
