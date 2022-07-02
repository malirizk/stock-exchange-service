package com.ing.stockexchangeservice.controller;

import com.ing.stockexchangeservice.dto.StockDto;
import com.ing.stockexchangeservice.service.StockService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/api/stock")
public class StockController {

  private final StockService stockService;

  public StockController(StockService stockService) {
    this.stockService = stockService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public StockDto addStock(@Valid @RequestBody StockDto stockDTO) {
    return stockService.save(stockDTO);
  }

  @PutMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public StockDto updateStockPrice(@PathVariable Long id, @Valid @RequestBody StockDto stockDto) {
    return stockService.updateStockPrice(id, stockDto);
  }

  @DeleteMapping(value = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStock(@PathVariable Long id) {
    stockService.deleteStock(id);
  }
}
