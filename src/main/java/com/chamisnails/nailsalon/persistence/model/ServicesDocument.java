package com.chamisnails.nailsalon.persistence.model;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "services")
public class ServicesDocument {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private int price;
}

