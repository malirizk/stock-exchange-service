package com.ing.stockexchangeservice.mapper;

import com.ing.stockexchangeservice.dto.StockExchangeDto;
import com.ing.stockexchangeservice.model.StockExchange;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    uses = StockMapper.class,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StockExchangeMapper {

  StockExchangeDto toStockExchangeDto(StockExchange stockExchange);

  StockExchange toStockExchange(StockExchangeDto stockExchangeDto);
}
