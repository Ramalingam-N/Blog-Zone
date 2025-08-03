package com.blog.service;

import com.blog.entity.Post;
import com.blog.entity.PostLike;
import com.blog.entity.Saved;
import com.blog.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface PostLikeService {
    void save(PostLike postLike);
    boolean isLikeExists(User user, Post post);
}
