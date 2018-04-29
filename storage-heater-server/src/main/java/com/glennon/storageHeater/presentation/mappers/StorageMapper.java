package com.glennon.storageHeater.presentation.mappers;

import com.glennon.storageHeater.presentation.api.StorageObject;
import com.glennon.storageHeater.presentation.model.StorageParent;
import com.glennon.storageHeater.presentation.model.StorageVersion;
import com.glennon.storageHeater.presentation.api.StorageVersionObject;

/**
 * Created by Gerald on 20/08/2017.
 */
public class StorageMapper {

    public static StorageParent mapParent (StorageObject storageObject) {
        if (storageObject == null) {
            return null;
        }

        StorageParent storageParent = new StorageParent();
        storageParent.setName(storageObject.getName());
        storageParent.setDescription(storageObject.getDescription());
        storageParent.setAuthor(storageObject.getAuthor());

        return storageParent;
    }

    public static StorageVersionObject mapVersionObject (StorageVersion storageVersion) {
        if (storageVersion == null) {
            return null;
        }

        StorageVersionObject storageVersionObject = new StorageVersionObject();
        storageVersionObject.setId(storageVersion.getId());
        storageVersionObject.setLocked(storageVersion.isLocked());
        storageVersionObject.setParentId(storageVersion.getParentId());
        storageVersionObject.setProperties(storageVersion.getProperties());
        storageVersionObject.setVersion(storageVersion.getVersion());
        return storageVersionObject;
    }

    public static StorageVersion mapVersion (StorageObject storageObject) {
        if (storageObject == null) {
            return null;
        }

        StorageVersion storageVersion = new StorageVersion();
        storageVersion.setProperties(storageObject.getProperties());
        return storageVersion;
    }
}
