package com.ing.stockexchangeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.stockexchangeservice.dto.StockDto;
import com.ing.stockexchangeservice.exception.StockExchangeNotFoundException;
import com.ing.stockexchangeservice.service.StockExchangeService;
import com.ing.stockexchangeservice.service.StockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Assertions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class StockExchangeControllerIT {

  public final String V1_API_STOCK_EXCHANGE = "/v1/api/stock-exchange/";
  @Autowired private MockMvc mockMvc;
  @Autowired private StockService stockService;
  @Autowired private StockExchangeService stockExchangeService;
  @Autowired private ObjectMapper objectMapper;

  @ParameterizedTest
  @CsvSource(value = {"1,Nasdaq", "2,NYSE"})
  void Should_Return_StockExchange_Details_When_StockExchange_IsExist(String id, String name)
      throws Exception {
    mockMvc
        .perform(get(V1_API_STOCK_EXCHANGE + name).contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(name))
        .andExpect(jsonPath("$.stocks.length()").value(0));
  }

  @Test
  void Should_Return_NotFound_When_StockExchange_IsNotExist() throws Exception {
    mockMvc
        .perform(get(V1_API_STOCK_EXCHANGE + "Euronext").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(
            result ->
                Assertions.assertTrue(
                    result.getResolvedException() instanceof StockExchangeNotFoundException));
  }

  @Test
  void Should_Return_Stocks_When_Add_Stocks_To_StockExchange() throws Exception {
    StockDto WMT = createStock("WMT", "Walmart", 121.7d);
    StockDto COST = createStock("COST", "Costco Wholesale", 463.31d);
    StockDto CASY = createStock("CASY", "Caseys General Stores", 190.98d);
    List<Long> stockIds = List.of(WMT.getId(), COST.getId(), CASY.getId());

    mockMvc
        .perform(
            post(V1_API_STOCK_EXCHANGE + "Nasdaq/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockIds)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("Nasdaq"))
        .andExpect(jsonPath("$.liveInMarket").value(false))
        .andExpect(jsonPath("$.stocks.length()").value(3));
  }

  @Test
  void Should_LiveInMarket_True_When_Add_5_Stocks_To_StockExchange() throws Exception {
    final int id = 1;
    final String nasdaq = "Nasdaq";
    StockDto WMT = createStock("WMT", "Walmart", 121.7d);
    StockDto COST = createStock("COST", "Costco Wholesale", 463.31d);
    StockDto CASY = createStock("CASY", "Caseys General Stores", 190.98d);
    StockDto TSCO = createStock("TSCO", "Tesco", 250.1d);
    StockDto SYY = createStock("SYY", "Sysco", 81.91d);
    List<Long> stockIds =
        List.of(WMT.getId(), COST.getId(), CASY.getId(), TSCO.getId(), SYY.getId());

    mockMvc
        .perform(
            post(V1_API_STOCK_EXCHANGE + nasdaq + "/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockIds)))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.name").value(nasdaq))
        .andExpect(jsonPath("$.liveInMarket").value(true))
        .andExpect(jsonPath("$.stocks.length()").value(stockIds.size()));

    assertEquals(
        stockIds.size(), stockExchangeService.findStockExchangeByName(nasdaq).getStocks().size());
  }

  @Test
  void Should_Return_Ok_When_Delete_Added_Stocks_To_StockExchange() throws Exception {
    final int id = 1;
    final String nasdaq = "Nasdaq";
    StockDto WMT = createStock("WMT", "Walmart", 121.7d);
    StockDto COST = createStock("COST", "Costco Wholesale", 463.31d);
    StockDto CASY = createStock("CASY", "Caseys General Stores", 190.98d);
    StockDto TSCO = createStock("TSCO", "Tesco", 250.1d);
    StockDto SYY = createStock("SYY", "Sysco", 81.91d);
    List<Long> stockIds =
        List.of(WMT.getId(), COST.getId(), CASY.getId(), TSCO.getId(), SYY.getId());
    List<Long> deletedStockIds = List.of(COST.getId(), TSCO.getId(), SYY.getId());

    stockExchangeService.addStocksToStockExchange(nasdaq, stockIds);
    mockMvc
        .perform(
            delete(V1_API_STOCK_EXCHANGE + "Nasdaq/stocks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(deletedStockIds)))
        .andDo(print())
        .andExpect(status().isOk());
    assertEquals(
        stockIds.size() - deletedStockIds.size(),
        stockExchangeService.findStockExchangeByName(nasdaq).getStocks().size());
  }

  private StockDto createStock(String name, String description, Double price) {
    StockDto stockDto =
        StockDto.builder().name(name).description(description).currentPrice(price).build();
    return stockService.save(stockDto);
  }
}
