package com.example.poem.core.controller;

import com.example.poem.PoemApplication;
import com.example.poem.core.base.config.SecurityConfig;
import com.example.poem.core.base.config.WebMvcConfig;
import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.service.UserDataService;
import com.example.poem.core.service.UserService;
import com.example.poem.core.shared.helpers.UserHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@ContextConfiguration(classes = { PoemApplication.class, SecurityConfig.class, WebMvcConfig.class })
class UserControllerTest {

  @MockBean
  private UserService userService;
  @MockBean
  private UserDataService userDataService;
  @Autowired
  private MockMvc mockMvc;

  @Test
  void should_return_login_view() throws Exception {
    //when & then
    mockMvc.perform(get("/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("login"));
  }

  @Test
  void should_return_signup_view() throws Exception {
    //when & then
    mockMvc.perform(get("/signup"))
        .andExpect(status().isOk())
        .andExpect(view().name("signup"))
        .andExpect(model().attributeExists("user"));
  }

  @Test
  void should_return_signup_success_view() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    when(userService.register(any())).thenReturn(user);
    //when & then
    mockMvc.perform(MockMvcRequestBuilders.post("/signup")
            .param("name", user.getName())
            .param("surname", user.getSurname())
            .param("username", user.getUsername())
            .param("password", user.getPassword())
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/login?success"));
    verify(userService, Mockito.times(1)).register(any());
  }

  @Test
  void should_throw_signup_fail_view() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    when(userService.register(any())).thenThrow(UsernameTakenException.class);
    //when & then
    mockMvc.perform(MockMvcRequestBuilders.post("/signup")
            .param("name", user.getName())
            .param("surname", user.getSurname())
            .param("username", user.getUsername())
            .param("password", user.getPassword())
            .with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/signup?fail"));
    verify(userService, Mockito.times(1)).register(any());
  }
}