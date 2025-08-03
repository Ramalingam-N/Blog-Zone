package com.blog.service;

import com.blog.entity.Comment;
import com.blog.entity.CommentLike;
import com.blog.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface CommentLikeService {
    void save(CommentLike commentLike);
    boolean isCommentLikeExists(User user, Comment comment);
}
