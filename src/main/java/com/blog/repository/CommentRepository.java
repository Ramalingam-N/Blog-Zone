package com.blog.repository;

import com.blog.entity.Comment;
import com.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public Set<Comment> findCommentsByPostId(Long id);
    @Query("SELECT COUNT(c) FROM CommentLike c WHERE c.comment = :comment")
    Long countAllLike(@Param("comment") Comment comment);

}
