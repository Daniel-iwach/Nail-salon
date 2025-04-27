package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.services.interfaces.IServicesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @Autowired
    IServicesService servicesService;

    /**
     * Get all services offered by the salon.
     *
     * Accessible to both ADMIN and USER roles.
     *
     * @return A list of all available services with HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get all services",
            description = "Fetches a list of all available services offered by the salon."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All services retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getServices")
    public ResponseEntity<?> getServices() {
        return new ResponseEntity<>(servicesService.getAllServices(), HttpStatus.OK);
    }
}
