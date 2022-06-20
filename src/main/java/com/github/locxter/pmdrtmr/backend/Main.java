package com.github.locxter.pmdrtmr.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

// Main class
@SpringBootApplication
@EnableResourceServer
public class Main {
    // Main method
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
