package com.example.poem.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.example.poem.core.service.FileUploadService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileUploadServiceTest {
  private static final String FILENAME = "testFile";
  private static final String EXE_EXT = ".exe";
  private static final Long verseId = 123L;
  private static final String verseTitle = "testVerseName";
  private FileUploadService sut;

  @BeforeEach
  void setUp() {
    sut = new FileUploadService();
  }

  @Test
  void should_save_file() {
    //given
    byte[] content = new byte[123];
    MultipartFile file = new MockMultipartFile(
        FILENAME,
        FILENAME + JPG_EXT,
        MIME_TYPE_JPG,
        content
    );

    //when
    String fileUrl = sut.uploadFile(file, verseId, verseTitle);

    //then
    assertEquals(String.format(IMAGE_PATH, verseTitle + verseId), fileUrl);
  }

  @Test
  void should_not_save_file_wrong_ext() {
    //given
    byte[] content = new byte[123];
    MultipartFile file = new MockMultipartFile(
        FILENAME,
        FILENAME + EXE_EXT,
        MIME_TYPE_JPG,
        content
    );

    //when
    String fileUrl = sut.uploadFile(file, verseId, verseTitle);

    //then
    assertEquals(NO_UPLOADED_IMAGE_PATH, fileUrl);
  }

  @Test
  void should_not_save_file_wrong_mimetype() {
    //given
    byte[] content = new byte[123];
    MultipartFile file = new MockMultipartFile(
        FILENAME,
        FILENAME + ".jpg",
        "plain/text",
        content
    );

    //when
    String fileUrl = sut.uploadFile(file, verseId, verseTitle);

    //then
    assertEquals(NO_UPLOADED_IMAGE_PATH, fileUrl);
  }

  @Test
  void should_not_save_file_to_big() {
    //given
    byte[] content = new byte[(int)(MAX_SIZE_BYTES + 1)];
    MultipartFile file = new MockMultipartFile(
        FILENAME,
        FILENAME + ".jpg",
        "plain/text",
        content
    );

    //when
    String fileUrl = sut.uploadFile(file, verseId, verseTitle);

    //then
    assertEquals(NO_UPLOADED_IMAGE_PATH, fileUrl);
  }

}