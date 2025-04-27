package com.chamisnails.nailsalon.persistence.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "dates")
public class DateDocument {

    @Id
    private String id;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotBlank
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotBlank
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @NotBlank
    private EState state;
}





