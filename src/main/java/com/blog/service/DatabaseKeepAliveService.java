package com.blog.service;

import com.blog.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class DatabaseKeepAliveService {

    @Autowired
    private PostRepository postRepository;

    // Runs every 15 minutes (900,000 milliseconds)
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