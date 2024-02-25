package ru.sber.demo.dto;


import ru.sber.demo.domain.Role;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.Pattern;

public class RegisterRequest {

    @Size(min = 3, max = 40, message = "Username must be from 3 to 40 characters long")
    @Pattern(regexp = "^[a-zA-Z0-9_]*$", message = "Username should contain characters, digits and underscore")
    private String username;

    @NotBlank
    @Size(min = 4, max = 40, message = "Password must be from 4 to 40 characters long")
    private String password;

    private Role role;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
