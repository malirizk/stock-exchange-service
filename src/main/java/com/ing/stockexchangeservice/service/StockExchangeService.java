package com.ing.stockexchangeservice.service;

import com.ing.stockexchangeservice.dto.StockExchangeDto;
import com.ing.stockexchangeservice.exception.StockExchangeNotFoundException;
import com.ing.stockexchangeservice.mapper.StockExchangeMapper;
import com.ing.stockexchangeservice.model.Stock;
import com.ing.stockexchangeservice.model.StockExchange;
import com.ing.stockexchangeservice.repository.StockExchangeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class StockExchangeService {

  private final StockExchangeRepository stockExchangeRepository;
  private final StockService stockService;
  private final StockExchangeMapper stockExchangeMapper;

  public StockExchangeDto findStockExchangeByName(String name) {
    return stockExchangeMapper.toStockExchangeDto(getStockExchangeByName(name));
  }

  private StockExchange getStockExchangeByName(String name) {
    return stockExchangeRepository
        .findByName(name)
        .orElseThrow(StockExchangeNotFoundException::new);
  }

  public StockExchangeDto addStocksToStockExchange(String name, List<Long> stockIds) {
    StockExchange stockExchange = getStockExchangeByName(name);
    Set<Stock> newStocks =
        stockIds.stream().map(stockService::findStockById).collect(Collectors.toSet());
    stockExchange.getStocks().addAll(newStocks);
    return stockExchangeMapper.toStockExchangeDto(stockExchangeRepository.save(stockExchange));
  }

  public void deleteStocksFromStockExchange(String name, Set<Long> stockIds) {
    StockExchange stockExchange = getStockExchangeByName(name);
    Set<Stock> deletedStocks =
        stockIds.stream().map(stockService::findStockById).collect(Collectors.toSet());
    stockExchange.getStocks().removeAll(deletedStocks);
    stockExchangeRepository.save(stockExchange);
  }
}
