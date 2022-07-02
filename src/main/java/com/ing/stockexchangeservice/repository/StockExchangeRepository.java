package com.ing.stockexchangeservice.repository;

import com.ing.stockexchangeservice.model.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {

  Optional<StockExchange> findByName(String name);
}
