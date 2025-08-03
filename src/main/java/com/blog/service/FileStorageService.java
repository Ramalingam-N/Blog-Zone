package com.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileStorageService {
    public String storeFile(MultipartFile multipartFile, String postUrl) throws IOException;
    public void deleteImage(String imagePath);
}
