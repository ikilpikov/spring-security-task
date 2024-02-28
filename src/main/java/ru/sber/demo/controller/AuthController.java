package ru.sber.demo.controller;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import ru.sber.demo.dto.LoginRequest;
import ru.sber.demo.dto.RegisterRequest;
import ru.sber.demo.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Map<String, String> authenticate(@RequestBody LoginRequest loginRequest) throws Exception {
        return userService.authenticateUser(loginRequest);
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody RegisterRequest registerRequest) throws Exception {
        return userService.registerUser(registerRequest);
    }

    @ExceptionHandler({BadCredentialsException.class, ExpiredJwtException.class})
    public ResponseEntity<String> handleJwtException(Exception ex) {
        return new ResponseEntity<>("cannot authenticate", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BindException.class, KeyAlreadyExistsException.class})
    public ResponseEntity<String> handleValidationException(Exception ex) {
        return new ResponseEntity<>("register failed", HttpStatus.BAD_REQUEST);
    }

}
