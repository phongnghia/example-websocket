package com.phongnghia.example_websocket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfigRest implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/rest/**")
                .allowedOrigins("**")
                .allowedMethods("POST", "GET")
                .allowedHeaders("*");
    }

}
