package ru.sber.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.sber.demo.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.fetchAllUsers());
        return "users";
    }

}
