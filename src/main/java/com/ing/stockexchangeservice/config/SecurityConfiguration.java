package com.ing.stockexchangeservice.config;

import com.ing.stockexchangeservice.common.token.JwtSecretKey;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration {

  private final UserDetailsService userDetailsService;
  private final JwtSecretKey jwtSecretKey;
  private final PasswordEncoder passwordEncoder;

  public SecurityConfiguration(
      UserDetailsService userDetailsService,
      JwtSecretKey jwtSecretKey,
      PasswordEncoder passwordEncoder) {
    this.userDetailsService = userDetailsService;
    this.jwtSecretKey = jwtSecretKey;
    this.passwordEncoder = passwordEncoder;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.headers()
        .disable()
        .csrf()
        .disable()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/login", "/signup")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .addFilterBefore(
            new JwtRequestFilter(userDetailsService, jwtSecretKey),
            UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) ->
        web.ignoring().antMatchers("/swagger-ui/**", "/**/api-docs/**", "/h2-console/**");
  }
}
