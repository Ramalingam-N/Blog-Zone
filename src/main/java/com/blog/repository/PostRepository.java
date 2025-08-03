package com.blog.repository;

import com.blog.entity.Post;
import com.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;


public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByUrl(String url);
    @Query("SELECT MAX(p.id) FROM Post p")
    Long findMaxId();
    @Query("SELECT COUNT(s) FROM Saved s WHERE s.post = :post")
    Long countAllSaved(@Param("post") Post post);
    @Query("SELECT COUNT(p) FROM PostLike p WHERE p.post = :post")
    Long countAllLike(@Param("post") Post post);
    @Query("SELECT p FROM Post p WHERE p.user.userName = :userName")
    List<Post> findAllPostsByUserName(String userName);
    @Query("SELECT s.post FROM Saved s WHERE s.user.userName = :userName")
    List<Post> findAllSavedPostByUserName(String userName);
    @Query("SELECT c.post FROM Comment c WHERE c.id = :commentId")
    Post findByCommentId(@Param("commentId") Long commentId);

    @Query("SELECT p FROM Post p WHERE p.user IN :followingUsers ORDER BY p.createdOn DESC")
    List<Post> allPostsByFollowingUser(@Param("followingUsers") Set<User> followingUsers);
}
