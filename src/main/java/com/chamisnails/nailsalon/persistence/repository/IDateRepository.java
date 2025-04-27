package com.chamisnails.nailsalon.persistence.repository;

import com.chamisnails.nailsalon.persistence.model.DateDocument;
import com.chamisnails.nailsalon.persistence.model.EState;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDateRepository extends MongoRepository<DateDocument, String> {

    List<DateDocument> findByState(EState state);

    List<DateDocument> findByDate(LocalDate date);

    List<DateDocument> findByDateAndState(LocalDate date, EState state);

    Optional<DateDocument> findById(String id);


}
