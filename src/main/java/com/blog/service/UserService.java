package com.blog.service;

import com.blog.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void saveUser(User user);
    public boolean userExistsByUserName(String userName);
    public boolean userExistsByEmail(String email);
    User findByEmail(String email);
    User findByUserName(String userName);
    boolean followUser(Long id);
    User findById(Long id);
}
