package com.example.poem.core.base;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
public class HomeController {
  @GetMapping("/home")
  public String home() {
    return "home";
  }

}
