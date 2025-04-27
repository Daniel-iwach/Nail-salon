package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.UserNotFoundException;
import com.chamisnails.nailsalon.persistence.model.ERol;
import com.chamisnails.nailsalon.persistence.model.UserDocument;
import com.chamisnails.nailsalon.persistence.repository.IUserRepository;
import com.chamisnails.nailsalon.presentation.dto.*;
import com.chamisnails.nailsalon.services.interfaces.IUserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserRepository userRepository;  // Repository for interacting with User data in the database

    @Autowired
    ModelMapper modelMapper;  // Mapper for converting between DTOs and entities

    @Autowired
    PasswordEncoder passwordEncoder;  // Encoder for hashing and checking passwords

    /**
     * Saves a new user to the database.
     * Checks if the provided email or username is already in use.
     * If not, it creates a new UserDocument, hashes the password, and saves it.
     *
     * @param user the user data transfer object (DTO) containing user information
     * @return the saved UserDTO
     * @throws IllegalArgumentException if email or username is already taken
     */
    @Override
    public UserDTO save(UserDTO user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email is already in use.");
        } else if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        UserDocument userDocument = modelMapper.map(user, UserDocument.class);
        userDocument.setPassword(passwordEncoder.encode(user.getPassword()));  // Hash the password
        userDocument.setRol(Set.of(ERol.USER));  // Assign the 'USER' role
        userDocument.setRegistrationDate(new Date());  // Set the registration date

        UserDocument userSaved = userRepository.save(userDocument);  // Save the user to the repository
        return modelMapper.map(userSaved, UserDTO.class);  // Convert and return the saved user as a DTO
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username the username of the user to retrieve
     * @return the UserDTO containing user information
     * @throws UserNotFoundException if the user with the given username does not exist
     */
    @Override
    public UserDTO getByUsername(String username) {
        UserDocument userDocument = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username: " + username + " not found."));
        return modelMapper.map(userDocument, UserDTO.class);
    }

    /**
     * Retrieves a user by their user ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the UserDTO containing user information
     * @throws UserNotFoundException if the user with the given ID does not exist
     */
    @Override
    public UserDTO getUserById(String userId) {
        UserDocument userDocument = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: " + userId + " not found."));
        return modelMapper.map(userDocument, UserDTO.class);
    }

    /**
     * Retrieves the ID of the currently authenticated user.
     *
     * @return the ID of the currently authenticated user
     * @throws UsernameNotFoundException if no user is found for the current authentication
     */
    @Override
    public String getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();  // Get the current username
        UserDocument userDocument = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for username: " + username));
        return userDocument.getId();  // Return the user ID
    }

    /**
     * Retrieves all users from the database.
     *
     * @return a list of UserDTOs representing all users
     */
    @Override
    public List<UserDTO> getAll() {
        List<UserDocument> list = userRepository.findAll();
        return list.stream()
                .map(user -> modelMapper.map(user, UserDTO.class))  // Convert each UserDocument to a UserDTO
                .toList();
    }
}
