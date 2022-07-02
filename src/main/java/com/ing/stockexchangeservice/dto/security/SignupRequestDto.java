package com.ing.stockexchangeservice.dto.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequestDto {

  @Pattern(regexp = ".+[@].+[\\.].+", message = "{validation.email.wrong.pattern}")
  private String email;

  @Size(min = 6, message = "{validation.password.size.too_short}")
  @NotBlank(message = "Password is empty or null")
  private String password;
}
