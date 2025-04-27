package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.InvalidRequestException;
import com.chamisnails.nailsalon.exceptions.OperationFailedException;
import com.chamisnails.nailsalon.exceptions.ResourceNotFoundException;
import com.chamisnails.nailsalon.persistence.model.*;
import com.chamisnails.nailsalon.persistence.repository.IAppointmentRepository;
import com.chamisnails.nailsalon.presentation.dto.*;
import com.chamisnails.nailsalon.services.interfaces.IAppointmentService;
import com.chamisnails.nailsalon.services.interfaces.IDateService;
import com.chamisnails.nailsalon.services.interfaces.IServicesService;
import com.chamisnails.nailsalon.services.interfaces.IUserService;
import com.chamisnails.nailsalon.util.mappers.AppointmentAssembler;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class AppointmentsServiceImpl implements IAppointmentService {

    @Autowired
    private IAppointmentRepository appointmentRepository;
    @Autowired
    private IDateService dateService;
    @Autowired
    private IServicesService servicesService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AppointmentAssembler appointmentAssembler;

    /**
     * Retrieve all appointments.
     *
     * @return A list of all appointments.
     */
    @Override
    public List<AppointmentResponseDTO> getAll() {
        List<AppointmentDocument> list = appointmentRepository.findAll();
        return list.stream()
                .map(appointmentDocument -> getResponseDTO(appointmentDocument))
                .toList();
    }

    /**
     * Retrieve appointments for the current user.
     *
     * @return A list of appointments for the current user.
     */
    @Override
    public List<AppointmentResponseDTO> getAppointments() {
        String userId = userService.getUserId();
        return findAppointmentsByUserId(userId);
    }

    /**
     * Helper method to convert AppointmentDTO to AppointmentResponseDTO.
     *
     * @param appointmentDTO The appointment data transfer object.
     * @return The corresponding AppointmentResponseDTO.
     */
    private AppointmentResponseDTO getResponseDTO(AppointmentDTO appointmentDTO) {
        UserDTO userDTO = userService.getUserById(appointmentDTO.getUserId());
        DateDTO dateDTO = dateService.getDate(appointmentDTO.getDateId());
        List<ServicesDTO> serviceList = servicesService.getSevices(appointmentDTO.getServiceIds());
        return appointmentAssembler.toResponse(appointmentDTO, userDTO, dateDTO, serviceList);
    }

    /**
     * Helper method to convert AppointmentDocument to AppointmentResponseDTO.
     *
     * @param appointmentDoc The appointment document from the repository.
     * @return The corresponding AppointmentResponseDTO.
     */
    private AppointmentResponseDTO getResponseDTO(AppointmentDocument appointmentDoc) {
        AppointmentDTO dto = modelMapper.map(appointmentDoc, AppointmentDTO.class);
        return getResponseDTO(dto);
    }

    /**
     * Book an appointment for the current user.
     *
     * @param dateId      The ID of the selected date.
     * @param serviceList A list of service IDs to be booked.
     * @return A confirmation message with the appointment ID.
     * @throws InvalidRequestException If the selected date is not available.
     * @throws OperationFailedException If the appointment cannot be saved.
     */
    @Override
    public String bookAnAppointment(String dateId, List<String> serviceList) {
        String userId = userService.getUserId();

        DateDTO date = dateService.getDate(dateId);
        if (date.getState() != EState.AVAILABLE) {
            throw new InvalidRequestException("The selected date is not available");
        }

        AppointmentDocument appointment = new AppointmentDocument(null, userId, dateId, EState.BUSY, serviceList);
        AppointmentDocument saved = appointmentRepository.save(appointment);

        if (saved.getId() == null) {
            throw new OperationFailedException("The appointment could not be saved");
        }

        dateService.changeStatusById(saved.getDateId(), EState.BUSY);
        return "Appointment saved successfully. ID: " + saved.getId();
    }

    /**
     * Cancel an existing appointment.
     *
     * @param appointmentId The ID of the appointment to be canceled.
     * @return A message indicating the appointment has been canceled.
     * @throws ResourceNotFoundException If the appointment does not exist.
     */
    @Override
    public String cancelAppointment(String appointmentId) {
        AppointmentDocument appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));
        appointment.setState(EState.CANCELED);
        dateService.changeStatusById(appointment.getDateId(), EState.AVAILABLE);
        appointmentRepository.save(appointment);
        return "Appointment canceled";
    }

    /**
     * Find appointments by their state.
     *
     * @param state The state of the appointments to retrieve.
     * @return A list of appointments with the specified state.
     */
    @Override
    public List<AppointmentResponseDTO> findAppointmentsByState(EState state) {
        List<AppointmentDocument> documentList = appointmentRepository.findByState(state);
        return documentList.stream()
                .map(appointmentDocument -> getResponseDTO(appointmentDocument))
                .toList();
    }

    /**
     * Find appointments for a specific user by user ID.
     *
     * @param userId The user ID whose appointments are to be retrieved.
     * @return A list of appointments for the specified user.
     */
    @Override
    public List<AppointmentResponseDTO> findAppointmentsByUserId(String userId) {
        List<AppointmentDocument> documentList = appointmentRepository.findByUserId(userId);
        return documentList.stream()
                .map(appointmentDocument -> getResponseDTO(appointmentDocument))
                .toList();
    }

    /**
     * Find appointments for a specific date.
     *
     * @param date The date for which appointments are to be retrieved.
     * @return A list of appointments on the specified date.
     */
    @Override
    public List<AppointmentResponseDTO> findAppointmentByDate(LocalDate date) {
        List<AppointmentDocument> appointmentList = appointmentRepository.findAll();
        List<AppointmentResponseDTO> dtoList = new ArrayList<>();

        appointmentList.forEach(appointment -> {
            DateDTO dateDTO = dateService.getDate(appointment.getDateId());
            if (dateDTO.getDate().equals(date.toString())) {
                AppointmentResponseDTO dto = getResponseDTO(appointment);
                dtoList.add(dto);
            }
        });
        return dtoList;
    }
}
