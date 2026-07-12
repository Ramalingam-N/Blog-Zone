package com.blog.service;

import com.blog.repository.PostRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseKeepAliveService {

    private PostRepository postRepository;

    public DatabaseKeepAliveService(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Scheduled(fixedRate = 900000)
    public void pingDatabase() {
        try {
            long count = postRepository.count();
            System.out.println("Keep-Alive Ping: Database is awake. Total posts: " + count);
        } catch (Exception e) {
            System.err.println("Keep-Alive Ping Failed: " + e.getMessage());
        }
    }
}