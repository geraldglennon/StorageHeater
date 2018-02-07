package com.glennon.storageHeater.presentation.repositories;

import com.glennon.storageHeater.presentation.model.Label;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by Gerald on 03/06/2017.
 */
public interface LabelRepository extends MongoRepository<Label, String> {

    @Query("{name: ?0}")
    Label findByName(String name);

    @Query("{parentId: ?0, version: ?1}")
    Label findByParentIdAndVersion(String uid, String version);
}
