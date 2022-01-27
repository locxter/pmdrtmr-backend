// Setting the package
package com.github.locxter.pmdrtmr.backend;

// Including needed classes/interfaces
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

// UserAuthenticationProvider class
@Component
public class UserAuthenticationProvider implements AuthenticationProvider
{
    // Attributes
    @Autowired
    private UserRepository userRepository;
    private final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    // Function for authenticating a user against the database
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null && PASSWORD_ENCODER.matches(password, user.getPassword()))
        {
            return new UsernamePasswordAuthenticationToken(
                username, password, new ArrayList<>());
        }
        else
        {
            return null;
        }
    }

    // Required function
    @Override
    public boolean supports(Class<?> authenticationToken)
    {
        return authenticationToken.equals(UsernamePasswordAuthenticationToken.class);
    }
}
