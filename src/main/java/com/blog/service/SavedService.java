package com.blog.service;

import com.blog.entity.Post;
import com.blog.entity.Saved;
import com.blog.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface SavedService {
    void save(Saved saved);
    boolean isSavedExists(User user, Post post);
}
