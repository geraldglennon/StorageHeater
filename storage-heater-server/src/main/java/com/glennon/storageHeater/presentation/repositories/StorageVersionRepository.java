package com.glennon.storageHeater.presentation.repositories;

import com.glennon.storageHeater.presentation.model.StorageVersion;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

/**
 * Created by Gerald on 03/06/2017.
 */
public interface StorageVersionRepository extends MongoRepository<StorageVersion, String> {

    Optional<StorageVersion> findById(String uid);

    @Query("{parentId: ?0, version.major: ?1, version.minor: ?2, version.build: ?3}")
    StorageVersion findByIdAndVersion(String uid, int major, int minor, int build);


    StorageVersion findByParentIdAndVersionMajorAndVersionMinorAndVersionBuild(String uid, int major, int minor, int build);
}
