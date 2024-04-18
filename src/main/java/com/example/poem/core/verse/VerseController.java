package com.example.poem.core.verse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/verse")
public class VerseController {

  static List<Verse> verseList = List.of(
      Verse.builder().text(
              " \"May the God of hope fill you with all joy and peace as you trust in him, so that you may overflow with hope by the power of the Holy Spirit.\" - Romans 15:13")
          .title("verse1")
          .shortDescription("Short desc1")
          .imageUrl("images/zdjecie7.jpg")
          .build(),
      Verse.builder().text(
              " \"May the God of hope fill you with all joy and peace as you trust in him, so that you may overflow with hope by the power of the Holy Spirit.\" - Romans 15:13")
          .title("verse2")
          .shortDescription("Short desc1")
          .imageUrl("images/zdjecie7.jpg")
          .build()
  );

  @GetMapping
  public String getAllVerses(Model model) {
    System.out.println("getAllVerses()");
    model.addAttribute("verses", verseList);
    return "verse/allVerses";
  }
}
