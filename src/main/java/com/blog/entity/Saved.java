package com.blog.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "saved")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Saved {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
