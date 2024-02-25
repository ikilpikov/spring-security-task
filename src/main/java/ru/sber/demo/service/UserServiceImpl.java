package ru.sber.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.sber.demo.domain.User;
import ru.sber.demo.dto.RegisterRequest;
import ru.sber.demo.repository.UserRepository;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public List<User> fetchAllUsers() {
        return repository.findAll();
    }

    @Override
    public void registerUser(RegisterRequest request) {
        if (userExists(request)) {
            throw new KeyAlreadyExistsException("User " + request.getUsername() + " already exists");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setRole(request.getRole());

        var encodedPassword = encoder.encode(request.getPassword());
        user.setPassword(encodedPassword);

        repository.save(user);
    }

    @Override
    public boolean userExists(RegisterRequest request) {
        return repository.findByUsername(request.getUsername()).isPresent();
    }

}
