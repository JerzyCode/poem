package com.example.poem.core.models.user;

import com.example.poem.core.base.exceptions.UsernameTakenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final PasswordEncoder passwordEncoder;

  public static final String USER_NAME_TAKEN = "Username Taken";

  private final UserRepository repository;

  public void register(User user) throws UsernameTakenException {
    boolean existUser = repository.findUserByUsername(user.getUsername()).isPresent();
    if (existUser) {
      throw new UsernameTakenException(USER_NAME_TAKEN);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(UserRole.USER);
    repository.save(user);
  }

  public User findByUsername(String username) {
    return repository.findUserByUsername(username).orElseThrow();
  }

}
