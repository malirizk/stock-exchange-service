package com.ing.stockexchangeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {

  private Long id;
  private String name;
  private String description;

  @NotNull(message = "{validation.stock.price.null}")
  private Double currentPrice;

  private LocalDateTime lastUpdate;
}
