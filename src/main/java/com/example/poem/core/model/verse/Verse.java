package com.example.poem.core.model.verse;

import com.example.poem.core.model.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Collection;
import java.util.List;

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
  @Size(max = 50)
  private String title;

  @NotNull
  @Size(max = 100)
  private String shortDescription;

  @NotNull
  @Size(max = 50000)
  private String text;

  @NotNull
  private Integer likes;

  private String imageUrl;
  private Integer commentsSize;
  private Integer views;

}
