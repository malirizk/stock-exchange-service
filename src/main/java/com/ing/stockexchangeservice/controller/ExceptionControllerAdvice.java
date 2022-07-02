package com.ing.stockexchangeservice.controller;

import com.ing.stockexchangeservice.dto.exception.ErrorResponseDto;
import com.ing.stockexchangeservice.exception.BusinessException;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
class ExceptionControllerAdvice {
  private final MessageSource messageSource;

  public ExceptionControllerAdvice(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  ResponseEntity<ErrorResponseDto> handleBusinessException(BusinessException ex) {
    String message =
        messageSource.getMessage(
            ex.getErrorResponseDto().getMessage(),
            ex.getErrorResponseDto().getArgs(),
            Locale.getDefault());
    ex.getErrorResponseDto().setMessage(message);
    return ResponseEntity.status(ex.getHttpStatus()).body(ex.getErrorResponseDto());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  List<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    return ex.getBindingResult().getFieldErrors().stream()
        // .map(e -> e.getField() + " - " + e.getDefaultMessage())
        .map(e -> e.getDefaultMessage())
        .collect(Collectors.toList());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  String handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    String name = ex.getName();
    String type = ex.getRequiredType().getSimpleName();
    Object value = ex.getValue();
    return String.format("'%s' should be a valid '%s' and '%s' isn't", name, type, value);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  List<String> handleConstraintValidationException(ConstraintViolationException ex) {
    return ex.getConstraintViolations().stream()
        // .map(e -> e.getPropertyPath().toString() + " - " + e.getMessage())
        .map(e -> e.getMessage())
        .collect(Collectors.toList());
  }

  @ExceptionHandler(RuntimeException.class)
  ResponseEntity handleGeneralException(RuntimeException ex) throws RuntimeException {
    if (AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class) != null) {
      throw ex;
    } else {
      if (ex instanceof NullPointerException) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
  }
}
