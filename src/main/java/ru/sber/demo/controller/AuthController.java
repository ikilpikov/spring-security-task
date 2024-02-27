package ru.sber.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import ru.sber.demo.dto.LoginRequest;
import ru.sber.demo.dto.RegisterRequest;
import ru.sber.demo.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.validation.Valid;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute @Valid RegisterRequest request, Model model) {
        //userService.registerUser(request);
        model.addAttribute("message", "Register success");
        return "register";
    }

    @ExceptionHandler({BindException.class, KeyAlreadyExistsException.class})
    public String handleValidationException(Exception ex, Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        model.addAttribute("message", "Register failed");
        return "register";
    }

}
