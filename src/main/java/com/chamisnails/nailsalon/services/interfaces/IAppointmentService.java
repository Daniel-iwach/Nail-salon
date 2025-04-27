package com.chamisnails.nailsalon.services.interfaces;


import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.presentation.dto.AppointmentResponseDTO;

import java.time.LocalDate;
import java.util.List;


public interface IAppointmentService {

    List<AppointmentResponseDTO>getAll();
    List<AppointmentResponseDTO> getAppointments();
    String bookAnAppointment(String dateId, List<String> serviceList);
    String cancelAppointment(String appointmentId);
    List<AppointmentResponseDTO>findAppointmentsByState(EState state);
    List<AppointmentResponseDTO>findAppointmentsByUserId(String userId);
    List<AppointmentResponseDTO>findAppointmentByDate(LocalDate date);
}
