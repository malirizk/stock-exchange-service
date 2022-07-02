package com.ing.stockexchangeservice.exception;

public class StockNotFoundException extends BusinessException {

  public StockNotFoundException() {
    super(ExceptionMap.STOCK_NOT_FOUND);
  }
}
