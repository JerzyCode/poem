package com.example.poem.core.controller;

import com.example.poem.core.base.exceptions.WrongUserException;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.service.VerseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class VerseController {

  private final VerseService verseService;

  @GetMapping("/home")
  public String getRandomVersesHome(Model model) {
    model.addAttribute("verses", verseService.findRandomVerses());
    return "home";
  }

  @GetMapping("/verses/{userId}")
  public String getVersesByUserId(Model model, @PathVariable Long userId) {
    model.addAttribute("userId", userId);
    model.addAttribute("verses", verseService.findAllByUserId(userId));
    return "verse/allVerses";
  }

  @GetMapping("/verse/{id}")
  public String getVerse(Model model, @PathVariable Long id) {
    Verse verse = verseService.getVerse(id);
    verseService.addView(verse);
    model.addAttribute("verse", verse);
    model.addAttribute("isLikedByUser", verseService.isLikedByUser(id));
    return "verse/verseDetails";
  }

  @PostMapping("/rest/api/verse")
  public String addVerse(@ModelAttribute VerseDTO verseDTO, @RequestParam Long userId) {
    verseService.addVerse(verseDTO, userId);
    return String.format("redirect:/verses/%d", userId);
  }

  @PostMapping("/rest/api/verse/edit")
  public String editVerse(@ModelAttribute VerseDTO verseDTO, @RequestParam Long verseId, @RequestParam Long userId) {
    String result = String.format("redirect:/verse/%d", verseId);
    try {
      verseService.editVerse(verseDTO, verseId, userId);
    }
    catch (WrongUserException e) {
      return result + "?fail";
    }
    return result;
  }

  @DeleteMapping("/rest/api/verse")
  public ResponseEntity<Void> deleteVerse(@RequestParam Long verseId) {
    verseService.deleteVerse(verseId);
    return ResponseEntity.ok().build();
  }
}
