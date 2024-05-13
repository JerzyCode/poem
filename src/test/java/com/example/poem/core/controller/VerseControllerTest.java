package com.example.poem.core.controller;

import com.example.poem.PoemApplication;
import com.example.poem.core.base.config.SecurityConfig;
import com.example.poem.core.base.config.WebMvcConfig;
import com.example.poem.core.base.exceptions.WrongUserException;
import com.example.poem.core.model.user.User;
import com.example.poem.core.model.verse.Verse;
import com.example.poem.core.model.verse.VerseDTO;
import com.example.poem.core.service.UserService;
import com.example.poem.core.service.VerseService;
import com.example.poem.core.shared.helpers.UserHelper;
import com.example.poem.core.shared.helpers.VerseHelper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VerseController.class)
@ContextConfiguration(classes = { PoemApplication.class, SecurityConfig.class, WebMvcConfig.class })
class VerseControllerTest {

  @MockBean
  private VerseService verseService;
  @MockBean
  private UserService userService;
  @Autowired
  private MockMvc mockMvc;
  private static final Long VERSE_ID = 1L;
  private static final Long USER_ID = 1L;

  @Test
  void should_return_home_view() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    List<Verse> verses = List.of(VerseHelper.prepareVerse(user), VerseHelper.prepareVerse(user));
    when(verseService.findRandomVerses()).thenReturn(verses);
    //when & then
    mockMvc.perform(get("/home"))
        .andExpect(status().isOk())
        .andExpect(view().name("home"))
        .andExpect(model().attribute("verses", verses));
    verify(verseService, Mockito.times(1)).findRandomVerses();
  }

  @Test
  void should_return_verse_details_view() throws Exception {
    //given
    Long verseId = 1L;
    User user = UserHelper.prepareTestUser();
    Verse verse = VerseHelper.prepareVerse(user);
    verse.setId(verseId);
    when(verseService.getVerse(verseId)).thenReturn(verse);
    //when & then
    mockMvc.perform(get("/verse/" + verseId))
        .andExpect(status().isOk())
        .andExpect(view().name("verse/verseDetails"))
        .andExpect(model().attribute("verse", verse));
    verify(verseService, Mockito.times(1)).getVerse(verseId);
  }

  @Test
  void should_return_all_verses_view() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    List<Verse> verses = List.of(VerseHelper.prepareVerse(user), VerseHelper.prepareVerse(user));
    user.setId(USER_ID);
    when(verseService.findAllByUserId(USER_ID)).thenReturn(verses);
    //when & then
    mockMvc.perform(get("/verses/" + USER_ID))
        .andExpect(status().isOk())
        .andExpect(view().name("verse/allVerses"))
        .andExpect(model().attribute("verses", verses))
        .andExpect(model().attribute("userId", USER_ID));
    verify(verseService, Mockito.times(1)).findAllByUserId(USER_ID);
  }

  @Test
  void should_add_verse() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();
    Verse verse = VerseHelper.prepareVerse(user);
    verse.setId(1L);
    //when & then
    mockMvc.perform(post("/rest/api/verse")
            .param("userId", String.valueOf(USER_ID))
            .flashAttr("verseDTO", verseDTO)
            .with(SecurityMockMvcRequestPostProcessors.csrf())
            .with(SecurityMockMvcRequestPostProcessors.user(user)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/verses/" + USER_ID));
    verify(verseService, times(1)).addVerse(verseDTO, USER_ID);
  }

  @Test
  void should_edit_verse() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();
    //when & then
    mockMvc.perform(post("/rest/api/verse/edit")
            .param("userId", String.valueOf(USER_ID))
            .param("verseId", String.valueOf(VERSE_ID))
            .flashAttr("verseDTO", verseDTO)
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user(user)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/verse/" + VERSE_ID));
    verify(verseService, times(1)).editVerse(verseDTO, VERSE_ID, USER_ID);
  }

  @Test
  void should_throw_edit_verse() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    VerseDTO verseDTO = VerseHelper.prepareVerseDto();
    doThrow(new WrongUserException("")).when(verseService).editVerse(verseDTO, VERSE_ID, USER_ID);
    //when & then
    mockMvc.perform(post("/rest/api/verse/edit")
            .param("userId", String.valueOf(USER_ID))
            .param("verseId", String.valueOf(VERSE_ID))
            .flashAttr("verseDTO", verseDTO)
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user(user)))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/verse/" + VERSE_ID + "?fail"));
    verify(verseService, times(1)).editVerse(verseDTO, VERSE_ID, USER_ID);
  }

  @Test
  void should_delete_verse() throws Exception {
    //given
    User user = UserHelper.prepareTestUser();
    //when & then
    mockMvc.perform(delete("/rest/api/verse")
            .param("verseId", String.valueOf(VERSE_ID))
            .with(csrf())
            .with(SecurityMockMvcRequestPostProcessors.user(user)))
        .andExpect(status().isOk());
    verify(verseService, times(1)).deleteVerse(VERSE_ID);
  }
}