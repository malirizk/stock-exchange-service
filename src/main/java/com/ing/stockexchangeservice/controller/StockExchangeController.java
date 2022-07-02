package com.ing.stockexchangeservice.controller;

import com.ing.stockexchangeservice.dto.StockExchangeDto;
import com.ing.stockexchangeservice.service.StockExchangeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/v1/api/stock-exchange")
public class StockExchangeController {

  private final StockExchangeService stockExchangeService;

  public StockExchangeController(StockExchangeService stockExchangeService) {
    this.stockExchangeService = stockExchangeService;
  }

  @GetMapping("/{name}")
  @ResponseStatus(HttpStatus.OK)
  public StockExchangeDto getStockExchangeByName(@PathVariable String name) {
    return stockExchangeService.findStockExchangeByName(name);
  }

  @PostMapping("/{name}/stocks")
  @ResponseStatus(HttpStatus.OK)
  public StockExchangeDto addStocksToStockExchange(
      @PathVariable String name, @RequestBody List<Long> stockIds) {
    return stockExchangeService.addStocksToStockExchange(name, stockIds);
  }

  @DeleteMapping("/{name}/stocks")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStocksFromStockExchange(
      @PathVariable String name, @RequestBody Set<Long> stockIds) {
    stockExchangeService.deleteStocksFromStockExchange(name, stockIds);
  }
}
