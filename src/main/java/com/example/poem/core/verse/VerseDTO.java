package com.example.poem.core.verse;

import lombok.*;

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
  private String imageUrl;
}
