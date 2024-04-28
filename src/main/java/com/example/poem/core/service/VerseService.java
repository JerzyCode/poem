package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.model.verse.VerseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VerseService {
  private final VerseRepository verseRepository;
  private final UserRepository userRepository;

  public Verse getVerse(Long id) {
    return verseRepository.findById(id).orElseThrow();
  }

  public List<Verse> findRandomVerses() {
    List<Verse> verses = verseRepository.findAll();
    Collections.shuffle(verses);
    int numOfVerses = verses.size();
    return switch (numOfVerses) {
      case 0 -> new ArrayList<>();
      case 1 -> List.of(verses.get(0));
      case 2 -> verses.subList(0, 2);
      default -> verses.subList(0, 3);
    };
  }

  public List<Verse> findAllByUserId(Long userId) {
    User user = userRepository.findById(userId).orElseThrow();
    return verseRepository.findAllByUser(user);
  }

  public void addVerse(VerseDTO verseDTO, Long userId) {
    User user = userRepository.findById(userId).orElseThrow();
    Verse verse = Verse.builder()
        .text(verseDTO.getText())
        .shortDescription(verseDTO.getShortDescription())
        .imageUrl(verseDTO.getImageUrl())
        .user(user)
        .title(verseDTO.getTitle())
        .build();
    verseRepository.save(verse);
  }

  public void deleteVerse(Long verseId) {
    Verse verse = verseRepository.findById(verseId).orElseThrow();
    verseRepository.delete(verse);
  }
}
