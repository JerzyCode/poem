package com.example.poem.core.service;

import com.example.poem.core.base.exceptions.WrongUserException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.model.user.UserRepository;
import com.example.poem.core.model.verse.Verse;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class VerseServiceTest {
  @MockBean
  private FileUploadService fileUploadService;

  @Autowired
  private VerseRepository verseRepository;
  @Autowired
  private UserRepository userRepository;
  private User user;
  private VerseService sut;

  @BeforeEach
  void setUp() {
    user = createUser();
    sut = new VerseService(verseRepository, userRepository, fileUploadService);
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
    assertThat(saved).isNotNull();
    assertThat(saved.getText()).isEqualTo(verseDTO.getText());
    assertThat(saved.getUser()).isEqualTo(user);
    assertThat(saved.getTitle()).isEqualTo(verseDTO.getTitle());
    assertThat(saved.getShortDescription()).isEqualTo(verseDTO.getShortDescription());
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
    assertThat(result).isNotNull();
    assertThat(result.getText()).isEqualTo(verse.getText());
    assertThat(result.getUser()).isEqualTo(user);
    assertThat(result.getTitle()).isEqualTo(verse.getTitle());
    assertThat(result.getShortDescription()).isEqualTo(verse.getShortDescription());
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
    assertThat(verses.get(0)).isNotNull();
    assertThat(verses.get(0).getText()).isEqualTo(verse2.getText());
    assertThat(verses.get(0).getUser()).isEqualTo(user);
    assertThat(verses.get(0).getTitle()).isEqualTo(verse2.getTitle());
    assertThat(verses.get(0).getId()).isEqualTo(verse2.getId());
    assertThat(verses.get(0).getShortDescription()).isEqualTo(verse2.getShortDescription());
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

  @Test
  @Transactional
  void should_edit_verse() throws WrongUserException {
    //given
    Verse toEdit = verseRepository.save(VerseHelper.prepareVerse(user));
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();
    verseDTO.setTitle("Changed title");

    //when
    sut.editVerse(verseDTO, toEdit.getId(), user.getId());

    //then
    Verse edited = verseRepository.findById(toEdit.getId()).orElseThrow();
    assertThat(edited).isNotNull();
    assertThat(edited.getText()).isEqualTo(verseDTO.getText());
    assertThat(edited.getUser()).isEqualTo(user);
    assertThat(edited.getTitle()).isEqualTo(verseDTO.getTitle());
    assertThat(edited.getShortDescription()).isEqualTo(verseDTO.getShortDescription());
  }

  @Test
  void should_throw_wrong_user() {
    //given
    Verse toEdit = verseRepository.save(VerseHelper.prepareVerse(user));
    User wrongUser = userRepository.save(UserHelper.prepareTestUser());
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();
    verseDTO.setTitle("Changed title");

    //when && then
    assertThrows(WrongUserException.class, () -> sut.editVerse(verseDTO, toEdit.getId(), wrongUser.getId()));
  }

  User createUser() {
    return userRepository.save(UserHelper.prepareTestUser());
  }

}