package com.ing.stockexchangeservice.repository;

import com.ing.stockexchangeservice.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
  Stock findByName(String name);
}
