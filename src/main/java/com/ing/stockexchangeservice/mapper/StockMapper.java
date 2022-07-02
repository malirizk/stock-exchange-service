package com.ing.stockexchangeservice.mapper;

import com.ing.stockexchangeservice.dto.StockDto;
import com.ing.stockexchangeservice.model.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockMapper {
  StockDto toStockDto(Stock stock);

  Stock toStock(StockDto stockDto);
}
