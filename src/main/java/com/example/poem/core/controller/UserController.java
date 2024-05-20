package com.example.poem.core.controller;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRole;
import com.example.poem.core.service.UserDataService;
import com.example.poem.core.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class UserController {

  private final UserService userService;
  private final UserDataService userDataService;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/signup")
  public String getSignUpPage(Model model) {
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping("/signup")
  public String signUpUser(@ModelAttribute User user) {
    user.setRole(UserRole.USER);
    try {
      User createdUser = userService.register(user);
      userDataService.createUserData(createdUser);
    }
    catch (Exception e) {
      return "redirect:/signup?fail";
    }
    return "redirect:/login?success";
  }
}
