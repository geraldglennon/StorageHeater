package com.glennon.storageHeater.presentation.repositories;

import com.glennon.storageHeater.presentation.model.StorageParent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Gerald on 03/06/2017.
 */
public interface StorageParentRepository extends MongoRepository<StorageParent, String> {

    Optional<StorageParent> findById(String id);
}
