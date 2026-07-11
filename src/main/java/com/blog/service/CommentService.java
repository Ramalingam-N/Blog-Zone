package com.blog.service;

import com.blog.entity.Comment;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface CommentService {
    void saveComment(Comment comment);
    Set<Comment> findCommentsByPostId(Long id);
    Comment findCommentById(Long id);
    Long countCommentLike(Comment comment);
    void deleteById(Long id);
}