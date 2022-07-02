package com.ing.stockexchangeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.stockexchangeservice.dto.security.SignupRequestDto;
import com.ing.stockexchangeservice.model.security.UserEntity;
import com.ing.stockexchangeservice.model.security.UserRole;
import com.ing.stockexchangeservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class SignupControllerIT {

  public static final String API_SIGNUP = "/signup";
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private UserRepository userRepository;
  @Autowired private PasswordEncoder passwordEncoder;

  @Test
  void Should_Return_Ok_When_Signup() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("user@email.com").password("password").build();

    mockMvc
        .perform(
            post(API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andExpect(status().isOk());

    UserEntity user = userRepository.findByEmail(signupRequestDto.getEmail()).get();
    assertThat(user.getId(), notNullValue());
    assertThat(
        passwordEncoder.matches(signupRequestDto.getPassword(), user.getPassword()), equalTo(true));
    assertThat(user.getRoles().toArray()[0], equalTo(UserRole.ROLE_USER));
  }

  @Test
  void Should_Return_BadRequest_When_Signup_With_Same_Email() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("user@email.com").password("password").build();
    mockMvc
        .perform(
            post(API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andExpect(status().isOk());

    SignupRequestDto signupReqDto =
        SignupRequestDto.builder()
            .email(signupRequestDto.getEmail())
            .password("different-password")
            .build();
    mockMvc
        .perform(
            post(API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupReqDto)))
        .andExpect(status().isInternalServerError())
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof DataIntegrityViolationException));
  }

  @Test
  void Should_Return_BadRequest_When_Signup_With_Wrong_Email_Format() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("WRONG-email.format").password("password").build();
    mockMvc
        .perform(
            post(API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void Should_Return_BadRequest_When_Signup_With_Short_Password() throws Exception {
    SignupRequestDto signupRequestDto =
        SignupRequestDto.builder().email("user@email.com").password("123").build();
    mockMvc
        .perform(
            post(API_SIGNUP)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequestDto)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof MethodArgumentNotValidException))
        .andExpect(
            result ->
                assertEquals(
                    "Minimum password length is 6",
                    ((MethodArgumentNotValidException) result.getResolvedException())
                        .getBindingResult()
                        .getFieldErrors()
                        .get(0)
                        .getDefaultMessage()));
  }
}
