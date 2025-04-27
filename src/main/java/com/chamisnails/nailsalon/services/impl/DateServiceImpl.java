package com.chamisnails.nailsalon.services.impl;

import com.chamisnails.nailsalon.exceptions.ResourceNotFoundException;
import com.chamisnails.nailsalon.persistence.model.DateDocument;
import com.chamisnails.nailsalon.persistence.model.EState;
import com.chamisnails.nailsalon.persistence.model.ETime;
import com.chamisnails.nailsalon.persistence.repository.IDateRepository;
import com.chamisnails.nailsalon.presentation.dto.DateDTO;
import com.chamisnails.nailsalon.services.interfaces.IDateService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class to manage the dates and availability for appointments.
 * Provides methods to load, retrieve, update, and manage the dates in the system.
 */
@Service
public class DateServiceImpl implements IDateService {

    @Autowired
    private IDateRepository dateRespository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * Loads the available dates for a given month. Only weekdays (Monday to Friday)
     * are considered, and for each day, time slots are created for each value of ETime.
     *
     * @param month the month for which to load the dates
     */
    @Override
    public void loadDatesOfTheMonth(YearMonth month) {
        int totalDays = month.lengthOfMonth();

        // Iterates through each day of the month
        for (int i = 1; i <= totalDays; i++) {
            DayOfWeek day = month.atDay(i).getDayOfWeek();

            // Skips weekends (Saturday and Sunday)
            if (day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY) {
                LocalDate date = month.atDay(i);

                // Creates time slots for each ETime value
                for (ETime time : ETime.values()) {
                    LocalTime start = time.getHour();
                    LocalTime end = start.plusHours(2);

                    DateDocument dateDocument = DateDocument.builder()
                            .date(date)
                            .startTime(start)
                            .endTime(end)
                            .state(EState.AVAILABLE)
                            .build();

                    // Saves the date document to the repository
                    dateRespository.save(dateDocument);
                }
            }
        }
    }

    /**
     * Retrieves all available dates from the repository.
     *
     * @return a list of all date DTOs
     */
    @Override
    public List<DateDTO> getAllDates() {
        return dateRespository.findAll().stream()
                .map(dateDocument -> modelMapper.map(dateDocument, DateDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves dates based on their availability state (e.g., AVAILABLE, BUSY).
     *
     * @param state the state to filter the dates by
     * @return a list of date DTOs in the specified state
     */
    @Override
    public List<DateDTO> getDatesByState(EState state) {
        List<DateDTO> list = new ArrayList<>();
        List<DateDocument> documentList = dateRespository.findByState(state);
        list = documentList.stream()
                .map(d -> modelMapper.map(d, DateDTO.class))
                .toList();
        return list;
    }

    /**
     * Retrieves available hours for a specific date.
     *
     * @param date the date to find available time slots for
     * @return a list of available hours (DateDTOs) for the given date
     */
    @Override
    public List<DateDTO> getHoursByDate(LocalDate date) {
        List<DateDocument> documentList = dateRespository.findByDateAndState(date, EState.AVAILABLE);
        return documentList.stream()
                .map(dateDocument -> modelMapper.map(dateDocument, DateDTO.class))
                .toList();
    }

    /**
     * Retrieves all dates for a specific date.
     *
     * @param date the date to retrieve
     * @return a list of DateDTOs for the specified date
     */
    @Override
    public List<DateDTO> getByDate(LocalDate date) {
        List<DateDocument> documentList = dateRespository.findByDate(date);
        return documentList.stream()
                .map(dateDocument -> modelMapper.map(dateDocument, DateDTO.class))
                .toList();
    }

    /**
     * Changes the status of all dates for a specific date.
     *
     * @param date the date whose status will be updated
     * @param state the new state to apply to the dates
     * @return a message indicating the result of the operation
     */
    @Override
    public String changeStatusByDate(LocalDate date, EState state) {
        List<DateDocument> documentList = dateRespository.findByDate(date);
        if (documentList.isEmpty()) {
            throw new ResourceNotFoundException("No dates found for date: " + date);
        }
        for (DateDocument dateDocument : documentList) {
            dateDocument.setState(state);
            dateRespository.save(dateDocument);
        }
        return "dates status updated";
    }

    /**
     * Changes the status of a specific date by its ID.
     *
     * @param idDate the ID of the date to update
     * @param state the new state to apply
     * @return a message indicating the result of the operation
     */
    @Override
    public String changeStatusById(String idDate, EState state) {
        DateDocument dateDocument = dateRespository.findById(idDate)
                .orElseThrow(() -> new ResourceNotFoundException("Date not found for ID: " + idDate));
        dateDocument.setState(state);
        dateRespository.save(dateDocument);
        return "dates status updated";
    }

    /**
     * Retrieves a specific date by its ID.
     *
     * @param id the ID of the date to retrieve
     * @return the DateDTO for the specified date
     */
    @Override
    public DateDTO getDate(String id) {
        DateDocument dateDocument = dateRespository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Date not found for ID: " + id));
        return modelMapper.map(dateDocument, DateDTO.class);
    }

    /**
     * Retrieves all available days, sorted in ascending order.
     *
     * @return a set of available dates in string format
     */
    @Override
    public Set<String> getDaysAvailable() {
        List<DateDTO> dateList = getDatesByState(EState.AVAILABLE);
        Set<LocalDate> sortedDates = dateList.stream()
                .map(DateDTO::getDate)
                .map(date -> LocalDate.parse(date))
                .collect(Collectors.toCollection(TreeSet::new));

        return sortedDates.stream()
                .map(LocalDate::toString)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
