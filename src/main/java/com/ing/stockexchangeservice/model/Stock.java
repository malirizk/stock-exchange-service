package com.ing.stockexchangeservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private String description;
  private Double currentPrice;
  private LocalDateTime lastUpdate;

  @ManyToMany(mappedBy = "stocks")
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<StockExchange> stockExchanges = new HashSet<>();
}
