package com.example.poem.core.model.user;

import com.example.poem.core.model.verse.Verse;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_data")
public class UserData {
  @Id
  @Column(name = "user_data_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "verse_id")
  private List<Verse> likedVerses;
}
