package ru.sber.demo.service;

import ru.sber.demo.domain.User;
import ru.sber.demo.dto.LoginRequest;
import ru.sber.demo.dto.RegisterRequest;

import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String, String> authenticateUser(LoginRequest loginRequest) throws Exception;

    List<User> fetchAllUsers();

    Map<String, String> registerUser(RegisterRequest request) throws Exception;

    boolean userExists(RegisterRequest request);

}
