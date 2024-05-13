package com.example.poem.core.model.verse;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VerseDTO {
  private String title;
  private String shortDescription;
  private String text;
  private MultipartFile image;
}
