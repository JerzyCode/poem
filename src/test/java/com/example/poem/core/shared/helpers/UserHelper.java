package com.example.poem.core.shared.helpers;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRole;

public class UserHelper {

  private static final String NAME = "name";
  private static final String SURNAME = "surname";
  private static final String PASSWORD = "password123@";
  private static final String USERNAME = "name@mail.com";

  public static User prepareTestUser() {
    return User.builder()
        .role(UserRole.USER)
        .name(NAME)
        .surname(SURNAME)
        .password(PASSWORD)
        .username(USERNAME)
        .build();
  }
}
