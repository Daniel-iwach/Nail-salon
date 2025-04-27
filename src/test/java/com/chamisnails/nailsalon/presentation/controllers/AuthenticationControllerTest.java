package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.exceptions.GlobalExceptionHandler;

import com.chamisnails.nailsalon.presentation.dto.AuthLoginRequest;
import com.chamisnails.nailsalon.presentation.dto.AuthResponse;
import com.chamisnails.nailsalon.presentation.dto.UserDTO;
import com.chamisnails.nailsalon.services.impl.UserDetailsServiceImpl;
import com.chamisnails.nailsalon.services.interfaces.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void login_shouldReturnAuthResponse() throws Exception {
        AuthLoginRequest loginRequest = new AuthLoginRequest("testUser","password");

        AuthResponse authResponse = new AuthResponse("testUser","message","jwt",true, List.of("USER"));

        when(userDetailsService.loginUser(loginRequest)).thenReturn(authResponse);

        mockMvc.perform(post("/log-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }


    @Test
    void registerUser_shouldReturnCreatedUser() throws Exception {
        UserDTO userDTO = new UserDTO("10","Test User","test","user",
                "test@example.com","password123",null,null);

        when(userService.save(userDTO)).thenReturn(userDTO);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }
}

