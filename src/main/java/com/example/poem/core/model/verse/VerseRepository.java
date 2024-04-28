package com.example.poem.core.model.verse;

import com.example.poem.core.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerseRepository extends JpaRepository<Verse, Long> {

  List<Verse> findAllByUser(User user);

}
