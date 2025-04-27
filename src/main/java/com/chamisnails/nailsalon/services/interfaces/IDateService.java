package com.chamisnails.nailsalon.services.interfaces;

import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.presentation.dto.DateDTO;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Set;

public interface IDateService {

    void loadDatesOfTheMonth(YearMonth month);

    List<DateDTO> getAllDates();

    List<DateDTO> getDatesByState(EState state);

    List<DateDTO>getByDate(LocalDate date);

    List<DateDTO> getHoursByDate(LocalDate date);

    String changeStatusByDate(LocalDate date, EState state);

    String changeStatusById(String idDate, EState state);

    DateDTO getDate(String id);

    Set<String>getDaysAvailable();
}
