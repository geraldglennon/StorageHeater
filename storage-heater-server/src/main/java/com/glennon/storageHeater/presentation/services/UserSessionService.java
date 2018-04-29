package com.glennon.storageHeater.presentation.services;

import com.glennon.storageHeater.presentation.model.UserSession;
import com.glennon.storageHeater.presentation.repositories.UserSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserSessionService {

    @Autowired
    private UserSessionRepository userSessionRepository;

    public void createUserSession(String sessionId, String username) {
        UserSession userSession = new UserSession();
        userSession.setUsername(username);
        userSession.setSessionId(sessionId);
        userSession.setCreateAt(new Date());

        userSessionRepository.insert(userSession);
    }
}
