package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import com.blog.repository.CommentRepository;
import com.blog.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.Set;
@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    public CommentServiceImpl(CommentRepository commentRepository){
        this.commentRepository = commentRepository;
    }

    @Override
    public void saveComment(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Set<Comment> findCommentsByPostId(Long id) {
        return commentRepository.findCommentsByPostId(id);
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).get();
    }

    @Override
    public Long countCommentLike(Comment comment) {
        return commentRepository.countAllLike(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
