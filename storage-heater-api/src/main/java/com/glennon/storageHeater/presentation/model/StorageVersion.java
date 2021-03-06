package com.glennon.storageHeater.presentation.model;

import org.springframework.data.annotation.Id;

/**
 * Created by Gerald on 13/07/2017.
 */
public class StorageVersion {

    @Id
    private String id;

    private Object properties;

    private Version version = Version.INITIAL_VERSION;

    private String parentId;

    private boolean locked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
