package com.example.poem.core.model.verse;

import com.example.poem.core.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Verse {

  @Id
  @Column(name = "verse_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @NotNull
  private String title;

  @NotNull
  private String shortDescription;

  @NotNull
  private String text;

  private String imageUrl;

}
