package com.example.poem.core.verse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/verse")
public class VerseController {

  private final VerseRepository repository;
  private final VerseService service;

  @GetMapping
  public String getAllVerses(Model model) {
    model.addAttribute("verses", repository.findAll());
    return "verse/allVerses";
  }

  @GetMapping("{id}")
  public String getVerse(Model model, @PathVariable Long id) {
    Verse verse = service.getVerse(id);
    model.addAttribute("verse", verse);
    return "verse/verseDetails";
  }

  @PostMapping
  public ResponseEntity<Verse> addVerse(@RequestBody VerseDTO verseDTO) {
    Verse verse = service.addVerse(verseDTO);
    return ResponseEntity.ok(verse);
  }

}
