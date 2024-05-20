package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserData;
import com.example.poem.core.model.user.UserDataRepository;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.shared.helpers.UserHelper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserDataServiceTest {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserDataRepository userDataRepository;
  private UserDataService sut;

  @BeforeEach
  void setUp() {
    sut = new UserDataService(userDataRepository);
  }

  @AfterEach
  void cleanUp() {
    userDataRepository.deleteAll();
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

}