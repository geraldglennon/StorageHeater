package com.glennon.storageHeater.presentation.model;

import org.springframework.data.annotation.Id;

/**
 * Created by Gerald on 13/07/2017.
 */
public class Label {

    @Id
    private String id;

    private String version;

    private String parentId;

    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
