package com.chamisnails.nailsalon.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServicesDTO {
    private String id;
    @NotBlank
    private String name;
    @NotBlank
    private int price;
}
