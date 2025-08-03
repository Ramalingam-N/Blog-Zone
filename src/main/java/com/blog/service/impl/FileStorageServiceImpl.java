package com.blog.service.impl;

import com.blog.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private String uploadDir = "src/main/resources/static/uploads";
    @Override
    public String storeFile(MultipartFile file, String postUrl) throws IOException {
        if(file.isEmpty()){
            return "Failed";
        }
        File dir = new File(uploadDir);
        if(!dir.exists()){
            dir.mkdir();
        }
        Path filePath = Paths.get(uploadDir, postUrl+".jpg");
        Files.write(filePath, file.getBytes());
        return filePath.toString();
    }

    @Override
    public void deleteImage(String imagePath) {
        File imageFile = new File("Is---------------------------------------------------------"+imagePath);
        System.out.println(imageFile);
        if (imageFile.exists()) {
            System.out.println("--------------------------------------------------------------------------Done...");
             imageFile.delete();
        }
    }
}
