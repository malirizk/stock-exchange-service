package com.ing.stockexchangeservice.common.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

@Component
public class JwtEndpointAccessTokenGenerator {

  private JwtSecretKey secretKey;

  @Value("${jwt.access.token.expirationMs}")
  private long ACCESS_TOKEN_VALID_TIME;

  @Value("${jwt.refresh.token.expirationMs}")
  private long REFRESH_TOKEN_VALID_TIME;

  public JwtEndpointAccessTokenGenerator(JwtSecretKey secretKey) {
    this.secretKey = secretKey;
  }

  public String createAccessToken(String email, Set<String> roles) {
    return createToken(email, roles, ACCESS_TOKEN_VALID_TIME);
  }

  public String createRefreshToken(String email, Set<String> roles) {
    return createToken(email, roles, REFRESH_TOKEN_VALID_TIME);
  }

  private String createToken(String email, Set<String> roles, long validTime) {
    Claims claims = Jwts.claims().setSubject(email);
    claims.put("roles", roles);
    Date now = new Date();
    return Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + validTime))
        .signWith(SignatureAlgorithm.HS256, secretKey.getSecretKeyAsBytes())
        .compact();
  }
}
