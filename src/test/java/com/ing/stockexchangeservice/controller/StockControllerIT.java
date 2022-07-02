package com.ing.stockexchangeservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ing.stockexchangeservice.dto.StockDto;
import com.ing.stockexchangeservice.exception.StockAlreadyExistException;
import com.ing.stockexchangeservice.exception.StockNotFoundException;
import com.ing.stockexchangeservice.service.StockExchangeService;
import com.ing.stockexchangeservice.service.StockService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WithMockUser
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class StockControllerIT {

  private final String V1_API_STOCK = "/v1/api/stock/";
  @Autowired private MockMvc mockMvc;
  @Autowired private StockService stockService;
  @Autowired private StockExchangeService stockExchangeService;
  @Autowired private ObjectMapper objectMapper;

  @Test
  void Should_Return_Stock_Details_When_Create_Stock() throws Exception {
    StockDto stockDto =
        StockDto.builder().name("Meta").description("Meta Stock").currentPrice(100.0d).build();

    mockMvc
        .perform(
            post(V1_API_STOCK)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("Meta"));
  }

  @Test
  void Should_Return_BadRequest_When_Create_Stock_With_Exist_Name() throws Exception {
    StockDto stockDto =
        StockDto.builder().name("Meta").description("Meta Stock").currentPrice(100.0d).build();
    stockService.save(stockDto);

    mockMvc
        .perform(
            post(V1_API_STOCK)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stockDto)))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertTrue(result.getResolvedException() instanceof StockAlreadyExistException));
  }

  @Test
  void Should_Success_When_Update_Price_Of_Exist_Stock() throws Exception {
    StockDto stockDto =
        StockDto.builder().name("Meta").description("Meta Stock").currentPrice(100.0d).build();
    stockDto = stockService.save(stockDto);

    StockDto updatedStock = StockDto.builder().currentPrice(150.0d).build();

    mockMvc
        .perform(
            put(V1_API_STOCK + stockDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStock)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.currentPrice").value(150.0d));
  }

  @Test
  void Should_Return_BadRequest_When_Update_Price_And_Value_Is_Null() throws Exception {
    StockDto stockDto =
        StockDto.builder().name("Meta").description("Meta Stock").currentPrice(100.0d).build();
    stockDto = stockService.save(stockDto);

    StockDto updatedStock = StockDto.builder().build();

    mockMvc
        .perform(
            put(V1_API_STOCK + stockDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStock)))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof MethodArgumentNotValidException));
  }

  @Test
  void Should_Return_BadRequest_When_Update_Stock_ID_Is_Null() throws Exception {

    StockDto updatedStock = StockDto.builder().currentPrice(150.0d).build();

    mockMvc
        .perform(
            put(V1_API_STOCK + "null")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStock)))
        .andExpect(status().isBadRequest())
        .andExpect(
            result ->
                assertTrue(
                    result.getResolvedException() instanceof MethodArgumentTypeMismatchException));
  }

  @Test
  void Should_Throw_Exception_When_Update_Price_Of_Not_Exist_Stock() throws Exception {
    StockDto updatedStock = StockDto.builder().currentPrice(150.0d).build();
    mockMvc
        .perform(
            put(V1_API_STOCK + "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStock)))
        .andExpect(status().isNotFound())
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof StockNotFoundException));
  }

  @Test
  void Should_Success_When_Delete_Exist_Stock() throws Exception {
    StockDto stockDto =
        StockDto.builder().name("Meta").description("Meta Stock").currentPrice(100.0d).build();
    stockDto = stockService.save(stockDto);

    mockMvc
        .perform(delete(V1_API_STOCK + stockDto.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void Should_Delete_Stock_When_Stock_Attached_To_StockExchange() throws Exception {
    StockDto stockDto =
        StockDto.builder().name("Meta").description("Meta Stock").currentPrice(100.0d).build();
    stockDto = stockService.save(stockDto);
    String nasdaq = "Nasdaq";
    stockExchangeService.addStocksToStockExchange(nasdaq, List.of(stockDto.getId()));

    mockMvc
        .perform(delete(V1_API_STOCK + stockDto.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    assertEquals(0, stockExchangeService.findStockExchangeByName(nasdaq).getStocks().size());
  }

  @Test
  void Should_Throw_Exception_When_Delete_Not_Exist_Stock() throws Exception {

    mockMvc
        .perform(delete(V1_API_STOCK + "1").contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(
            result -> assertTrue(result.getResolvedException() instanceof StockNotFoundException));
  }
}
