package com.ing.stockexchangeservice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class StockExchange {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;
  private String description;
  @Transient private Boolean liveInMarket;

  @ManyToMany
  @JoinTable(
      name = "stock_exchange_mapping",
      joinColumns =
          @JoinColumn(name = "stock_exchange_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
          @JoinColumn(name = "stock_id", referencedColumnName = "id", nullable = false))
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private Set<Stock> stocks = new HashSet<>();

  public Boolean getLiveInMarket() {
    return this.getStocks().size() >= 5;
  }
}
