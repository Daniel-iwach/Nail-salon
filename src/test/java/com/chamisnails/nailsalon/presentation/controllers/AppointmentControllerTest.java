package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.exceptions.GlobalExceptionHandler;
import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.presentation.dto.AppointmentRequestDTO;
import com.chamisnails.nailsalon.presentation.dto.AppointmentResponseDTO;
import com.chamisnails.nailsalon.services.interfaces.IAppointmentService;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(GlobalExceptionHandler.class)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private IAppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appointmentController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void getAppointments_shouldReturnAppointments() throws Exception {
        AppointmentResponseDTO appointment = new AppointmentResponseDTO();
        when(appointmentService.getAppointments()).thenReturn(List.of(appointment));

        mockMvc.perform(get("/appointment/getAppointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createAppointment_shouldReturnCreatedMessage() throws Exception {
        AppointmentRequestDTO requestDTO = new AppointmentRequestDTO("date123", List.of("service1"));
        when(appointmentService.bookAnAppointment("date123", List.of("service1"))).thenReturn("appointment saved successfully. ID: 123");

        mockMvc.perform(post("/appointment/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void cancelAppointment_shouldReturnSuccessMessage() throws Exception {
        when(appointmentService.cancelAppointment("123")).thenReturn("appointment canceled");

        mockMvc.perform(put("/appointment/cancel/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByUser_shouldReturnUserAppointments() throws Exception {
        AppointmentResponseDTO responseDTO = new AppointmentResponseDTO();
        when(appointmentService.findAppointmentsByUserId("user123")).thenReturn(List.of(responseDTO));

        mockMvc.perform(get("/appointment/get/user123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAppointmentByState_shouldReturnFilteredAppointments() throws Exception {
        when(appointmentService.findAppointmentsByState(EState.BUSY)).thenReturn(List.of());

        mockMvc.perform(get("/appointment/getByState")
                        .param("state", "BUSY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getByDate_shouldReturnAppointmentsOnDate() throws Exception {
        LocalDate date = LocalDate.of(2025, 4, 23);
        when(appointmentService.findAppointmentByDate(date)).thenReturn(List.of());

        mockMvc.perform(get("/appointment/getByDate/{date}", date)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getAllAppointments_shouldReturnAllAppointments() throws Exception {
        when(appointmentService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/appointment/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}

