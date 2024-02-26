package ru.sber.demo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.sber.demo.security.SecurityConfig;

@WebMvcTest(WeatherController.class)
@Import(SecurityConfig.class)
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void show_weather_page() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/current"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("weather"));
    }

    @Test
    @WithAnonymousUser
    void show_weather_page_when_unauthorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/current"))
                .andExpect(MockMvcResultMatchers.status().isFound());
    }

}