package com.chamisnails.nailsalon.presentation.dto;

import com.chamisnails.nailsalon.persistence.model.EState;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class DateDTO {
    private String id;
    @NotBlank
    private String date;
    @NotBlank
    private String startTime;
    @NotBlank
    private String endTime;
    @NotBlank
    private EState state;
}
