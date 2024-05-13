package com.example.poem.core.base.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongUserException extends Exception {
  public WrongUserException(String message) {
    super(message);
  }
}
