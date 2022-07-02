package com.ing.stockexchangeservice.config;

import com.ing.stockexchangeservice.common.token.JwtSecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtSecretKeyConfiguration {

  @Value("${jwt.secret}")
  private String secretKey;

  @Bean
  public JwtSecretKey secretKey() {
    return new JwtSecretKey(secretKey);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}
