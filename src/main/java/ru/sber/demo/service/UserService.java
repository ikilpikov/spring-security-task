package ru.sber.demo.service;

import ru.sber.demo.domain.User;
import ru.sber.demo.dto.RegisterRequest;

import java.util.List;

public interface UserService {

    List<User> fetchAllUsers();

    void registerUser(RegisterRequest request);

    boolean userExists(RegisterRequest request);

}
