package com.ing.stockexchangeservice.dto.security;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
}
