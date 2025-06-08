package com.glennon.storageHeater.infrastructure.models;

import java.util.ArrayList;
import java.util.List;

public class AuthorityRoles {

    private List<String> roles = new ArrayList<>();

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
