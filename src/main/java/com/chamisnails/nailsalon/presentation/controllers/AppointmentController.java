package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.presentation.dto.AppointmentRequestDTO;
import com.chamisnails.nailsalon.services.interfaces.IAppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Controller class to handle appointment-related operations in the system.
 * Exposes endpoints for CRUD operations related to appointments.
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    IAppointmentService appointmentService;

    /**
     * Get all appointments.
     *
     * Accessible to users with ADMIN or USER roles.
     *
     * @return List of all appointments with an HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get all appointments",
            description = "Fetches all appointments from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all appointments retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getAppointments")
    public ResponseEntity<?> getAppointments() {
        return new ResponseEntity<>(appointmentService.getAppointments(), HttpStatus.OK);
    }

    /**
     * Book a new appointment.
     *
     * Accessible to users with ADMIN or USER roles.
     *
     * @param requestDTO The details of the appointment request.
     * @return The created appointment with an HTTP status 201 (Created).
     */
    @Operation(
            summary = "Book a new appointment",
            description = "Creates a new appointment with the provided details."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Appointment successfully booked"),
            @ApiResponse(responseCode = "400", description = "Invalid appointment details")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/book")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequestDTO requestDTO) {
        return new ResponseEntity<>(appointmentService.bookAnAppointment(
                requestDTO.dateId(), requestDTO.serviceList()), HttpStatus.CREATED);
    }

    /**
     * Cancel an existing appointment.
     *
     * Accessible to users with ADMIN or USER roles.
     *
     * @param id The ID of the appointment to be canceled.
     * @return The result of the cancellation with an HTTP status 200 (OK).
     */
    @Operation(
            summary = "Cancel an appointment",
            description = "Cancels an appointment using its unique ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointment successfully canceled"),
            @ApiResponse(responseCode = "404", description = "Appointment not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping("cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable String id) {
        return new ResponseEntity<>(appointmentService.cancelAppointment(id), HttpStatus.OK);
    }

    /**
     * Get appointments by a specific user ID.
     *
     * Accessible to users with ADMIN or USER roles.
     *
     * @param userId The ID of the user whose appointments are to be fetched.
     * @return A list of appointments for the specified user with an HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get appointments by user",
            description = "Fetches all appointments for a specific user by their user ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments for the user retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getByUser(@PathVariable String userId) {
        return new ResponseEntity<>(appointmentService.findAppointmentsByUserId(userId), HttpStatus.OK);
    }

    /**
     * Get appointments by their current state (e.g., pending, completed).
     *
     * Accessible only to users with ADMIN role.
     *
     * @param state The state of the appointments to filter by.
     * @return A list of appointments with the specified state with an HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get appointments by state",
            description = "Fetches appointments based on their state (e.g., pending, completed)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments by state retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid state provided")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByState")
    public ResponseEntity<?> getAppointmentByState(@RequestParam("state") EState state) {
        return new ResponseEntity<>(appointmentService.findAppointmentsByState(state), HttpStatus.OK);
    }

    /**
     * Get appointments by a specific date.
     *
     * Accessible to users with ADMIN or USER roles.
     *
     * @param date The date of the appointments to fetch.
     * @return A list of appointments on the specified date with an HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get appointments by date",
            description = "Fetches all appointments scheduled for a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments for the date retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No appointments found for the given date")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getByDate/{date}")
    public ResponseEntity<?> getByDate(@PathVariable LocalDate date) {
        return new ResponseEntity<>(appointmentService.findAppointmentByDate(date), HttpStatus.OK);
    }

    /**
     * Get all appointments in the system.
     *
     * Accessible only to users with ADMIN role.
     *
     * @return A list of all appointments with an HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get all appointments",
            description = "Fetches all appointments from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all appointments retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.getAll(), HttpStatus.OK);
    }
}
