// Setting the package
package com.github.locxter.pmdrtmr.backend;

// Including needed classes/interfaces
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

// Main class
@SpringBootApplication
@EnableResourceServer
public class Main
{
    // Main function
    public static void main(String[] args)
    {
        SpringApplication.run(Main.class, args);
    }
}
