package com.blog.repository;

import com.blog.entity.Saved;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedRepository extends JpaRepository<Saved, Long> {
    Optional<Saved> findByUserIdAndPostId(Long userId, Long postId);
}
