package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.persistence.model.ERol;
import com.chamisnails.nailsalon.persistence.model.UserDocument;
import com.chamisnails.nailsalon.persistence.repository.IUserRepository;
import com.chamisnails.nailsalon.presentation.dto.AuthLoginRequest;
import com.chamisnails.nailsalon.presentation.dto.AuthResponse;
import com.chamisnails.nailsalon.security.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        UserDocument userDocument = new UserDocument();
        userDocument.setUsername("testUser");
        userDocument.setPassword("encodedPass");
        userDocument.setRol(Set.of(ERol.USER));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userDocument));

        UserDetails userDetails = userDetailsService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_ShouldThrowExceptionWhenNotFound() {
        when(userRepository.findByUsername("invalid")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("invalid"));
    }

    @Test
    void authenticate_ShouldReturnAuthenticationToken_WhenPasswordMatches() {
        UserDocument userDocument = new UserDocument();
        userDocument.setUsername("testUser");
        userDocument.setPassword("encodedPass");
        userDocument.setRol(Set.of(ERol.USER));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userDocument));
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);

        Authentication auth = userDetailsService.authenticate("testUser", "rawPass");

        assertEquals("testUser", auth.getPrincipal());
        assertEquals("rawPass", auth.getCredentials());
    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIncorrect() {
        UserDocument userDocument = new UserDocument();
        userDocument.setUsername("testUser");
        userDocument.setPassword("encodedPass");
        userDocument.setRol(Set.of(ERol.USER));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userDocument));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () ->
                userDetailsService.authenticate("testUser", "wrongPass"));
    }

    @Test
    void loginUser_ShouldReturnAuthResponse() {
        AuthLoginRequest request = new AuthLoginRequest("testUser", "rawPass");

        UserDocument userDocument = new UserDocument();
        userDocument.setUsername("testUser");
        userDocument.setPassword("encodedPass");
        userDocument.setRol(Set.of(ERol.ADMIN));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(userDocument));
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);
        when(jwtUtils.createToken(any())).thenReturn("jwt-token");

        AuthResponse response = userDetailsService.loginUser(request);

        assertEquals("testUser", response.username());
        assertEquals("jwt-token", response.jwt());
        assertTrue(response.roles().contains("ROLE_ADMIN"));
    }
}
