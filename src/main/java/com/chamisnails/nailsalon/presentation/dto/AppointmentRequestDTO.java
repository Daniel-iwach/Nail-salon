package com.chamisnails.nailsalon.presentation.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;


import java.util.List;

public record AppointmentRequestDTO(
        @NotBlank
        String dateId,
        @NotEmpty
        List<String> serviceList
) {}

