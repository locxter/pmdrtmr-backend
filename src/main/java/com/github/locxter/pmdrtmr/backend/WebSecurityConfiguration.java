// Setting the package
package com.github.locxter.pmdrtmr.backend;

// Including needed classes/interfaces
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

// WebSecurityConfiguration class
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter
{
    // Attributes
    @Autowired
    private UserAuthenticationProvider userAuthenticationProvider;

    // Function to remove /signup from the secured URLs
    @Override
    public void configure(WebSecurity webSecurity) throws Exception
    {
        webSecurity.ignoring().mvcMatchers("/signup");
    }

    // Function to protect all available URLs via authentication
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.authorizeRequests().mvcMatchers("/**").authenticated();
    }

    // Required function
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    // Function to use the custom authentication provider
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception
    {
        authenticationManagerBuilder.authenticationProvider(userAuthenticationProvider);
    }
}
