package com.chamisnails.nailsalon.persistence.repository;

import com.chamisnails.nailsalon.persistence.model.AppointmentDocument;
import com.chamisnails.nailsalon.persistence.model.EState;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IAppointmentRepository extends MongoRepository<AppointmentDocument, String> {

    List<AppointmentDocument> findByUserId(String username);

    List<AppointmentDocument> findByState(EState state);

    Optional<AppointmentDocument> findById(String id);

}
