package com.ing.stockexchangeservice.service;

import com.ing.stockexchangeservice.dto.StockDto;
import com.ing.stockexchangeservice.exception.StockAlreadyExistException;
import com.ing.stockexchangeservice.exception.StockNotFoundException;
import com.ing.stockexchangeservice.mapper.StockMapper;
import com.ing.stockexchangeservice.model.Stock;
import com.ing.stockexchangeservice.repository.StockRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StockService {

  private final StockRepository stockRepository;
  private final StockMapper stockMapper;

  public StockDto save(StockDto stockDto) {

    if (StringUtils.isBlank(stockDto.getName())) {
      throw new ValidationException("Stock name is null or empty");
    }

    if (stockRepository.findByName(stockDto.getName()) != null) {
      throw new StockAlreadyExistException();
    }

    Stock stock = stockMapper.toStock(stockDto);
    stock.setLastUpdate(LocalDateTime.now());
    stockRepository.save(stock);
    return stockMapper.toStockDto(stock);
  }

  public StockDto updateStockPrice(Long id, StockDto stockDto) {
    Stock stock = findStockById(id);
    stock.setCurrentPrice(stockDto.getCurrentPrice());
    stock.setLastUpdate(LocalDateTime.now());
    stock = stockRepository.save(stock);
    return stockMapper.toStockDto(stock);
  }

  public void deleteStock(Long id) {
    Stock stock = findStockById(id);
    stockRepository.delete(stock);
  }

  public Stock findStockById(Long id) {
    return Optional.ofNullable(id)
        .map(stockRepository::findById)
        .get()
        .orElseThrow(StockNotFoundException::new);
  }
}
