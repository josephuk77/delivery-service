package com.sparta.delivery.aaglobal;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalException extends RuntimeException {

  private final HttpStatus status;
  private final String message;

  public GlobalException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }
}
