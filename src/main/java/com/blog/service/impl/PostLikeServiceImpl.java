package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.entity.PostLike;
import com.blog.entity.User;
import com.blog.repository.PostLikeRepository;
import com.blog.service.PostLikeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostLikeServiceImpl implements PostLikeService {
    private PostLikeRepository postLikeRepository;
    public PostLikeServiceImpl(PostLikeRepository postLikeRepository){
        this.postLikeRepository = postLikeRepository;
    }
    @Override
    public void save(PostLike postLike) {
        Optional<PostLike> existingPostLike = postLikeRepository.findByUserIdAndPostId(postLike.getUser().getId(), postLike.getPost().getId());
        if(existingPostLike.isEmpty())
            postLikeRepository.save(postLike);
        else
            postLikeRepository.delete(existingPostLike.get());
    }

    @Override
    public boolean isLikeExists(User user, Post post) {
        return !postLikeRepository.findByUserIdAndPostId(user.getId(), post.getId()).isEmpty();
    }
}
