package com.github.locxter.pmdrtmr.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;

// AuthorizationServerConfiguration class
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter
{
    // Attributes
    @Autowired
    private AuthenticationManager authenticationManager;

    // Required function
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) throws Exception
    {
        authorizationServerEndpointsConfigurer.authenticationManager(authenticationManager);
    }

    // Function to add an in-memory client for authentication
    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception
    {
        clientDetailsServiceConfigurer.inMemory().withClient("pmdrtmr").secret("{noop}pmdrtmr").authorizedGrantTypes("password").scopes("all");
    }
}
