package com.example.poem.core.controller;

import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.model.verse.VerseRepository;
import com.example.poem.core.service.VerseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class VerseController {

  private final VerseRepository repository;
  private final VerseService service;

  @GetMapping("/verse")
  public String getAllVerses(Model model) {
    model.addAttribute("verses", repository.findAll());
    return "verse/allVerses";
  }

  @GetMapping("/verse{id}")
  public String getVerse(Model model, @PathVariable Long id) {
    Verse verse = service.getVerse(id);
    model.addAttribute("verse", verse);
    return "verse/verseDetails";
  }

  @PostMapping("/rest/api/verse")
  public String addVerse(@ModelAttribute VerseDTO verseDTO) {
    service.addVerse(verseDTO);
    return "redirect:/verse";
  }

}
