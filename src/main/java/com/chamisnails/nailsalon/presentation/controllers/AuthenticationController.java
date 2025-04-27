package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.presentation.dto.AuthLoginRequest;
import com.chamisnails.nailsalon.presentation.dto.AuthResponse;
import com.chamisnails.nailsalon.presentation.dto.UserDTO;
import com.chamisnails.nailsalon.services.impl.UserDetailsServiceImpl;
import com.chamisnails.nailsalon.services.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {


    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private IUserService userService;

    @Operation(
            summary = "User Login",
            description = "This endpoint allows users to log in by providing their credentials. If successful, the system returns a JWT token that can be used for authenticated requests."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully logged in",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials provided",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Unauthorized access: invalid username or password",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/log-in")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthLoginRequest userRequest) {
        return new ResponseEntity<>(this.userDetailService.loginUser(userRequest), HttpStatus.OK);
    }

    @Operation(
            summary = "Register a new user",
            description = "This endpoint allows the registration of a new user with their details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody UserDTO user) {
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }


}
