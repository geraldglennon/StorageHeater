package com.glennon.storageHeater.presentation.repositories;

import com.glennon.storageHeater.presentation.model.UserSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by Gerald on 03/03/2018.
 */
public interface UserSessionRepository extends MongoRepository<UserSession, String> {

    @Query("{sessionId: ?0}")
    UserSession findBySessionId(String sessionId);
}
