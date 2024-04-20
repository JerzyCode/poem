package com.example.poem.core.controller;

import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.model.verse.VerseRepository;
import com.example.poem.core.service.VerseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<Verse> addVerse(@RequestBody VerseDTO verseDTO) {
    Verse verse = service.addVerse(verseDTO);
    return ResponseEntity.ok(verse);
  }

}
