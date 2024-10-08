package com.example.poem.core.shared.helpers;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import org.springframework.mock.web.MockMultipartFile;

public class VerseHelper {
  private static final String IMAGE_URL = "image_url";
  private static final String TEXT = "text text text";
  private static final String SHORT_TEXT = "shorttext";
  private static final String TITLE = "Title";

  public static VerseDTO prepareVerseDto() {
    return VerseDTO.builder()
        .image(new MockMultipartFile(TITLE, new byte[] {}))
        .shortDescription(SHORT_TEXT)
        .text(TEXT)
        .title(TITLE)
        .build();
  }

  public static Verse prepareVerse(User user) {
    return Verse.builder()
        .imageUrl(IMAGE_URL)
        .user(user)
        .shortDescription(SHORT_TEXT)
        .text(TEXT)
        .likes(0)
        .commentsSize(0)
        .views(123)
        .title(TITLE)
        .build();
  }

  public static Verse prepareVerse() {
    return Verse.builder()
        .imageUrl(IMAGE_URL)
        .shortDescription(SHORT_TEXT)
        .text(TEXT)
        .title(TITLE)
        .build();
  }

}
