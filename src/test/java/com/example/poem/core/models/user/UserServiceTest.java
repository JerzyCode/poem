package com.example.poem.core.models.user;

import com.example.poem.core.base.authorization.JwtService;
import com.example.poem.core.base.authorization.dto.AuthRequest;
import com.example.poem.core.base.authorization.dto.AuthenticationResponse;
import com.example.poem.core.base.authorization.dto.AuthenticationResponseAssert;
import com.example.poem.core.base.exceptions.UsernameTakenException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.poem.core.base.Constants.TEST_PASSWORD;
import static com.example.poem.core.base.Constants.TEST_USERNAME;
import static com.example.poem.core.models.user.UserService.WRONG_CREDENTIALS_MSG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

@SpringBootTest
class UserServiceTest {
  @Autowired
  private UserRepository repository;
  @Autowired
  private AuthenticationManager authenticationManager;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtService jwtService;
  private UserService sut;

  @BeforeEach
  void setUp() {
    sut = new UserService(repository, authenticationManager, passwordEncoder, jwtService);
  }

  @AfterEach
  void clearAll() {
    repository.deleteAll();
  }

  @Test
  void should_register_new_user() throws UsernameTakenException {
    // given
    AuthRequest request = new AuthRequest(TEST_USERNAME, TEST_PASSWORD);

    //when
    AuthenticationResponse response = sut.register(request);

    //then
    String token = response.token();
    assertThat(token)
        .isNotNull()
        .isNotEmpty();
    AuthenticationResponseAssert.assertThat(response)
        .hasExpirationDate(jwtService.extractExpiration(token).getTime())
        .hasUsername(jwtService.extractUsername(token));
  }

  @Test
  void register_should_throw_username_taken_exception() {
    // given
    AuthRequest request = new AuthRequest(TEST_USERNAME, TEST_PASSWORD);
    createUser();

    //when
    var response = catchThrowable(() -> sut.register(request));

    //then
    assertThat(response)
        .isNotNull()
        .isInstanceOf(UsernameTakenException.class)
        .hasMessage(TEST_USERNAME);
  }

  @Test
  void should_login_user() {
    // given
    User user = createUser();
    AuthRequest request = new AuthRequest(user.getUsername(), TEST_PASSWORD);

    // when
    var response = sut.login(request);

    //then
    String token = response.token();
    assertThat(token)
        .isNotNull()
        .isNotEmpty();
    AuthenticationResponseAssert.assertThat(response)
        .hasUsername(user.getUsername())
        .hasExpirationDate(jwtService.extractExpiration(token).getTime());
  }

  @Test
  void login_wrong_password_should_throw_bad_credentials_exception() {
    // given
    AuthRequest request = new AuthRequest(TEST_USERNAME, TEST_PASSWORD + "1");
    createUser();

    //when
    var response = catchThrowable(() -> sut.login(request));

    //then
    assertThat(response)
        .isNotNull()
        .isInstanceOf(BadCredentialsException.class)
        .hasMessage(WRONG_CREDENTIALS_MSG);
  }

  @Test
  void login_wrong_username_should_throw_bad_credentials_exception() {
    // given
    AuthRequest request = new AuthRequest(TEST_USERNAME + "1", TEST_PASSWORD);
    createUser();

    //when
    var response = catchThrowable(() -> sut.login(request));

    //then
    assertThat(response)
        .isNotNull()
        .isInstanceOf(BadCredentialsException.class)
        .hasMessage(WRONG_CREDENTIALS_MSG);
  }

  private User createUser() {
    return repository.save(User.builder()
        .username(TEST_USERNAME)
        .password(passwordEncoder.encode(TEST_PASSWORD))
        .role(UserRole.USER)
        .build());
  }

}