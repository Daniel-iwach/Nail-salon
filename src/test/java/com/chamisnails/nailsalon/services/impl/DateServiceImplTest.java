package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.ResourceNotFoundException;
import com.chamisnails.nailsalon.persistence.model.DateDocument;
import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.persistence.model.ETime;
import com.chamisnails.nailsalon.persistence.repository.IDateRepository;
import com.chamisnails.nailsalon.presentation.dto.DateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DateServiceImplTest {

    @Mock
    private IDateRepository dateRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DateServiceImpl dateService;

    @Test
    void testLoadDatesOfTheMonth_SavesWeekdaysOnly() {
        YearMonth month = YearMonth.of(2025, 4);
        dateService.loadDatesOfTheMonth(month);

        long weekdays = month.atDay(1).datesUntil(month.atEndOfMonth().plusDays(1))
                .filter(d -> !(d.getDayOfWeek().toString().equals("SATURDAY") || d.getDayOfWeek().toString().equals("SUNDAY")))
                .count();

        verify(dateRepository, times((int) (weekdays * ETime.values().length))).save(any(DateDocument.class));
    }

    @Test
    void testGetAllDates_ShouldReturnMappedList() {
        DateDocument doc = new DateDocument();
        DateDTO dto = new DateDTO();
        when(dateRepository.findAll()).thenReturn(List.of(doc));
        when(modelMapper.map(doc, DateDTO.class)).thenReturn(dto);

        List<DateDTO> result = dateService.getAllDates();
        assertEquals(1, result.size());
        verify(dateRepository).findAll();
    }

    @Test
    void testGetDatesByState_ReturnsListOfDateDTO() {
        DateDocument doc = new DateDocument();
        DateDTO dto = new DateDTO();
        when(dateRepository.findByState(EState.AVAILABLE)).thenReturn(List.of(doc));
        when(modelMapper.map(doc, DateDTO.class)).thenReturn(dto);

        List<DateDTO> result = dateService.getDatesByState(EState.AVAILABLE);
        assertEquals(1, result.size());
    }

    @Test
    void testChangeStatusByDate_ThrowsExceptionIfNoDatesFound() {
        LocalDate date = LocalDate.of(2025, 4, 20);
        when(dateRepository.findByDate(date)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> dateService.changeStatusByDate(date, EState.BUSY));
    }

    @Test
    void testChangeStatusByDate_UpdatesEachDateDocument() {
        LocalDate date = LocalDate.of(2025, 4, 22);
        DateDocument doc1 = new DateDocument();
        DateDocument doc2 = new DateDocument();
        List<DateDocument> dateList = List.of(doc1, doc2);

        when(dateRepository.findByDate(date)).thenReturn(dateList);

        String result = dateService.changeStatusByDate(date, EState.BUSY);

        assertEquals("dates status updated", result);
        verify(dateRepository, times(2)).save(any(DateDocument.class));
        assertEquals(EState.BUSY, doc1.getState());
        assertEquals(EState.BUSY, doc2.getState());
    }

    @Test
    void testChangeStatusByDate_ThrowsExceptionIfEmptyList() {
        LocalDate date = LocalDate.of(2025, 4, 23);
        when(dateRepository.findByDate(date)).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class,
                () -> dateService.changeStatusByDate(date, EState.BUSY));
    }


    @Test
    void testChangeStatusById_UpdatesStatus() {
        String id = "abc123";
        DateDocument doc = new DateDocument();
        when(dateRepository.findById(id)).thenReturn(Optional.of(doc));

        String result = dateService.changeStatusById(id, EState.BUSY);
        assertEquals("dates status updated", result);
        verify(dateRepository).save(doc);
    }

    @Test
    void testChangeStatusById_ThrowsExceptionIfNotFound() {
        String id = "notExist";
        when(dateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> dateService.changeStatusById(id, EState.BUSY));
    }

    @Test
    void testGetHoursByDate_ReturnsAvailableHoursForDate() {
        LocalDate date = LocalDate.of(2025, 4, 20);
        DateDocument doc1 = new DateDocument();
        doc1.setDate(date);
        doc1.setStartTime(LocalTime.of(10, 0));
        doc1.setEndTime(LocalTime.of(12, 0));
        doc1.setState(EState.AVAILABLE);

        DateDTO dto1 = new DateDTO();
        dto1.setDate("2025-04-20");
        dto1.setStartTime("10:00");
        dto1.setEndTime("12:00");

        when(dateRepository.findByDateAndState(date, EState.AVAILABLE)).thenReturn(List.of(doc1));
        when(modelMapper.map(doc1, DateDTO.class)).thenReturn(dto1);

        List<DateDTO> result = dateService.getHoursByDate(date);

        assertEquals(1, result.size());
        assertEquals("2025-04-20", result.get(0).getDate());
        assertEquals("10:00", result.get(0).getStartTime());
    }


    @Test
    void testGetDate_ShouldReturnDateDTO() {
        String id = "abc123";
        DateDocument doc = new DateDocument();
        DateDTO dto = new DateDTO();
        when(dateRepository.findById(id)).thenReturn(Optional.of(doc));
        when(modelMapper.map(doc, DateDTO.class)).thenReturn(dto);

        DateDTO result = dateService.getDate(id);
        assertEquals(dto, result);
    }

    @Test
    void testGetDate_ThrowsExceptionIfNotFound() {
        String id = "nonExistentId";
        when(dateRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> dateService.getDate(id));
    }


    @Test
    void testGetDaysAvailable_ReturnsSortedUniqueDays() {
        // Simula objetos del repositorio
        DateDocument doc1 = new DateDocument();
        doc1.setDate(LocalDate.of(2025, 4, 10));
        doc1.setState(EState.AVAILABLE);

        DateDocument doc2 = new DateDocument();
        doc2.setDate(LocalDate.of(2025, 4, 8));
        doc2.setState(EState.AVAILABLE);

        // Simula los DTO que devuelve el modelMapper
        DateDTO dto1 = new DateDTO();
        dto1.setDate("2025-04-10");

        DateDTO dto2 = new DateDTO();
        dto2.setDate("2025-04-08");

        when(dateRepository.findByState(EState.AVAILABLE)).thenReturn(List.of(doc1, doc2));
        when(modelMapper.map(doc1, DateDTO.class)).thenReturn(dto1);
        when(modelMapper.map(doc2, DateDTO.class)).thenReturn(dto2);

        // Ejecuta el método real
        Set<String> result = dateService.getDaysAvailable();

        assertEquals(new LinkedHashSet<>(List.of("2025-04-08", "2025-04-10")), result);
    }

}
