package com.chamisnails.nailsalon.persistence.repository;

import com.chamisnails.nailsalon.persistence.model.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface IUserRepository extends MongoRepository<UserDocument, String> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<UserDocument> findByUsername(String username);

    void deleteByUsername(String username);
}

