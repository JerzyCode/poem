package com.example.poem.core.base.authorization;

import com.example.poem.core.base.authorization.dto.AuthRequest;
import com.example.poem.core.base.authorization.dto.AuthenticationResponse;
import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/authentication")
@Slf4j
public class AuthController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody AuthRequest requestDto) throws UsernameTakenException {
    log.info(String.format("Register request with username=%s", requestDto.username()));
    return new ResponseEntity<>(userService.register(requestDto), HttpStatus.OK);
  }

  @PostMapping(value = "/login")
  public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthRequest request) {
    return new ResponseEntity<>(userService.login(request), HttpStatus.OK);

  }
}
