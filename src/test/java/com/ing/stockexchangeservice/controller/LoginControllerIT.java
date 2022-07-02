package com.ing.stockexchangeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.stockexchangeservice.dto.security.LoginRequestDto;
import com.ing.stockexchangeservice.dto.security.SignupRequestDto;
import com.ing.stockexchangeservice.exception.security.InvalidLoginException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class LoginControllerIT {

  public static final String API_LOGIN = "/login";
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void Should_Return_Token_When_Login() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("user@email.com").password("password").build();
    mockMvc
        .perform(
            post(SignupControllerIT.API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andExpect(status().isOk());

    LoginRequestDto loginRequestDto =
        LoginRequestDto.builder().email("user@email.com").password("password").build();
    mockMvc
        .perform(
            post(API_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.accessToken", notNullValue()))
        .andExpect(jsonPath("$.refreshToken", notNullValue()));
  }

  @Test
  void Should_Throws_Exception_When_Login_With_Wrong_Password() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("user@email.com").password("password").build();
    mockMvc
        .perform(
            post(SignupControllerIT.API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andExpect(status().isOk());

    LoginRequestDto loginRequestDto =
        LoginRequestDto.builder().email("user@email.com").password("WRONG-password").build();
    mockMvc
        .perform(
            post(API_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
        .andExpect(status().isUnauthorized())
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof InvalidLoginException));
  }

  @Test
  void Should_Throws_Exception_When_Login_With_Wrong_Email() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("user@email.com").password("password").build();
    mockMvc
        .perform(
            post(SignupControllerIT.API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andExpect(status().isOk());

    LoginRequestDto loginRequestDto =
        LoginRequestDto.builder().email("WRONG-user@email.com").password("password").build();
    mockMvc
        .perform(
            post(API_LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequestDto)))
        .andExpect(status().isUnauthorized())
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof InvalidLoginException));
  }
}
