package com.self.login.user.controller;

import com.self.login.user.entity.ApplicationUser;
import com.self.login.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<ApplicationUser> getAllUsers(){
        return userService.getAllUsers();
    }
}
