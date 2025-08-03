package com.blog.service;

import com.blog.entity.Post;
import com.blog.entity.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public interface PostService {
    public void savePost(Post post);
    public String generateUrl();
    public Post findByUrl(String url);
    public String formatDate(LocalDateTime localDateTime);
    public String formatImagePath(String path);
    public Post findPostById(Long id);
    public Long countSaved(Post post);
    public Long countLike(Post post);
    public List<Post> findAllPostsByUserName(String userName);
    public List<Post> findAllSavedPostByUserName(String userName);
    void deleteById(Long id);
    Post findByCommentId(Long commentId);
    List<Post> allPosts();
    List<Post> allPostsByFollowingUser(Set<User> followingUsers);
}
