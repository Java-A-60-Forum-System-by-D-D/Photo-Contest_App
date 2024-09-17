package com.example.demo.ServiceTests;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.example.demo.services.impl.CloudinaryImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CloudinaryImageServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryImageServiceImpl cloudinaryImageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void uploadImageSuccessfullyUploadsImage() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        File tempFile = File.createTempFile("temp-file", "test.jpg");
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        doNothing().when(multipartFile).transferTo(tempFile);
        when(uploader.upload(any(File.class), anyMap())).thenReturn(Map.of("url", "http://example.com/image.jpg"));

        String result = cloudinaryImageService.uploadImage(multipartFile);

        assertEquals("http://example.com/image.jpg", result);
    }

    @Test
    void uploadImageThrowsExceptionWhenFileCreationFails() {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        assertThrows(RuntimeException.class, () -> cloudinaryImageService.uploadImage(multipartFile));
    }

    @Test
    void uploadImageThrowsExceptionWhenTransferFails() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        File tempFile = File.createTempFile("temp-file", "test.jpg");
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        doThrow(IOException.class).when(multipartFile).transferTo(tempFile);

        assertThrows(RuntimeException.class, () -> cloudinaryImageService.uploadImage(multipartFile));
    }

    @Test
    void uploadImageThrowsExceptionWhenUploadFails() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        File tempFile = File.createTempFile("temp-file", "test.jpg");
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        doNothing().when(multipartFile).transferTo(tempFile);
        when(uploader.upload(any(File.class), anyMap())).thenThrow(IOException.class);

        assertThrows(RuntimeException.class, () -> cloudinaryImageService.uploadImage(multipartFile));
    }

    @Test
    void uploadImageFromUrlSuccessfullyUploadsImage() throws IOException {
        when(uploader.upload(anyString(), anyMap())).thenReturn(Map.of("url", "http://example.com/image.jpg"));

        String result = cloudinaryImageService.uploadImageFromUrl("http://example.com/image.jpg");

        assertEquals("http://example.com/image.jpg", result);
    }

    @Test
    void uploadImageFromUrlThrowsExceptionWhenUploadFails() throws IOException {
        when(uploader.upload(anyString(), anyMap())).thenThrow(IOException.class);

        assertThrows(IOException.class, () -> cloudinaryImageService.uploadImageFromUrl("http://example.com/image.jpg"));
    }
}