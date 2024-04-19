package com.example.poem.core.base.authorization;

import com.example.poem.core.base.authorization.dto.AuthRequest;
import com.example.poem.core.base.authorization.dto.AuthenticationResponse;
import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.models.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.example.poem.core.base.Constants.TEST_PASSWORD;
import static com.example.poem.core.base.Constants.TEST_USERNAME;
import static com.example.poem.core.base.exceptions.handler.RestResponseExceptionHandler.USERNAME_TAKEN_MSG;
import static com.example.poem.core.models.user.UserService.WRONG_CREDENTIALS_MSG;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("dev")
class AuthControllerTest {
  @MockBean
  private UserService service;

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private MockMvc mvc;

  public static final String REGISTER_URL = "/authentication/register";
  public static final String LOGIN_URL = "/authentication/login";
  public static final Long EXPIRATION_DATE = 213L;

  @Test
  void register_should_return_created_user_token() throws Exception {
    //given
    AuthRequest request = createRequest();
    AuthenticationResponse response = createAuthResponse(request.username());
    //when
    when(service.register(request)).thenReturn(response);
    //then
    mvc.perform(post(REGISTER_URL)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(response.token()));
  }

  @Test
  void register_username_taken__should_return_bad_request() throws Exception {
    //given
    AuthRequest request = createRequest();
    when(service.register(request)).thenThrow(new UsernameTakenException(request.username()));
    //when
    //then
    MvcResult result = mvc.perform(post(REGISTER_URL)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    Assertions.assertTrue(result.getResponse().getContentAsString().contains(String.format(USERNAME_TAKEN_MSG, request.username())));
  }

  @Test
  void login_should_return_logged_user_token() throws Exception {
    //given
    AuthRequest request = createRequest();
    AuthenticationResponse response = createAuthResponse(request.username());
    when(service.login(request)).thenReturn(response);

    //when
    //then
    mvc.perform(post(LOGIN_URL)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value(response.token()))
        .andReturn();
  }

  @Test
  void login_wrong_credentials_should_throw() throws Exception {
    //given
    AuthRequest request = createRequest();
    when(service.login(request)).thenThrow(new BadCredentialsException(WRONG_CREDENTIALS_MSG));
    //when
    //then
    MvcResult result = mvc.perform(post(LOGIN_URL)
            .content(mapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    Assertions.assertTrue(result.getResponse().getContentAsString().contains(WRONG_CREDENTIALS_MSG));
  }

  private AuthenticationResponse createAuthResponse(String username) {
    return new AuthenticationResponse("token", username, EXPIRATION_DATE);
  }

  private AuthRequest createRequest() {
    return new AuthRequest(TEST_USERNAME, TEST_PASSWORD);
  }
}