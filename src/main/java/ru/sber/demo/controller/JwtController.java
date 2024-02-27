package ru.sber.demo.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.*;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import ru.sber.demo.dto.LoginRequest;
import ru.sber.demo.dto.RegisterRequest;
import ru.sber.demo.repository.UserRepository;
import ru.sber.demo.security.TokenProvider;
import ru.sber.demo.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class JwtController {

    private final UserService userService;

    public JwtController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String authenticate(HttpServletRequest request, @ModelAttribute LoginRequest loginRequest) throws Exception {
        var token = userService.authenticateUser(loginRequest);
        request.getSession().setAttribute("token", token);
        return "redirect:/weather/current";
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest registerRequest) throws Exception {
        return userService.registerUser(registerRequest);
    }

    @ExceptionHandler({BadCredentialsException.class, ExpiredJwtException.class})
    public String handleJwtException(Exception ex) {
        return "register";
    }

    @ExceptionHandler({BindException.class, KeyAlreadyExistsException.class})
    public String handleValidationException(Exception ex) {
        return "register";
    }

}
