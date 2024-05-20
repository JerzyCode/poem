package com.example.poem.core.base.config;

import com.example.poem.core.base.exceptions.UsernameTakenException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRole;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseRepository;
import com.example.poem.core.service.UserDataService;
import com.example.poem.core.service.UserService;
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
  private final UserDataService userDataService;
  private final UserService service;

  @Bean
  @Profile("mock")
  public CommandLineRunner createMocks() throws UsernameTakenException {
    log.info("Created Mocks!");
    User user = service.register(User.builder()
        .username("test@wp.pl")
        .name("Jerzy")
        .surname("Boksa")
        .password("test123")
        .role(UserRole.USER)
        .build());

    userDataService.createUserData(user);

    List<Verse> verseList = List.of(
        Verse.builder()
            .text("Chybaby nie wieǳiała, co znaczy twarz blada\n" +
                "I kiedy kto nie g’rzeczy, Hanno, odpowiada,\n" +
                "Miłość\n" +
                "Często wzdycha, a rzadko kiedy się rozśmieగe —\n" +
                "Tedy nie wiesz, że prze cię moగe serce mdleగe?")
            .title("Do Hanny")
            .likes(123)
            .commentsSize(14)
            .views(194292)
            .shortDescription("Fraszka Jana Kochanowskiego")
            .imageUrl("images/zdjecie7.jpg")
            .user(user)
            .build(),
        Verse.builder()
            .text("Wiesz, coś mi winien; mieగże się do taszki⁸,\n" +
                "Bo cię wnet włożę, Jóstcie, mięǳy \uEECAaszki!")
            .title("Do Jósta")
            .likes(51)
            .commentsSize(17)
            .views(5123)
            .shortDescription("Fraszka Jana Kochanowskiego")
            .imageUrl("images/zdjecie7.jpg")
            .user(user)
            .build()
    );

    repository.saveAll(verseList);

    return args -> log.info("CREATED MOCKS!");
  }

}
