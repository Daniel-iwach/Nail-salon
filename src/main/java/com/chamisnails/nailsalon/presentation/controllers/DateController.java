package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.services.interfaces.IDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/date")
public class DateController {

    @Autowired
    IDateService dateService;

    /**
     * Load the dates for a specific month.
     *
     * Accessible only to users with ADMIN role.
     *
     * @param month The month in YYYY-MM format to load dates for.
     * @return HTTP status indicating success or failure (400 for invalid month format, 201 for successful load).
     */
    @Operation(
            summary = "Load dates for a month",
            description = "This endpoint allows the admin to load dates for a specific month."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dates loaded successfully for the month"),
            @ApiResponse(responseCode = "400", description = "Invalid month format")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/loadMonth/{month}")
    public ResponseEntity<?> loadMonth(@PathVariable String month) {
        YearMonth yearMonth;
        try {
            yearMonth = YearMonth.parse(month);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().build();
        }
        dateService.loadDatesOfTheMonth(yearMonth);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Get available days for appointments.
     *
     * Accessible to both ADMIN and USER roles.
     *
     * @return A list of available days with HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get available days for appointments",
            description = "Fetches the days with available slots for appointments."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available days retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getDaysAvailable")
    public ResponseEntity<?> getDaysAvailable() {
        return new ResponseEntity<>(dateService.getDaysAvailable(), HttpStatus.OK);
    }

    /**
     * Get dates by their current state.
     *
     * Accessible only to users with ADMIN role.
     *
     * @param state The state of the dates to filter by (e.g., available, booked).
     * @return A list of dates with the specified state.
     */
    @Operation(
            summary = "Get dates by state",
            description = "Fetches dates that have a specific state (e.g., available, booked)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dates by state retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid state provided")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByState")
    public ResponseEntity<?> getByState(@RequestParam("state") EState state) {
        return new ResponseEntity<>(dateService.getDatesByState(state), HttpStatus.OK);
    }

    /**
     * Get all dates in the system.
     *
     * Accessible only to users with ADMIN role.
     *
     * @return A list of all dates with HTTP status 200 (OK).
     */
    @Operation(
            summary = "Get all dates",
            description = "Fetches all dates from the system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All dates retrieved successfully")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(dateService.getAllDates(), HttpStatus.OK);
    }

    /**
     * Get available hours for a specific date.
     *
     * Accessible to both ADMIN and USER roles.
     *
     * @param date The date to get available hours for.
     * @return A list of available hours for the specified date.
     */
    @Operation(
            summary = "Get available hours for a specific date",
            description = "Fetches the available hours for a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available hours for the date retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No available hours for the specified date")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/getHours/{date}")
    public ResponseEntity<?> getHours(@PathVariable LocalDate date) {
        return new ResponseEntity<>(dateService.getHoursByDate(date), HttpStatus.OK);
    }

    /**
     * Get all appointments for a specific date.
     *
     * Accessible only to users with ADMIN role.
     *
     * @param date The date to get appointments for.
     * @return A list of appointments for the specified date.
     */
    @Operation(
            summary = "Get appointments for a specific date",
            description = "Fetches all appointments for a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Appointments for the date retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No appointments found for the specified date")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getByDate/{date}")
    public ResponseEntity<?> getByDate(@PathVariable LocalDate date) {
        return new ResponseEntity<>(dateService.getByDate(date), HttpStatus.OK);
    }

    /**
     * Change the status of a date by providing the date and new state.
     *
     * Accessible only to users with ADMIN role.
     *
     * @param date The date whose status is to be changed.
     * @param state The new state to set for the date.
     * @return A message confirming the status change.
     */
    @Operation(
            summary = "Change the status of a date",
            description = "Updates the status of a specific date."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status of the date changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date or state provided")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/changeStatusByDate")
    public ResponseEntity<String> changeStatus(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("state") EState state) {
        return new ResponseEntity<>(dateService.changeStatusByDate(date, state), HttpStatus.OK);
    }

    /**
     * Change the status of a date by its ID.
     *
     * Accessible only to users with ADMIN role.
     *
     * @param dateId The ID of the date whose status is to be changed.
     * @param state The new state to set for the date.
     * @return A message confirming the status change.
     */
    @Operation(
            summary = "Change the status of a date by ID",
            description = "Updates the status of a specific date using its ID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status of the date changed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid date ID or state provided")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/changeStatusById")
    public ResponseEntity<?> changeStatusById(@RequestParam("dateId") String dateId,
                                              @RequestParam("state") EState state) {
        return new ResponseEntity<>(dateService.changeStatusById(dateId, state), HttpStatus.OK);
    }
}
