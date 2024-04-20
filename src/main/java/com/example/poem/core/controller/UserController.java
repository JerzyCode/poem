package com.example.poem.core.controller;

import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRole;
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

  private final UserService service;

  @GetMapping("/login")
  public String login() {
    return "login";
  }

  @GetMapping("/signup")
  public String getSignUpPage(Model model) {
    System.out.println("getSignUpPage");
    model.addAttribute("user", new User());
    return "signup";
  }

  @PostMapping("/signup")
  public String signUpUser(@ModelAttribute User user) {
    System.out.println("signUpUser");
    user.setRole(UserRole.USER);
    try {
      service.register(user);
    }
    catch (UsernameTakenException e) {
      // TODO tutaj zrobic zeby komunikat wyświetlić że jest już tkai user
      return "redirect:/signup?fail";
    }
    return "redirect:/login?success";
  }
}
