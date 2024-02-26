package ru.sber.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.sber.demo.domain.Role;
import ru.sber.demo.domain.User;
import ru.sber.demo.security.SecurityConfig;
import ru.sber.demo.service.UserService;

import java.util.List;

import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> usersStub = List.of(
            new User("user", "hash", Role.ROLE_USER),
            new User("admin", "hash", Role.ROLE_ADMIN)
    );

    @Test
    @WithAnonymousUser
    void get_all_users_when_unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void get_all_users_by_admin() throws Exception {
        when(userService.fetchAllUsers()).thenReturn(usersStub);

        mockMvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("users"))
                .andExpect(MockMvcResultMatchers.model().attribute("users", usersStub));
    }

    @Test
    @WithMockUser
    void get_all_users_by_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/user/all"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}