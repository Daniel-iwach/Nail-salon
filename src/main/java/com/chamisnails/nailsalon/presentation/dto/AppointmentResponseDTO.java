package com.chamisnails.nailsalon.presentation.dto;

import com.chamisnails.nailsalon.persistence.model.EState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class AppointmentResponseDTO {
    @NotBlank
    private String id;
    @NotBlank
    private String username;
    @NotBlank
    private String date;
    @NotBlank
    private String time;
    @NotBlank
    private EState state;
    @NotEmpty
    private List<String> services;
}
