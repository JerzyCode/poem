package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserData;
import com.example.poem.core.model.user.UserDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDataService {
  private final UserDataRepository userDataRepository;

  public UserData createUserData(User user) {
    UserData userData = UserData.builder()
        .likedVerses(new ArrayList<>())
        .user(user)
        .build();
    return userDataRepository.save(userData);
  }
}
