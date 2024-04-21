package com.example.poem.core.controller;

import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.service.VerseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class VerseController {

  private final VerseService service;

  @GetMapping("/verse")
  public String getAllVerses(Model model) {
    model.addAttribute("verses", service.findAll());
    return "verse/allVerses";
  }

  @GetMapping("/verses")
  public String getVersesByUserId(Model model, @RequestParam Long userId) {
    model.addAttribute("verses", service.findAllByUserId(userId));
    return "verse/allVerses";
  }

  @GetMapping("/verse/{id}")
  public String getVerse(Model model, @PathVariable Long id) {
    Verse verse = service.getVerse(id);
    model.addAttribute("verse", verse);
    return "verse/verseDetails";
  }

  @PostMapping("/rest/api/verse")
  public String addVerse(@ModelAttribute VerseDTO verseDTO, @RequestParam Long userId) {
    service.addVerse(verseDTO, userId);
    return String.format("redirect:/verses?userId=%d", userId);
  }

}
