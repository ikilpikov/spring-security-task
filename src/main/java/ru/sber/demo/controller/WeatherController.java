package ru.sber.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/weather")
public class WeatherController {

    @GetMapping("/current")
    public String showWeather() {
        return "weather";
    }

}
