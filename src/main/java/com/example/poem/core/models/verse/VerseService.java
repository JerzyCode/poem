package com.example.poem.core.models.verse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerseService {
  private final VerseRepository repository;

  public Verse getVerse(Long id) {
    return repository.findById(id).orElseThrow();
  }

  public Verse addVerse(VerseDTO verseDTO) {
    Verse verse = Verse.builder()
        .text(verseDTO.getText())
        .shortDescription(verseDTO.getShortDescription())
        .imageUrl(verseDTO.getImageUrl())
        .title(verseDTO.getTitle())
        .build();
    return repository.save(verse);
  }
}
