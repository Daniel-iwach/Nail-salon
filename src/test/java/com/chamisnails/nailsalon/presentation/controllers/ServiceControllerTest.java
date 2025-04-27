package com.chamisnails.nailsalon.presentation.controllers;



import com.chamisnails.nailsalon.exceptions.GlobalExceptionHandler;
import com.chamisnails.nailsalon.presentation.dto.ServicesDTO;
import com.chamisnails.nailsalon.services.interfaces.IServicesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IServicesService servicesService;

    @InjectMocks
    private ServiceController serviceController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(serviceController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getServices_shouldReturnListOfServices() throws Exception {
        ServicesDTO service1 = new ServicesDTO();
        ServicesDTO service2 = new ServicesDTO();
        when(servicesService.getAllServices()).thenReturn(List.of(service1, service2));

        mockMvc.perform(get("/getServices")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
