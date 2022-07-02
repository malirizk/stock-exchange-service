package com.ing.stockexchangeservice.exception;

public class StockExchangeNotFoundException extends BusinessException {

  public StockExchangeNotFoundException() {
    super(ExceptionMap.STOCK_EXCHANGE_NOT_FOUND);
  }
}
