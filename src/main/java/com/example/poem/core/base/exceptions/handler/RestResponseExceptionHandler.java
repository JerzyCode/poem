package com.example.poem.core.base.exceptions.handler;

import com.example.poem.core.base.exceptions.UsernameTakenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class RestResponseExceptionHandler {
  public static final String USERNAME_TAKEN_MSG = "Username %s is already taken";

  @ExceptionHandler(UsernameTakenException.class)
  public ResponseEntity<Object> handlerUsernameTakenException(UsernameTakenException ex) {
    String errorMark = "E" + new Date().getTime();
    String message = String.format(USERNAME_TAKEN_MSG, ex.getMessage());
    HttpStatus status = HttpStatus.BAD_REQUEST;

    log.error("handlerUsernameTakenException({}): {}, msg={}", errorMark, ex.getClass(), ex.getMessage());
    return createResponse(errorMark, message, status);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
    String errorMark = "E" + new Date().getTime();
    String message = ex.getMessage();
    HttpStatus status = HttpStatus.BAD_REQUEST;

    log.error("handleBadCredentialsException({}): {}, msg={}", errorMark, ex.getClass(), ex.getMessage());
    return createResponse(errorMark, message, status);
  }

  private static ResponseEntity<Object> createResponse(String errorMark, String message, HttpStatus status) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("errorMark", errorMark);
    body.put("message", message);

    return new ResponseEntity<>(body, status);
  }
}
