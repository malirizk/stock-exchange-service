package com.ing.stockexchangeservice.common.token;

import java.nio.charset.StandardCharsets;

public class JwtSecretKey {

  private final byte[] secretKey;

  public JwtSecretKey(String secretKeyPhrase) {
    secretKey = secretKeyPhrase.getBytes(StandardCharsets.UTF_8);
  }

  public byte[] getSecretKeyAsBytes() {
    return secretKey;
  }
}
