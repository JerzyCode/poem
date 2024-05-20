package com.example.poem.core.controller;

import com.example.poem.core.service.VerseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ActionsController {

  private final VerseService service;

  @PutMapping("/rest/api/like")
  public ResponseEntity<Void> likeVerse(@RequestParam Long verseId, @RequestParam Long userId) {
    System.out.printf("Like verse with id=%d, and user=%d", verseId, userId);
    return ResponseEntity.ok().build();
  }
}
