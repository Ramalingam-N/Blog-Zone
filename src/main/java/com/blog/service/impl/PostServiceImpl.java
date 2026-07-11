package com.blog.service.impl;

import com.blog.entity.Post;
import com.blog.entity.User;
import com.blog.repository.PostRepository;
import com.blog.service.PostService;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {
    private PostRepository postRepository;
    public PostServiceImpl(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @Override
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Override
    public String generateUrl() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    @Override
    public Post findByUrl(String url) {
        return postRepository.findByUrl(url);
    }

    @Override
    public String formatDate(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
        return localDateTime.format(formatter);
    }

    @Override
    public String formatImagePath(String path) {
        path = path.substring(25);
        String finalPath = "";
        for(int i = 0; i < path.length(); i++){
            if(path.charAt(i) == '\\')
                finalPath += "/";
            else
                finalPath += path.charAt(i);
        }
        return finalPath;
    }

    @Override
    public Post findPostById(Long id) {
        return postRepository.findById(id).get();
    }

    @Override
    public Long countSaved(Post post) {
        return postRepository.countAllSaved(post);
    }

    @Override
    public Long countLike(Post post) {
        return postRepository.countAllLike(post);
    }

    @Override
    public List<Post> findAllPostsByUserName(String userName) {
        return postRepository.findAllPostsByUserName(userName);
    }

    @Override
    public List<Post> findAllSavedPostByUserName(String userName) {
        return postRepository.findAllSavedPostByUserName(userName);
    }

    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Post findByCommentId(Long commentId) {
        return postRepository.findByCommentId(commentId);
    }

    @Override
    public List<Post> allPosts() {
        return postRepository.findAllByOrderByCreatedOnDesc();
    }

    @Override
    public List<Post> allPostsByFollowingUser(Set<User> followingUsers) {
        return postRepository.allPostsByFollowingUser(followingUsers);
    }
}