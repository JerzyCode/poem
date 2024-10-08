package com.example.poem.core.service;

import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.model.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final PasswordEncoder passwordEncoder;
  private final UserRepository repository;
  public static final String USER_NAME_TAKEN = "Username Taken";

  public User register(User user) throws UsernameTakenException {
    boolean existUser = repository.findUserByUsername(user.getUsername()).isPresent();
    if (existUser) {
      throw new UsernameTakenException(USER_NAME_TAKEN);
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRole(UserRole.USER);
    return repository.save(user);
  }

  public User findByUsername(String username) {
    return repository.findUserByUsername(username).orElseThrow();
  }

  public User findById(Long id) {
    return repository.findById(id).orElseThrow();
  }

}
