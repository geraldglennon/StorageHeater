package com.glennon.storageHeater.presentation.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Created by Gerald on 03/03/2018.
 */
public class UserSession {

    @Id
    private String id;

    private String username;

    private String sessionId;

    private Date createAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
