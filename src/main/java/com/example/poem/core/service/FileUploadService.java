package com.example.poem.core.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
@Slf4j
public class FileUploadService {
  protected static final String IMAGE_PATH = "images/%s.jpg";
  protected static final String NO_UPLOADED_IMAGE_PATH = "images/image_not_found.jpg";
  protected static final String MIME_TYPE_JPG = "image/jpg";
  protected static final String MIME_TYPE_JPEG = "image/jpeg";
  protected static final String JPG_EXT = ".jpg";
  protected static final String JPEG_EXT = ".jpeg";
  protected static final long MAX_SIZE_BYTES = 5242880L;
  protected static final String WRONG_MIMETYPE_MSG = "Mimetype %s is not supported.";
  protected static final String WRONG_EXTENSION_MSG = "Extension %s is not supported.";
  protected static final String WRONG_SIZE_MSG = "Image is to big. Max file size is " + MAX_SIZE_BYTES + ".";
  protected static final String WRONG_SAVE_FILE_MSG = "Saving file went wrong.";
  protected static final String FILE_IS_NOT_VALID_MSG = "File is invalid.";

  private String getImagePath(String verseTitle, Long verseId) {
    return String.format(IMAGE_PATH, verseTitle + verseId);
  }

  private boolean isValidMimeType(String mimeType) {
    return mimeType.contains(MIME_TYPE_JPEG) || mimeType.contains(MIME_TYPE_JPG);
  }

  private boolean isValidExtension(String extension) {
    return extension.equals(JPG_EXT) || extension.equals(JPEG_EXT);
  }

  private boolean isValidSize(long numOfBytes) {
    return numOfBytes <= MAX_SIZE_BYTES;
  }

  private String getExtension(String fileName) {
    return fileName.substring(fileName.lastIndexOf("."));
  }

  private boolean isValidImage(MultipartFile image) {
    String extension = getExtension(Objects.requireNonNull(image.getOriginalFilename()));
    String contentType = image.getContentType();
    String msg;
    if (contentType == null || !isValidMimeType(contentType)) {
      msg = String.format(WRONG_MIMETYPE_MSG, contentType);
      log.debug(msg);
      return false;
    }
    else if (!isValidExtension(extension)) {
      msg = String.format(WRONG_EXTENSION_MSG, extension);
      log.debug(msg);
      return false;
    }
    else if (!isValidSize(image.getSize())) {
      msg = WRONG_SIZE_MSG;
      log.debug(msg);
      return false;
    }

    return true;
  }

  private void saveFile(String filename, byte[] bytes) throws IOException {
    Path path = Path.of(filename);
    if (Files.exists(path)) {
      Files.delete(path);
    }
    Files.createFile(path);
    Files.write(path, bytes);
  }

  public String uploadFile(MultipartFile file, long verseId, String verseTitle) {
    String path = getImagePath(verseTitle, verseId);
    if (file.isEmpty()) {
      return NO_UPLOADED_IMAGE_PATH;
    }
    if (!isValidImage(file)) {
      log.info(FILE_IS_NOT_VALID_MSG);
      return NO_UPLOADED_IMAGE_PATH;
    }
    try {
      saveFile(path, file.getBytes());
    }
    catch (IOException e) {
      log.info(WRONG_SAVE_FILE_MSG);
      e.printStackTrace();
      return NO_UPLOADED_IMAGE_PATH;
    }
    return path;
  }
}
