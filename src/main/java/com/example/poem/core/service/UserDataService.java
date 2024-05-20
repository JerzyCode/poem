package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserData;
import com.example.poem.core.model.user.UserDataRepository;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDataService {
  private final UserDataRepository userDataRepository;
  private final VerseRepository verseRepository;

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

  public void changeVerseLikedForUser(User user, Verse verse, boolean isLiked) {
    if (isLiked) {
      unlikeVerse(user, verse);
    }
    else {
      likeVerse(user, verse);
    }
  }

  private void likeVerse(User user, Verse verse) {
    UserData userData = userDataRepository.findByUser(user).orElseThrow();
    userData.getLikedVerses().add(verse);
    verse.setLikes(verse.getLikes() + 1);
    verseRepository.save(verse);
    userDataRepository.save(userData);
  }

  private void unlikeVerse(User user, Verse verse) {
    UserData userData = userDataRepository.findByUser(user).orElseThrow();
    userData.getLikedVerses().remove(verse);
    verse.setLikes(verse.getLikes() - 1);
    verseRepository.save(verse);
    userDataRepository.save(userData);
  }
}
