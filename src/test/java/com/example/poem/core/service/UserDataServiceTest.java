package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserData;
import com.example.poem.core.model.user.UserDataRepository;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseRepository;
import com.example.poem.core.shared.helpers.UserHelper;
import com.example.poem.core.shared.helpers.VerseHelper;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserDataServiceTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserDataRepository userDataRepository;
  @Autowired
  private VerseRepository verseRepository;
  private UserDataService sut;

  @BeforeEach
  void setUp() {
    sut = new UserDataService(userDataRepository, verseRepository);
  }

  @AfterEach
  @Transactional
  void cleanUp() {
    userDataRepository.deleteAll();
    verseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void should_create_user_data() {
    //given
    User user = userRepository.save(UserHelper.prepareTestUser());
    //when
    UserData result = sut.createUserData(user);
    //then
    Assertions.assertThat(result).isNotNull();
    UserData saved = userDataRepository.findById(result.getId()).orElseThrow();
    Assertions.assertThat(result.getId()).isEqualTo(saved.getId());
    Assertions.assertThat(result.getLikedVerses()).isNotNull().isEmpty();
  }

  @Test
  void should_return_true_verse_liked_by_user() {
    //given
    UserData userData = prepareUserData();
    Verse verse = verseRepository.save(VerseHelper.prepareVerse(userData.getUser()));
    userData.getLikedVerses().add(verse);
    userDataRepository.save(userData);
    //when
    boolean result = sut.isVerseLikedByUser(userData.getUser(), verse);
    //then
    Assertions.assertThat(result).isTrue();
  }

  @Test
  void should_return_false_verse_liked_by_user() {
    //given
    UserData userData = prepareUserData();
    Verse verse = VerseHelper.prepareVerse(userData.getUser());
    //when
    boolean result = sut.isVerseLikedByUser(userData.getUser(), verse);
    //then
    Assertions.assertThat(result).isFalse();
  }

  @Test
  void should_add_verse_to_liked() {
    //given
    UserData userData = prepareUserData();
    Verse verse = verseRepository.save(VerseHelper.prepareVerse(userData.getUser()));
    int previousLikes = verse.getLikes();
    //when
    sut.changeVerseLikedForUser(userData.getUser(), verse, false);
    //then
    UserData updatedUserData = userDataRepository.findByUser(userData.getUser()).orElseThrow();
    Assertions.assertThat(updatedUserData).isNotNull();
    Verse likedVerse = updatedUserData.getLikedVerses().get(0);
    Assertions.assertThat(likedVerse).isNotNull();
    Assertions.assertThat(likedVerse.getId()).isEqualTo(verse.getId());
    Assertions.assertThat(likedVerse.getText()).isEqualTo(verse.getText());
    Assertions.assertThat(likedVerse.getLikes()).isEqualTo(previousLikes + 1);
  }

  @Test
  void should_remove_verse_from_liked() {
    //given
    UserData userData = prepareUserData();
    Verse verse = prepareVerse(userData.getUser());
    userData.getLikedVerses().add(verse);
    userDataRepository.save(userData);
    int previousLikes = verse.getLikes();
    //when
    sut.changeVerseLikedForUser(userData.getUser(), verse, true);
    //then
    UserData updatedUserData = userDataRepository.findByUser(userData.getUser()).orElseThrow();
    Assertions.assertThat(updatedUserData).isNotNull();
    Assertions.assertThat(updatedUserData.getLikedVerses()).isEmpty();
    Assertions.assertThat(verseRepository.findById(verse.getId()).orElseThrow().getLikes()).isEqualTo(previousLikes - 1);
  }

  private UserData prepareUserData() {
    User user = userRepository.save(UserHelper.prepareTestUser());
    return userDataRepository.save(UserData.builder()
        .user(user)
        .likedVerses(new ArrayList<>())
        .build());
  }

  private Verse prepareVerse(User user) {
    Verse verse = VerseHelper.prepareVerse(user);
    verse.setLikes(1);
    return verseRepository.save(verse);
  }

}