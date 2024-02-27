package ru.sber.demo.service;

import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.demo.domain.User;
import ru.sber.demo.dto.LoginRequest;
import ru.sber.demo.dto.RegisterRequest;
import ru.sber.demo.repository.UserRepository;
import ru.sber.demo.security.TokenProvider;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.Map;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    private final AuthenticationManager authenticationManager;

    private final TokenProvider tokenProvider;

    public UserServiceImpl(UserRepository repository,
                           PasswordEncoder encoder,
                           AuthenticationManager authenticationManager,
                           TokenProvider tokenProvider) {

        this.repository = repository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Map<String, String> authenticateUser(LoginRequest loginRequest) throws Exception {
        return createToken(loginRequest.getUsername(), loginRequest.getPassword());
    }

    @Override
    public List<User> fetchAllUsers() {
        return repository.findAll();
    }

    @Override
    public Map<String, String> registerUser(RegisterRequest request) {
        if (userExists(request)) {
            throw new KeyAlreadyExistsException("User " + request.getUsername() + " already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());

        var encodedPassword = encoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        repository.save(user);
        return createToken(request.getUsername(), request.getPassword());
    }

    @Override
    public boolean userExists(RegisterRequest request) {
        return repository.findByUsername(request.getUsername()).isPresent();
    }

    private Map<String, String> createToken(String username, String password) {
        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                username,
                password
        );

        authenticationManager.authenticate(authInputToken);
        String token = tokenProvider.createToken(username);
        return Map.of("token", token);
    }

}
