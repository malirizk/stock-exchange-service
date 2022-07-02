package com.ing.stockexchangeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockExchangeDto {

  private Long id;
  private String name;
  private String description;
  private Boolean liveInMarket;
  private List<StockDto> stocks;
}
