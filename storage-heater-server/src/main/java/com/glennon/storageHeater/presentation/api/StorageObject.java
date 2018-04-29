package com.glennon.storageHeater.presentation.api;

import javax.validation.constraints.NotNull;

/**
 * Created by Gerald on 03/06/2017.
 */
public class StorageObject {

    private String id;

    @NotNull
    private String name;

    private String description;

    private String author;

    private Object properties;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
