package com.chamisnails.nailsalon.util.mappers;

import com.chamisnails.nailsalon.presentation.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AppointmentAssembler {

    /**
     * Converts an AppointmentDTO along with associated UserDTO, DateDTO, and ServiceDTOs into an AppointmentResponseDTO.
     * This method assembles the response data for an appointment including user, date, time, state, and services.
     *
     * @param appointmentDTO the appointment data transfer object containing appointment information
     * @param userDTO the user data transfer object containing user information
     * @param dateDTO the date data transfer object containing appointment date and time information
     * @param serviceList a list of services related to the appointment
     * @return an AppointmentResponseDTO containing the final response data to be sent to the client
     */
    public AppointmentResponseDTO toResponse(AppointmentDTO appointmentDTO,
                                             UserDTO userDTO,
                                             DateDTO dateDTO,
                                             List<ServicesDTO> serviceList) {
        // Extract the username of the user
        String username = userDTO.getUsername();

        // Format the date and time from DateDTO
        String date = dateDTO.getDate().toString();
        String time = dateDTO.getStartTime().toString();

        // Extract the names of the services from the service list
        List<String> serviceNames = serviceList.stream()
                .map(s -> s.getName())  // Extract the name of each service
                .toList();

        // Create a new AppointmentResponseDTO and populate it with the data
        AppointmentResponseDTO response = new AppointmentResponseDTO();
        response.setId(appointmentDTO.getId());  // Set the appointment ID
        response.setUsername(username);  // Set the username of the user
        response.setDate(date);  // Set the formatted date
        response.setTime(time);  // Set the formatted time
        response.setState(appointmentDTO.getState());  // Set the state of the appointment
        response.setServices(serviceNames);  // Set the list of service names

        return response;  // Return the populated AppointmentResponseDTO
    }
}
