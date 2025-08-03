package com.blog.controller;

import com.blog.entity.User;
import com.blog.security.SecurityUtils;
import com.blog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.*;

@Controller
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/profile")
    public String profilePage(Model model){
        String email = SecurityUtils.getCurrentUser().getUsername();
        User user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/view-profile/{userName}")
    public String viewProfile(@PathVariable("userName") String userName, Model model){
        User user = userService.findByUserName(userName);
        model.addAttribute("user", user);
        User currentUser = userService.findByEmail(SecurityUtils.getCurrentUser().getUsername());
        boolean flag = false;
        if(user.getEmail().equals(SecurityUtils.getCurrentUser().getUsername()))
            model.addAttribute("checking", "none");
        for(User u : user.getFollowers()) {
            if(u.getEmail().equals(currentUser.getEmail())) {
                model.addAttribute("follow", "Following");
                flag = true;
                break;
            }
        }
        if(!flag) {
            model.addAttribute("follow", "Follow");
        }

        return "other-profile";
    }

    @GetMapping("/{userName}/following")
    public String following(@PathVariable("userName") String userName, Model model){
        Set<User> following = userService.findByUserName(userName).getFollowing();
        model.addAttribute("followers", following);
        model.addAttribute("name", "Following");
        List<String> list = new ArrayList<>();
        Set <User> users = userService.findByEmail(SecurityUtils.getCurrentUser().getUsername()).getFollowing();
        for (User u : following) {
            if (users.contains(u))
                list.add("Following");
            else if (u.getEmail().equals(SecurityUtils.getCurrentUser().getUsername())) {
                list.add("none");
            } else
                list.add("Follow");
        }
        model.addAttribute("follow", list);
        return "profile-list";
    }

    @GetMapping("/{userName}/followers")
    public String followers(@PathVariable("userName") String userName, Model model){
        Set<User> following = userService.findByUserName(userName).getFollowers();
        model.addAttribute("followers", following);
        model.addAttribute("name", "Followers");
        List<String> list = new ArrayList<>();
        Set <User> users = userService.findByEmail(SecurityUtils.getCurrentUser().getUsername()).getFollowing();

        for (User u : following) {
            if (users.contains(u))
                list.add("Following");
            else if (u.getEmail().equals(SecurityUtils.getCurrentUser().getUsername())) {
                list.add("none");
            } else
                list.add("Follow");
        }

        model.addAttribute("follow", list);
        return "profile-list";
    }

    @PostMapping("/follow-user/{userId}")
    public ResponseEntity<Map<String, String>> followUser(@PathVariable("userId") Long userId){
        Map<String, String> response = new HashMap<>();
        boolean flag = userService.followUser(userId);
        if(flag)
            response.put("follow", "Following");
        else
            response.put("follow", "Follow");
        User user = userService.findById(userId);
        response.put("followerCount", String.valueOf(user.getFollowers().size()));
        return ResponseEntity.ok(response);
    }
}
