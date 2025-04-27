package com.chamisnails.nailsalon.presentation.controllers;


import com.chamisnails.nailsalon.exceptions.GlobalExceptionHandler;
import com.chamisnails.nailsalon.exceptions.UserNotFoundException;
import com.chamisnails.nailsalon.persistence.model.ERol;
import com.chamisnails.nailsalon.presentation.dto.UserDTO;
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

import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void getUserProfile_shouldReturnUser_whenUserExists() throws Exception {
        String username = "juan";
        UserDTO user = new UserDTO("1", "juan", "Juan", "Perez", "juan@example.com", "password", Set.of(ERol.USER), new Date());

        when(userService.getByUsername(username)).thenReturn(user);

        mockMvc.perform(get("/user/get-by-username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("juan"))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void getUserProfile_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        String username = "juan";

        given(userService.getByUsername(username))
                .willThrow(new UserNotFoundException("User with username: " + username + " not found."));


        mockMvc.perform(get("/user/get-by-username/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById_shouldReturnUser_whenUserExists() throws Exception {
        String userId = "1";
        UserDTO user = new UserDTO(userId, "juan", "Juan", "Perez", "juan@example.com", "password", Set.of(ERol.USER), new Date());

        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/user/get-by-id/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.username").value("juan"))
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Perez"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));
    }

    @Test
    void getUserById_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        String userId = "1";

        given(userService.getUserById(userId))
                .willThrow(new UserNotFoundException("User with ID: " + userId + " not found."));

        mockMvc.perform(get("/user/get-by-id/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_shouldReturnUsers_whenUsersExist() throws Exception {
        // Simulate a list of users
        UserDTO user1 = new UserDTO("1", "juan", "Juan", "Perez", "juan@example.com", "password", Set.of(ERol.USER), new Date());
        UserDTO user2 = new UserDTO("2", "maria", "Maria", "Lopez", "maria@example.com", "password", Set.of(ERol.USER), new Date());

        when(userService.getAll()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/user/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("juan"))
                .andExpect(jsonPath("$[0].firstName").value("Juan"))
                .andExpect(jsonPath("$[0].lastName").value("Perez"))
                .andExpect(jsonPath("$[1].username").value("maria"))
                .andExpect(jsonPath("$[1].firstName").value("Maria"))
                .andExpect(jsonPath("$[1].lastName").value("Lopez"));
    }

    @Test
    void getUserId_shouldReturnUserId_whenUserExists() throws Exception {
        String username = "juan";
        String userId = "1";

        when(userService.getUserId()).thenReturn(userId);

        mockMvc.perform(get("/user/getId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(userId));
    }
}
