package com.example.poem.core.controller;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.service.UserDataService;
import com.example.poem.core.service.UserService;
import com.example.poem.core.service.VerseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class LikeCommentController {

  private final VerseService verseService;
  private final UserDataService userDataService;
  private final UserService userService;

  @PutMapping("/rest/api/like")
  public ResponseEntity<Void> verseLikeUnlike(@RequestParam Long verseId) {
    Long userId = getUserId();
    Verse verse = verseService.getVerse(verseId);
    User user = userService.findById(userId);
    userDataService.changeVerseLikedForUser(user, verse, verseService.isLikedByUser(verseId));
    return ResponseEntity.ok().build();
  }

  private Long getUserId() {
    User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return user.getId();
  }
}
