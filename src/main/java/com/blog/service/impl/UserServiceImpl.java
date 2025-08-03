package com.blog.service.impl;

import com.blog.entity.User;
import com.blog.repository.UserRepository;
import com.blog.security.SecurityUtils;
import com.blog.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public void saveUser(User user) {
        System.out.println("Entered password: "+user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("Encoded password: "+user.getPassword());
        userRepository.save(user);
    }

    @Override
    public boolean userExistsByUserName(String userName) {
        return userRepository.existsByUserName(userName);
    }

    @Override
    public boolean userExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findByEmail(String email){

        return userRepository.findByEmail(email);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public boolean followUser(Long id) {
        User currentUser = userRepository.findByEmail(SecurityUtils.getCurrentUser().getUsername());
        User user = userRepository.findById(id).get();
        Set<User> userfollowers = user.getFollowers();
        Set<User> currentUserfollowers = currentUser.getFollowing();

        if (userfollowers.contains(currentUser)){
            userfollowers.remove(currentUser);
            currentUserfollowers.remove(user);
            userRepository.save(currentUser);
            userRepository.save(user);
            return false;
        }
        else {
            userfollowers.add(currentUser);
            currentUserfollowers.add(user);
            userRepository.save(currentUser);
            userRepository.save(user);
            return true;
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }
}
