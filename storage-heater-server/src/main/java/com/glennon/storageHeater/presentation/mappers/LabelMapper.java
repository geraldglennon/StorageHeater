package com.glennon.storageHeater.presentation.mappers;

import com.glennon.storageHeater.presentation.model.Label;
import com.glennon.storageHeater.presentation.api.LabelObject;

/**
 * Created by Gerald on 20/08/2017.
 */
public class LabelMapper {

    public static Label mapEntity(LabelObject labelObject) {
        if (labelObject == null) {
            return null;
        }

        Label label = new Label();
        label.setId(labelObject.getId());
        label.setName(labelObject.getName());
        label.setParentId(labelObject.getParentId());
        label.setVersion(labelObject.getVersion());

        return label;
    }
}
