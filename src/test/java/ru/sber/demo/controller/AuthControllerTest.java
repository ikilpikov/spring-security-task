package ru.sber.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.sber.demo.domain.Role;
import ru.sber.demo.dto.RegisterRequest;
import ru.sber.demo.security.SecurityConfig;
import ru.sber.demo.service.UserService;

import javax.management.openmbean.KeyAlreadyExistsException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(AuthController.class)
@Import({SecurityConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private static final String VALID_USERNAME = "user";
    private static final String INVALID_USERNAME = "u1";

    private final RegisterRequest userDto = new RegisterRequest("user", "user", Role.ROLE_USER);

    @Test
    @WithAnonymousUser
    public void show_register_page_test() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithAnonymousUser
    public void register_valid_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", VALID_USERNAME)
                        .param("password", "password")
                        .param("role", "ROLE_USER"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Register success"));
    }

    @Test
    @WithAnonymousUser
    public void register_existing_id_user() throws Exception {
        doThrow(new KeyAlreadyExistsException())
                .when(userService)
                .registerUser(any(RegisterRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", VALID_USERNAME)
                        .param("password", "password")
                        .param("role", "ROLE_USER"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Register failed"));

    }

    @Test
    @WithAnonymousUser
    public void register_bad_credentials_user() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", INVALID_USERNAME)
                        .param("password", "2")
                        .param("role", "ROLE_USER"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("message"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Register failed"));

    }
}