package com.example.poem.core.service;

import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseAssert;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.model.verse.VerseRepository;
import com.example.poem.core.shared.helpers.UserHelper;
import com.example.poem.core.shared.helpers.VerseHelper;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.ListAssert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class VerseServiceTest {

  @Autowired
  private VerseRepository verseRepository;
  @Autowired
  private UserRepository userRepository;
  private User user;
  private VerseService sut;

  @BeforeEach
  void setUp() {
    user = createUser();
    sut = new VerseService(verseRepository, userRepository);
  }

  @AfterEach
  void cleanUp() {
    verseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  @Transactional
  void add_verse_should_save_verse() {
    //given
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();

    //when
    sut.addVerse(verseDTO, user.getId());

    //then
    Verse saved = verseRepository.findAllByUser(user).get(0);
    VerseAssert.assertThat(saved)
        .isNotNull()
        .hasText(verseDTO.getText())
        .hasUser(user)
        .hasTitle(verseDTO.getTitle())
        .hasShortDescription(verseDTO.getShortDescription());
  }

  @Test
  void add_verse_wrong_params_should_throw() {
    //given
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();
    verseDTO.setTitle(null);

    //when & then
    assertThrows(ConstraintViolationException.class, () -> sut.addVerse(verseDTO, user.getId()));
  }

  @Test
  @Transactional
  void get_verse_should_return_verse() {
    //given
    Verse verse = verseRepository.save(VerseHelper.prepareVerse(user));

    //when
    Verse result = sut.getVerse(verse.getId());

    //then
    VerseAssert.assertThat(result)
        .isNotNull()
        .hasText(verse.getText())
        .hasUser(verse.getUser())
        .hasTitle(verse.getTitle())
        .hasShortDescription(verse.getShortDescription());
  }

  @Test
  void get_verse_wrong_id_should_throw() {
    //given
    //when & then
    assertThrows(NoSuchElementException.class, () -> sut.getVerse(123L));
  }

  @Test
  @Transactional
  void should_return_all_verse_by_user() {
    //given
    Verse verse1 = verseRepository.save(VerseHelper.prepareVerse(user));
    Verse verse2 = verseRepository.save(VerseHelper.prepareVerse(user));

    //when
    List<Verse> result = sut.findAllByUserId(user.getId());

    //then
    ListAssert.assertThatList(result)
        .isNotNull()
        .hasSize(2)
        .hasSameElementsAs(List.of(verse1, verse2));
  }

  @Test
  @Transactional
  void should_delete_verse() {
    //given
    Verse verse1 = verseRepository.save(VerseHelper.prepareVerse(user));
    Verse verse2 = verseRepository.save(VerseHelper.prepareVerse(user));
    verse2.setTitle("titleToDelet");

    //when
    sut.deleteVerse(verse1.getId());

    //then
    List<Verse> verses = verseRepository.findAllByUser(user);
    ListAssert.assertThatList(verses)
        .isNotNull()
        .hasSize(1)
        .hasSameElementsAs(List.of(verse2));
    VerseAssert.assertThat(verses.get(0))
        .hasUser(user)
        .hasText(verse2.getText())
        .hasTitle(verse2.getTitle())
        .hasId(verse2.getId())
        .hasShortDescription(verse2.getShortDescription());
  }

  @Test
  void find_random_verse_should_return_zero() {
    //given
    //when
    List<Verse> verses = sut.findRandomVerses();
    //then
    ListAssert.assertThatList(verses)
        .isNotNull()
        .hasSize(0);
  }

  @Test
  void find_random_verse_should_return_one() {
    //given
    verseRepository.save(VerseHelper.prepareVerse(user));
    //when
    List<Verse> verses = sut.findRandomVerses();
    //then
    ListAssert.assertThatList(verses)
        .isNotNull()
        .hasSize(1);
  }

  @Test
  void find_random_verse_should_return_two() {
    //given
    verseRepository.save(VerseHelper.prepareVerse(user));
    verseRepository.save(VerseHelper.prepareVerse(user));
    //when
    List<Verse> verses = sut.findRandomVerses();
    //then
    ListAssert.assertThatList(verses)
        .isNotNull()
        .hasSize(2);
  }

  @Test
  void find_random_verse_should_return_three() {
    //given
    verseRepository.save(VerseHelper.prepareVerse(user));
    verseRepository.save(VerseHelper.prepareVerse(user));
    verseRepository.save(VerseHelper.prepareVerse(user));

    //when
    List<Verse> verses = sut.findRandomVerses();
    //then
    ListAssert.assertThatList(verses)
        .isNotNull()
        .hasSize(3);
  }

  User createUser() {
    return userRepository.save(UserHelper.prepareTestUser());
  }

}