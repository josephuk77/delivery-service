package com.sparta.delivery.aaglobal;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(GlobalException.class)
  public ResponseEntity<String> handleExternalApiException(GlobalException e) {
    HttpStatus status = e.getStatus();
    String msg = e.getMessage();
    return ResponseEntity.status(status).body(msg);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(
      MethodArgumentNotValidException e) {

    BindingResult bindingResult = e.getBindingResult();
    Map<String, String> errors = new HashMap<>();
    bindingResult.getAllErrors()
        .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }
}
