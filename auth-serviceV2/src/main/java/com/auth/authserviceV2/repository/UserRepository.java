package com.auth.authserviceV2.repository;

import com.auth.authserviceV2.models.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
        Optional<User> findByUsername(String username);
}

