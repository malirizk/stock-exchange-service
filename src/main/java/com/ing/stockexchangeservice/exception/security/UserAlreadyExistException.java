package com.ing.stockexchangeservice.exception.security;

import com.ing.stockexchangeservice.exception.BusinessException;
import com.ing.stockexchangeservice.exception.ExceptionMap;

public class UserAlreadyExistException extends BusinessException {
  public UserAlreadyExistException() {
    super(ExceptionMap.USER_ALREADY_EXIST);
  }
}
