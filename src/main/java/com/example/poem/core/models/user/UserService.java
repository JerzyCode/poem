package com.example.poem.core.models.user;

import com.example.poem.core.base.authorization.JwtService;
import com.example.poem.core.base.authorization.dto.AuthRequest;
import com.example.poem.core.base.authorization.dto.AuthenticationResponse;
import com.example.poem.core.base.exceptions.UsernameTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  public static final String WRONG_CREDENTIALS_MSG = "Bad credentials";

  private final UserRepository repository;
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;

  public AuthenticationResponse register(AuthRequest authRequest) throws UsernameTakenException {
    String username = authRequest.username();
    Optional<User> existingUser = repository.findUserByUsername(username);
    if (existingUser.isPresent()) {
      throw new UsernameTakenException(username);
    }
    String hashedPassword = passwordEncoder.encode(authRequest.password());
    User newUser = User.builder()
        .username(username)
        .password(hashedPassword)
        .role(UserRole.USER)
        .build();
    var user = repository.save(newUser);
    String token = jwtService.buildToken(user);
    return buildAuthenticationResponse(token);
  }

  public AuthenticationResponse login(AuthRequest authRequest) throws BadCredentialsException {
    var user = repository.findUserByUsername(authRequest.username()).orElseThrow(() -> new BadCredentialsException(WRONG_CREDENTIALS_MSG));
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password()));
    var jwtToken = jwtService.buildToken(user);
    return buildAuthenticationResponse(jwtToken);
  }

  private AuthenticationResponse buildAuthenticationResponse(String jwtToken) {
    String username = jwtService.extractUsername(jwtToken);
    Long expirationDate = jwtService.extractExpiration(jwtToken).getTime();
    return new AuthenticationResponse(jwtToken, username, expirationDate);
  }

}
