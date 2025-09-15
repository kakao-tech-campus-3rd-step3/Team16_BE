package com.kakaotechcampus.team16be.groundrule.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GroundRuleExceptionHandler {

  @ExceptionHandler(GroundRuleException.class)
  public ResponseEntity<String> handleGroundRuleNotFound(GroundRuleException ex){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }
}
