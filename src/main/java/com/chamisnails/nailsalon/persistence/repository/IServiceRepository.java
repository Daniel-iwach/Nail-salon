package com.chamisnails.nailsalon.persistence.repository;


import com.chamisnails.nailsalon.persistence.model.ServicesDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface IServiceRepository extends MongoRepository<ServicesDocument, String> {

    Optional<ServicesDocument> findById(String id);
}

