package com.glennon.storageHeater.presentation.api;

import jakarta.validation.constraints.NotNull;

/**
 * Created by Gerald on 03/03/2017.
 */
public class UserCredentials {

    @NotNull
    private String username;

    @NotNull
    private String password;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
