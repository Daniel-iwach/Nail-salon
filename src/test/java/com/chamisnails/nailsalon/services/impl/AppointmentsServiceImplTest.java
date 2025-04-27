package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.InvalidRequestException;
import com.chamisnails.nailsalon.exceptions.OperationFailedException;
import com.chamisnails.nailsalon.exceptions.ResourceNotFoundException;
import com.chamisnails.nailsalon.persistence.model.AppointmentDocument;
import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.persistence.repository.IAppointmentRepository;
import com.chamisnails.nailsalon.presentation.dto.AppointmentDTO;
import com.chamisnails.nailsalon.presentation.dto.AppointmentResponseDTO;
import com.chamisnails.nailsalon.presentation.dto.DateDTO;
import com.chamisnails.nailsalon.presentation.dto.UserDTO;
import com.chamisnails.nailsalon.services.interfaces.IDateService;
import com.chamisnails.nailsalon.services.interfaces.IServicesService;
import com.chamisnails.nailsalon.services.interfaces.IUserService;
import com.chamisnails.nailsalon.util.mappers.AppointmentAssembler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentsServiceImplTest {

    @Mock
    private IAppointmentRepository appointmentRepository;

    @Mock
    private IDateService dateService;

    @Mock
    private IServicesService servicesService;

    @Mock
    private IUserService userService;

    @Mock
    private AppointmentAssembler appointmentAssembler;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private AppointmentsServiceImpl service;



    @Test
    void bookAnAppointment_WhenDateIsAvailable_ShouldSaveAppointment() {
        String userId = "user123";
        String dateId = "date456";
        List<String> services = List.of("service1", "service2");

        DateDTO dateDTO = new DateDTO();
        dateDTO.setId(dateId);
        dateDTO.setDate("2025-04-22");
        dateDTO.setStartTime("10:00");
        dateDTO.setEndTime("11:00");
        dateDTO.setState(EState.AVAILABLE);

        AppointmentDocument savedAppointment = new AppointmentDocument("appt789", userId, dateId, EState.BUSY, services);

        when(userService.getUserId()).thenReturn(userId);
        when(dateService.getDate(dateId)).thenReturn(dateDTO);
        when(appointmentRepository.save(any(AppointmentDocument.class))).thenReturn(savedAppointment);

        String result = service.bookAnAppointment(dateId, services);

        assertTrue(result.contains("appointment saved successfully"));
        verify(appointmentRepository).save(any(AppointmentDocument.class));
        verify(dateService).changeStatusById(dateId, EState.BUSY);
    }

    @Test
    void bookAnAppointment_WhenDateNotAvailable_ShouldThrowException() {
        String dateId = "date789";
        List<String> services = List.of("serviceX");

        DateDTO dateDTO = new DateDTO();
        dateDTO.setId(dateId);
        dateDTO.setDate("2025-04-22");
        dateDTO.setState(EState.BUSY);

        when(dateService.getDate(dateId)).thenReturn(dateDTO);

        assertThrows(InvalidRequestException.class, () -> service.bookAnAppointment(dateId, services));
    }

    @Test
    void bookAnAppointment_WhenAppointmentNotSaved_ShouldThrowException() {
        String userId = "user999";
        String dateId = "date000";
        List<String> services = List.of("serviceZ");

        DateDTO dateDTO = new DateDTO();
        dateDTO.setId(dateId);
        dateDTO.setDate("2025-04-22");
        dateDTO.setState(EState.AVAILABLE);

        AppointmentDocument unsaved = new AppointmentDocument(null, userId, dateId, EState.BUSY, services);

        when(userService.getUserId()).thenReturn(userId);
        when(dateService.getDate(dateId)).thenReturn(dateDTO);
        when(appointmentRepository.save(any(AppointmentDocument.class))).thenReturn(unsaved);

        assertThrows(OperationFailedException.class, () -> service.bookAnAppointment(dateId, services));
    }

    @Test
    void getAll_ShouldReturnListOfAppointmentResponseDTO() {

        AppointmentDocument doc1 = new AppointmentDocument("1", "user1", "1234", EState.BUSY, List.of("s1"));
        AppointmentDocument doc2 = new AppointmentDocument("2", "user2", "1235", EState.BUSY, List.of("s1"));
        AppointmentDocument doc3 = new AppointmentDocument("3", "user3", "1236", EState.BUSY, List.of("s1"));

        AppointmentDTO dto1 = new AppointmentDTO();
        dto1.setId("1");
        dto1.setUserId("user1");
        dto1.setDateId("1234");
        dto1.setServiceIds(List.of("s1"));

        AppointmentDTO dto2 = new AppointmentDTO();
        dto2.setId("2");
        dto2.setUserId("user2");
        dto2.setDateId("1235");
        dto2.setServiceIds(List.of("s1"));

        AppointmentDTO dto3 = new AppointmentDTO();
        dto3.setId("3");
        dto3.setUserId("user3");
        dto3.setDateId("1236");
        dto3.setServiceIds(List.of("s1"));

        AppointmentResponseDTO response = new AppointmentResponseDTO();

        when(appointmentRepository.findAll()).thenReturn(List.of(doc1, doc2, doc3));

        when(modelMapper.map(eq(doc1), eq(AppointmentDTO.class))).thenReturn(dto1);
        when(modelMapper.map(eq(doc2), eq(AppointmentDTO.class))).thenReturn(dto2);
        when(modelMapper.map(eq(doc3), eq(AppointmentDTO.class))).thenReturn(dto3);

        when(userService.getUserById(any())).thenReturn(new UserDTO());
        when(dateService.getDate(any())).thenReturn(new DateDTO());
        when(servicesService.getSevices(any())).thenReturn(List.of());
        when(appointmentAssembler.toResponse(any(), any(), any(), any())).thenReturn(response);

        List<AppointmentResponseDTO> result = service.getAll();

        assertEquals(3, result.size());
    }

    @Test
    void getAppointments_ShouldReturnAppointmentsOfCurrentUser() {
        when(userService.getUserId()).thenReturn("user123");
        when(appointmentRepository.findByUserId("user123")).thenReturn(List.of());

        List<AppointmentResponseDTO> result = service.getAppointments();
        assertNotNull(result);
    }

    @Test
    void cancelAppointment_WhenExists_ShouldCancelAndReturnMessage() {
        AppointmentDocument appointment = new AppointmentDocument("id", "user", "date", EState.BUSY, List.of());

        when(appointmentRepository.findById("id")).thenReturn(Optional.of(appointment));
        when(appointmentRepository.save(any())).thenReturn(appointment);

        String result = service.cancelAppointment("id");

        assertEquals("appointment canceled", result);
        verify(dateService).changeStatusById("date", EState.AVAILABLE);
    }

    @Test
    void cancelAppointment_WhenNotFound_ShouldThrowException() {
        when(appointmentRepository.findById("notFound")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.cancelAppointment("notFound"));
    }

    @Test
    void findAppointmentsByState_ShouldReturnFilteredAppointments() {
        AppointmentDocument doc = new AppointmentDocument("1", "user", "date", EState.BUSY, List.of("s1"));
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId("1");
        dto.setUserId("user");
        dto.setDateId("date");
        dto.setServiceIds(List.of("s1"));

        AppointmentResponseDTO response = new AppointmentResponseDTO();

        when(appointmentRepository.findByState(EState.BUSY)).thenReturn(List.of(doc));
        when(modelMapper.map(doc, AppointmentDTO.class)).thenReturn(dto);
        when(userService.getUserById("user")).thenReturn(new UserDTO());
        when(dateService.getDate("date")).thenReturn(new DateDTO());
        when(servicesService.getSevices(List.of("s1"))).thenReturn(List.of());
        when(appointmentAssembler.toResponse(any(), any(), any(), any())).thenReturn(response);

        List<AppointmentResponseDTO> result = service.findAppointmentsByState(EState.BUSY);
        assertEquals(1, result.size());
    }


    @Test
    void findAppointmentByDate_ShouldFilterByDate() {
        AppointmentDocument doc = new AppointmentDocument("1", "user", "dateId", EState.BUSY, List.of("s1"));
        DateDTO dateDTO = new DateDTO();
        dateDTO.setDate("2025-04-23");

        AppointmentDTO dto = new AppointmentDTO();
        dto.setId("1");
        dto.setUserId("user");
        dto.setDateId("dateId");
        dto.setServiceIds(List.of("s1"));

        AppointmentResponseDTO response = new AppointmentResponseDTO();

        when(appointmentRepository.findAll()).thenReturn(List.of(doc));
        when(dateService.getDate("dateId")).thenReturn(dateDTO);
        when(modelMapper.map(doc, AppointmentDTO.class)).thenReturn(dto);
        when(userService.getUserById("user")).thenReturn(new UserDTO());
        when(servicesService.getSevices(List.of("s1"))).thenReturn(List.of());
        when(appointmentAssembler.toResponse(any(), any(), any(), any())).thenReturn(response);

        List<AppointmentResponseDTO> result = service.findAppointmentByDate(LocalDate.of(2025, 4, 23));
        assertEquals(1, result.size());
    }

}
