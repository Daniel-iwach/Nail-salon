
package com.chamisnails.nailsalon.presentation.controllers;

import com.chamisnails.nailsalon.persistence.model.EState;

import com.chamisnails.nailsalon.presentation.dto.DateDTO;
import com.chamisnails.nailsalon.services.interfaces.IDateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class DateControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IDateService dateService;

    @InjectMocks
    private DateController dateController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dateController).build();
    }

    @Test
    void loadMonth_shouldReturnCreated() throws Exception {
        String validMonth = "2023-05";

        mockMvc.perform(post("/date/loadMonth/{month}", validMonth)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(dateService, times(1)).loadDatesOfTheMonth(YearMonth.parse(validMonth));
    }

    @Test
    void loadMonth_withInvalidFormat_shouldReturnBadRequest() throws Exception {
        String invalidMonth = "May-2023";

        mockMvc.perform(post("/date/loadMonth/{month}", invalidMonth)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(dateService, never()).loadDatesOfTheMonth(any());
    }

    @Test
    void getDaysAvailable_shouldReturnOk() throws Exception {
        Set<String> availableDays = new HashSet<>(Arrays.asList("2023-05-01", "2023-05-02"));
        when(dateService.getDaysAvailable()).thenReturn(availableDays);

        mockMvc.perform(get("/date/getDaysAvailable")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getByState_shouldReturnOk() throws Exception {
        List<DateDTO> dates = Arrays.asList(
                createTestDateDTO("1", "2023-05-01", "09:00", "11:00", EState.AVAILABLE),
                createTestDateDTO("2", "2023-05-01", "11:00", "13:00", EState.AVAILABLE)
        );

        when(dateService.getDatesByState(EState.AVAILABLE)).thenReturn(dates);

        mockMvc.perform(get("/date/getByState")
                        .param("state", "AVAILABLE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getAll_shouldReturnOk() throws Exception {
        List<DateDTO> dates = Arrays.asList(
                createTestDateDTO("1", "2023-05-01", "09:00", "11:00", EState.AVAILABLE),
                createTestDateDTO("2", "2023-05-01", "11:00", "13:00", EState.BUSY)
        );

        when(dateService.getAllDates()).thenReturn(dates);

        mockMvc.perform(get("/date/getAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getHours_shouldReturnOk() throws Exception {
        List<DateDTO> hours = Arrays.asList(
                createTestDateDTO("1", "2023-05-01", "09:00", "11:00", EState.AVAILABLE),
                createTestDateDTO("2", "2023-05-01", "11:00", "13:00", EState.AVAILABLE)
        );

        LocalDate testDate = LocalDate.of(2023, 5, 1);
        when(dateService.getHoursByDate(testDate)).thenReturn(hours);

        mockMvc.perform(get("/date/getHours/{date}", "2023-05-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getByDate_shouldReturnOk() throws Exception {
        List<DateDTO> dates = Arrays.asList(
                createTestDateDTO("1", "2023-05-01", "09:00", "11:00", EState.AVAILABLE),
                createTestDateDTO("2", "2023-05-01", "11:00", "13:00", EState.BUSY)
        );

        LocalDate testDate = LocalDate.of(2023, 5, 1);
        when(dateService.getByDate(testDate)).thenReturn(dates);

        mockMvc.perform(get("/date/getByDate/{date}", "2023-05-01")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void changeStatusByDate_shouldReturnOk() throws Exception {
        String expectedResponse = "dates status updated";
        when(dateService.changeStatusByDate(any(), any())).thenReturn(expectedResponse);

        mockMvc.perform(put("/date/changeStatusByDate")
                        .param("date", "2023-05-01")
                        .param("state", "BUSY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    @Test
    void changeStatusById_shouldReturnOk() throws Exception {
        String expectedResponse = "dates status updated";
        when(dateService.changeStatusById(any(), any())).thenReturn(expectedResponse);

        mockMvc.perform(put("/date/changeStatusById")
                        .param("dateId", "12345")
                        .param("state", "BUSY")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));
    }

    private DateDTO createTestDateDTO(String id, String date, String startTime, String endTime, EState state) {
        DateDTO dto = new DateDTO();
        dto.setId(id);
        dto.setDate(date);
        dto.setStartTime(startTime);
        dto.setEndTime(endTime);
        dto.setState(state);
        return dto;
    }
}