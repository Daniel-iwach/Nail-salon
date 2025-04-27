package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.UserNotFoundException;
import com.chamisnails.nailsalon.persistence.model.ERol;
import com.chamisnails.nailsalon.persistence.model.UserDocument;
import com.chamisnails.nailsalon.persistence.repository.IUserRepository;
import com.chamisnails.nailsalon.presentation.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.context.SecurityContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void save_ValidUser_ReturnsSavedUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        UserDocument userDocument = new UserDocument();
        userDocument.setUsername("testuser");
        userDocument.setEmail("test@example.com");
        userDocument.setPassword("encoded");
        userDocument.setRol(Set.of(ERol.USER));

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(modelMapper.map(userDTO, UserDocument.class)).thenReturn(userDocument);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(UserDocument.class))).thenReturn(userDocument);
        when(modelMapper.map(userDocument, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.save(userDTO);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void testSave_ThrowsExceptionWhenEmailAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.save(userDTO)
        );

        assertEquals("Email is already in use.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testSave_ThrowsExceptionWhenUsernameAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                userService.save(userDTO)
        );

        assertEquals("Username is already in use.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }



    @Test
    void getByUsername_ExistingUser_ReturnsUserDTO() {
        String username = "user1";
        UserDocument userDocument = new UserDocument();
        userDocument.setUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userDocument));
        when(modelMapper.map(userDocument, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
    }

    @Test
    void getByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getByUsername("missing"));
    }

    @Test
    void getUserById_ExistingId_ReturnsUserDTO() {
        String id = "abc123";
        UserDocument userDocument = new UserDocument();
        userDocument.setId(id);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(userDocument));
        when(modelMapper.map(userDocument, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void getUserById_NotFound_ThrowsException() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.getUserById("missing"));
    }

    @Test
    void getUserId_ReturnsUserId() {
        String username = "testuser";
        String id = "user123";

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        UserDocument userDocument = new UserDocument();
        userDocument.setId(id);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userDocument));

        String result = userService.getUserId();

        assertEquals(id, result);
    }

    @Test
    void getAll_ReturnsListOfUserDTO() {
        List<UserDocument> documents = List.of(new UserDocument(), new UserDocument());
        List<UserDTO> dtos = List.of(new UserDTO(), new UserDTO());

        when(userRepository.findAll()).thenReturn(documents);
        when(modelMapper.map(any(UserDocument.class), eq(UserDTO.class)))
                .thenReturn(dtos.get(0), dtos.get(1));

        List<UserDTO> result = userService.getAll();

        assertEquals(2, result.size());
    }
}
