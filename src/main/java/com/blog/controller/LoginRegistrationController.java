package com.blog.controller;

import com.blog.entity.User;
import com.blog.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginRegistrationController {
    private UserService userService;
    public LoginRegistrationController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    @GetMapping("/register")
    public String registerPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model){
        if(userService.userExistsByEmail(user.getEmail())){
            model.addAttribute("emailError", "Already logged in with this email");
            return "register";
        }
        if(userService.userExistsByUserName(user.getUserName())){
            model.addAttribute("userNameError", "Username already exists.");
            return "register";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }
}
