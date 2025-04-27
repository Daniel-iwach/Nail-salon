package com.chamisnails.nailsalon.persistence.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "appointments")
public class AppointmentDocument {

    @Id
    private String id;
    @NotBlank
    private String userId;
    @NotBlank
    private String dateId;
    @NotBlank
    private EState state;
    @NotEmpty
    private List<String> serviceIds;
}
