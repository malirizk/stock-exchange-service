package com.ing.stockexchangeservice.exception.security;

import com.ing.stockexchangeservice.exception.BusinessException;
import com.ing.stockexchangeservice.exception.ExceptionMap;

public class InvalidLoginException extends BusinessException {
  public InvalidLoginException() {
    super(ExceptionMap.INVALID_LOGIN);
  }
}
