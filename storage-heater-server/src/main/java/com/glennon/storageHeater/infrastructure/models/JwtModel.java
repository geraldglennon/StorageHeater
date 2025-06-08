package com.glennon.storageHeater.infrastructure.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtModel {

    @JsonProperty("preferred_username")
    private String preferredUsername;

    @JsonProperty("resource_access")
    private AuthorityRoles resourceRoles = new AuthorityRoles();

    public String getPreferredUsername() {
        return preferredUsername;
    }

    public void setPreferredUsername(String preferredUsername) {
        this.preferredUsername = preferredUsername;
    }

    public AuthorityRoles getResourceRoles() {
        return resourceRoles;
    }

    public void setResourceRoles(AuthorityRoles resourceRoles) {
        this.resourceRoles = resourceRoles;
    }
}
