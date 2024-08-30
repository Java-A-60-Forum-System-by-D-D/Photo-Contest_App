package com.example.demo.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
@Service
public class CloudinaryImageServiceImpl implements CloudinaryImageService {
    private static final String TEMP_FILE = "temp-file";
    private static final String TEMP_FILE_PREFIX = "temp-file-";
    private static final String URL_KEY = "url";

    private final Cloudinary cloudinary;

    public CloudinaryImageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        File file = null;

        try {
            file = File.createTempFile(TEMP_FILE, multipartFile.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return this.cloudinary
                    .uploader()
                    .upload(file, Collections.emptyMap())
                    .get(URL_KEY)
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadImageFromUrl(String imageUrl) throws IOException {

        Map result = cloudinary.uploader()
                .upload(imageUrl, ObjectUtils.asMap("resource_type", "image"));
        return result.get("url")
                .toString();

    }
}
