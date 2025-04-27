package com.chamisnails.nailsalon.presentation.dto;

import com.chamisnails.nailsalon.persistence.model.EState;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDTO {
    private String id;
    @NotBlank
    private String userId;
    @NotBlank
    private String dateId;
    @NotBlank
    private EState state;
    private List<String> serviceIds;
}
