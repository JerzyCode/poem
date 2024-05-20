package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserData;
import com.example.poem.core.model.user.UserDataRepository;
import com.example.poem.core.model.verse.Verse;
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

  public boolean isVerseLikedByUser(User user, Verse verse) {
    UserData userData = userDataRepository.findByUser(user).orElseThrow();
    Verse likedVerse = userData.getLikedVerses().stream()
        .filter(v -> v.getId().equals(verse.getId()))
        .findFirst()
        .orElse(null);
    return likedVerse != null;
  }
}
