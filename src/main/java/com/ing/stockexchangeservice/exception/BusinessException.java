package com.ing.stockexchangeservice.exception;

import com.ing.stockexchangeservice.dto.exception.ErrorResponseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = false)
public abstract class BusinessException extends RuntimeException {
  private HttpStatus httpStatus;
  private ErrorResponseDto errorResponseDto;

  public BusinessException() {
    this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    this.errorResponseDto = new ErrorResponseDto();
  }

  public BusinessException(ExceptionMap map) {
    this.httpStatus = map.getHttpStatus();
    this.errorResponseDto =
        ErrorResponseDto.builder().code(map.getCode()).message(map.getMessage()).build();
  }

  public BusinessException(ExceptionMap map, Object[] args) {
    this.httpStatus = map.getHttpStatus();
    this.errorResponseDto =
        ErrorResponseDto.builder().code(map.getCode()).message(map.getMessage()).args(args).build();
  }
}
