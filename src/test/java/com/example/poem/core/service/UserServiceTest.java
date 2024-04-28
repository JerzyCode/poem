package com.example.poem.core.service;

import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.shared.helpers.UserHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private UserRepository userRepository;
  private UserService sut;
  private static final String TEST_USERNAME = "test@mail.com";

  @BeforeEach
  void setUp() {
    sut = new UserService(passwordEncoder, userRepository);
  }

  @AfterEach
  void cleanUp() {
    userRepository.deleteAll();
  }

  @Test
  void test_register_should() throws UsernameTakenException {
    //given
    User user = UserHelper.prepareTestUser();

    //when
    User registeredUser = sut.register(user);

    //then
    User savedUser = userRepository.findById(registeredUser.getId()).orElseThrow();
    Assertions.assertEquals(savedUser.getId(), registeredUser.getId());
    Assertions.assertEquals(savedUser.getUsername(), user.getUsername());
    Assertions.assertEquals(savedUser.getName(), user.getName());
    Assertions.assertEquals(savedUser.getSurname(), user.getSurname());
    Assertions.assertEquals(savedUser.getRole(), user.getRole());
  }

  @Test
  void test_register_should_throw_username_taken_exception() {
    //given
    User user1 = UserHelper.prepareTestUser();
    User user2 = UserHelper.prepareTestUser();
    userRepository.save(user1);

    //when & then
    assertThrows(UsernameTakenException.class, () -> sut.register(user2));
  }

  @Test
  void test_find_by_username_should_return() {
    //given
    User user = UserHelper.prepareTestUser();
    userRepository.save(user);
    //when
    User foundUser = sut.findByUsername(user.getUsername());
    //then
    Assertions.assertEquals(foundUser.getId(), user.getId());
    Assertions.assertEquals(foundUser.getUsername(), user.getUsername());
    Assertions.assertEquals(foundUser.getName(), user.getName());
    Assertions.assertEquals(foundUser.getSurname(), user.getSurname());
    Assertions.assertEquals(foundUser.getRole(), user.getRole());
  }

  @Test
  void test_find_by_username_should_throw_no_such_element_exception() {
    //given
    //when & then
    assertThrows(NoSuchElementException.class, () -> sut.findByUsername(TEST_USERNAME));
  }

}