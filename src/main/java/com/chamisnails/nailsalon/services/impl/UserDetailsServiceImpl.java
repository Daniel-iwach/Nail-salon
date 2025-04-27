package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.persistence.model.UserDocument;
import com.chamisnails.nailsalon.persistence.repository.IUserRepository;
import com.chamisnails.nailsalon.presentation.dto.AuthLoginRequest;
import com.chamisnails.nailsalon.presentation.dto.AuthResponse;
import com.chamisnails.nailsalon.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class that provides user authentication functionality, including login and user details loading.
 * Implements the UserDetailsService interface for Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private JwtUtils jwtUtils;  // Utility for handling JWT creation and validation

    @Autowired
    private PasswordEncoder passwordEncoder;  // Encoder for password verification

    @Autowired
    private IUserRepository userRepository;  // Repository to interact with the User data

    /**
     * Loads user details from the database based on the provided username.
     *
     * @param username the username of the user to load
     * @return the UserDetails object containing user information and authorities
     * @throws UsernameNotFoundException if the user with the given username is not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserDocument userDocument = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user " + username + " does not exist"));

        // Map roles to GrantedAuthority objects
        Collection<? extends GrantedAuthority> authorities = userDocument.getRol().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());

        // Return User object (from Spring Security) with user credentials and authorities
        return new User(userDocument.getUsername(),
                userDocument.getPassword(),
                true, true, true, true, authorities);
    }

    /**
     * Logs in the user and returns an AuthResponse containing JWT token and user roles.
     *
     * @param authLoginRequest the login request containing username and password
     * @return an AuthResponse object with login details
     */
    public AuthResponse loginUser(AuthLoginRequest authLoginRequest) {

        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        // Authenticate the user
        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create JWT token
        String accessToken = jwtUtils.createToken(authentication);

        // Get roles from the authentication object
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        // Return authentication response
        return new AuthResponse(username, "User logged successfully", accessToken, true, roles);
    }

    /**
     * Authenticates the user by comparing the provided credentials with the stored data.
     *
     * @param username the username to authenticate
     * @param password the password to authenticate
     * @return an Authentication object containing authenticated user details
     * @throws BadCredentialsException if the credentials are invalid
     */
    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        // Check if the user exists and if the password matches
        if (userDetails == null || !passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        // Return the authentication token
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
}
