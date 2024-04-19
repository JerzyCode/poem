package com.example.poem.core.base.config;

import com.example.poem.core.models.verse.Verse;
import com.example.poem.core.models.verse.VerseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DevConfig {
  private final VerseRepository repository;

  @Bean
  @Profile("mock")
  public CommandLineRunner createMocks() {
    log.info("Created Mocks!");

    List<Verse> verseList = List.of(
        Verse.builder()
            .text("Chybaby nie wieǳiała, co znaczy twarz blada\n" +
                "I kiedy kto nie g’rzeczy, Hanno, odpowiada,\n" +
                "Miłość\n" +
                "Często wzdycha, a rzadko kiedy się rozśmieగe —\n" +
                "Tedy nie wiesz, że prze cię moగe serce mdleగe?")
            .title("Do Hanny")
            .shortDescription("Fraszka Jana Kochanowskiego")
            .imageUrl("images/zdjecie7.jpg")
            .build(),
        Verse.builder()
            .text("Wiesz, coś mi winien; mieగże się do taszki⁸,\n" +
                "Bo cię wnet włożę, Jóstcie, mięǳy \uEECAaszki!")
            .title("Do Jósta")
            .shortDescription("Fraszka Jana Kochanowskiego")
            .imageUrl("images/zdjecie7.jpg")
            .build()
    );

    repository.saveAll(verseList);

    return args -> log.info("CREATED MOCKS!");
  }
}
