package com.example.poem.core.verse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/verse")
public class VerseController {

  private final VerseRepository repository;

  @GetMapping
  public String getAllVerses(Model model) {
    model.addAttribute("verses", repository.findAll());
    return "verse/allVerses";
  }

  @GetMapping("{id}")
  public String getVerse(Model model, @PathVariable Long id) {
    Verse verse = repository.findById(id).orElseThrow();
    model.addAttribute("verse", verse);
    return "verse/verseDetails";
  }
}
