package com.chamisnails.nailsalon.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class AppConfig {

    // Bean for ModelMapper, which helps in mapping one object to another.
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();

        // Converter from LocalDate to String
        Converter<LocalDate, String> localDateToString = new Converter<>() {
            @Override
            public String convert(MappingContext<LocalDate, String> context) {
                LocalDate source = context.getSource();
                return source != null ? source.toString() : null;
            }
        };

        // Converter from LocalTime to String
        Converter<LocalTime, String> localTimeToString = new Converter<>() {
            @Override
            public String convert(MappingContext<LocalTime, String> context) {
                LocalTime source = context.getSource();
                return source != null ? source.toString() : null;
            }
        };
        return mapper;
    }

    // Bean for ObjectMapper, which is used for JSON serialization/deserialization.
    // Configures the ObjectMapper to handle Java 8 time types (LocalDate, LocalTime).
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
}
