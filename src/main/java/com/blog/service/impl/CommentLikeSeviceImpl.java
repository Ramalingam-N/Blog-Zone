package com.blog.service.impl;

import com.blog.entity.Comment;
import com.blog.entity.CommentLike;
import com.blog.entity.User;
import com.blog.repository.CommentLikeRepository;
import com.blog.service.CommentLikeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeSeviceImpl implements CommentLikeService {
    private CommentLikeRepository commentLikeRepository;
    public CommentLikeSeviceImpl(CommentLikeRepository commentLikeRepository){
        this.commentLikeRepository = commentLikeRepository;
    }
    @Override
    public void save(CommentLike commentLike) {
        Optional<CommentLike> existsCommentLike = commentLikeRepository.findByUserIdAndCommentId(commentLike.getUser().getId(), commentLike.getComment().getId());
        if(existsCommentLike.isEmpty()){
            commentLikeRepository.save(commentLike);
        }
        else{
            commentLikeRepository.delete(existsCommentLike.get());
        }
    }

    @Override
    public boolean isCommentLikeExists(User user, Comment comment) {
        return !commentLikeRepository.findByUserIdAndCommentId(user.getId(), comment.getId()).isEmpty();
    }
}
