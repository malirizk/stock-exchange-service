package com.ing.stockexchangeservice.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ExceptionMap {
  STOCK_NOT_FOUND("BUS001", "exception.stock.is.not.found", HttpStatus.NOT_FOUND),
  STOCK_EXCHANGE_NOT_FOUND("BUS002", "exception.stock.exchange.is.not.found", HttpStatus.NOT_FOUND),
  STOCK_IS_ALREADY_EXIST("BUS003", "exception.stock.is.already.exist", HttpStatus.BAD_REQUEST),
  USER_ALREADY_EXIST("SEC001", "exception.signup.username.exist", HttpStatus.BAD_REQUEST),
  INVALID_LOGIN("SEC002", "exception.login.invalid.login", HttpStatus.UNAUTHORIZED);

  private String code;
  private String message;
  private HttpStatus httpStatus;
}
