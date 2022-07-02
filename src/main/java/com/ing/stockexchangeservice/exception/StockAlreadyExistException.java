package com.ing.stockexchangeservice.exception;

public class StockAlreadyExistException extends BusinessException {
  public StockAlreadyExistException() {
    super(ExceptionMap.STOCK_IS_ALREADY_EXIST);
  }
}
