package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.presentation.dto.UserDTO;
import com.chamisnails.nailsalon.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    IUserService userService;

    /**
     * Get user profile by username.
     *
     * Accessible to both ADMIN and USER roles.
     *
     * @param username The username of the user.
     * @return The user profile corresponding to the provided username.
     */
    @Operation(
            summary = "Get user profile by username",
            description = "Fetches the user profile based on the provided username."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get-by-username/{username}")
    public ResponseEntity<UserDTO> getUserProfile(@PathVariable String username) {
        return new ResponseEntity<>(userService.getByUsername(username), HttpStatus.OK);
    }

    /**
     * Get user profile by user ID.
     *
     * Accessible to both ADMIN and USER roles.
     *
     * @param userId The user ID of the user.
     * @return The user profile corresponding to the provided user ID.
     */
    @Operation(
            summary = "Get user profile by ID",
            description = "Fetches the user profile based on the provided user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get-by-id/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    /**
     * Get all users (Admin only).
     *
     * Accessible only to ADMIN role.
     *
     * @return A list of all users.
     */
    @Operation(
            summary = "Get all users",
            description = "Fetches a list of all users. Accessible only to ADMIN."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllUser() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }

    /**
     * Get the current user's ID.
     *
     * Accessible to both ADMIN and USER roles.
     *
     * @return The current user's ID.
     */
    @Operation(
            summary = "Get current user ID",
            description = "Fetches the current user's ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User ID retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getId")
    public ResponseEntity<?> getUserID() {
        return new ResponseEntity<>(userService.getUserId(), HttpStatus.OK);
    }
}
