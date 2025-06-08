package com.glennon.storageHeater.presentation.services;

import com.glennon.storageHeater.presentation.QueryConverter;
import com.glennon.storageHeater.presentation.api.LabelObject;
import com.glennon.storageHeater.presentation.api.StorageObject;
import com.glennon.storageHeater.presentation.api.StorageVersionObject;
import com.glennon.storageHeater.presentation.mappers.LabelMapper;
import com.glennon.storageHeater.presentation.model.*;
import com.glennon.storageHeater.presentation.repositories.LabelRepository;
import com.glennon.storageHeater.presentation.repositories.StorageParentRepository;
import com.glennon.storageHeater.presentation.repositories.StorageVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.glennon.storageHeater.presentation.mappers.StorageMapper.mapParent;
import static com.glennon.storageHeater.presentation.mappers.StorageMapper.mapVersion;
import static com.glennon.storageHeater.presentation.mappers.StorageMapper.mapVersionObject;

/**
 * Created by Gerald on 30/08/2017.
 */

@Service
public class StorageService {

    @Autowired
    private StorageParentRepository storageParentRepository;

    @Autowired
    private StorageVersionRepository storageVersionRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponseEntity getPaginated(int page, int perPage, String q) {
        Pageable pageable = PageRequest.of(page, perPage, Sort.by(Sort.Direction.ASC, "name"));
        Optional<Page<StorageParent>> pagination = null;

        if (q.length() > 0) {
            Query query = QueryConverter.convert(q);
            query.limit(perPage);
            query.skip(page);

            List<StorageParent> storageObjects = mongoTemplate.find(query, StorageParent.class);
            long count = mongoTemplate.count(query, StorageParent.class);

            Page<StorageParent> paginationator = new PageImpl<>(storageObjects, pageable, count);
            return new ResponseEntity(paginationator, HttpStatus.OK);
        } else {
            pagination = Optional.ofNullable(storageParentRepository.findAll(pageable));
            return new ResponseEntity(pagination.get(), HttpStatus.OK);
        }

    }

    public ResponseEntity getStorage (String uid) {
        Optional<StorageParent> storageObject = storageParentRepository.findById(uid);
        if (!storageObject.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Collections.reverse(storageObject.get().getVersions());

        return new ResponseEntity(storageObject.get(), HttpStatus.OK);
    }

    public ResponseEntity getStorageVersion (String uid, String version) {
        Optional<StorageParent> storageObject = storageParentRepository.findById(uid);
        if (!storageObject.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            boolean contains = storageObject.get().getVersions().contains(version);
            if (!contains) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }
        }

        Version versionObj = new Version(version);

        Optional<StorageVersion> storageVersion = Optional.ofNullable(
                storageVersionRepository.findByParentIdAndVersionMajorAndVersionMinorAndVersionBuild(
                        storageObject.get().getId(),
                        versionObj.getMajor(),
                        versionObj.getMinor(),
                        versionObj.getBuild())
        );

        if (!storageVersion.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        StorageVersionObject storageVersionObject = mapVersionObject(storageVersion.get());

        Optional<Label> label = Optional.ofNullable(labelRepository.findByParentIdAndVersion(
                storageVersionObject.getParentId(),
                storageVersionObject.getVersion().toString()));

        if (label.isPresent()) {
            storageVersionObject.setLabel(label.get().getName());
        }

        return new ResponseEntity(storageVersionObject, HttpStatus.OK);
    }

    public ResponseEntity createStorage (StorageObject storageObject) {
        Version initialVersion = Version.INITIAL_VERSION;
        StorageParent storageParent = mapParent(storageObject);
        storageParent.getVersions().add(initialVersion.toString());
        StorageParent savedStorageParent = storageParentRepository.save(storageParent);

        StorageVersion storageVersion = mapVersion(storageObject);
        storageVersion.setVersion(initialVersion);
        storageVersion.setParentId(savedStorageParent.getId());
        storageVersionRepository.save(storageVersion);

        return new ResponseEntity(savedStorageParent, HttpStatus.CREATED);
    }

    public ResponseEntity updateStorage (String uid, StorageVersion storageVersion) {
        Optional<StorageVersion> foundStorageObject = storageVersionRepository.findById(uid);
        if (!foundStorageObject.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else if (foundStorageObject.get().isLocked()) {
            return new ResponseEntity(HttpStatus.LOCKED);
        }
        StorageVersion save = storageVersionRepository.save(storageVersion);
        return new ResponseEntity(save, HttpStatus.OK);
    }

    public ResponseEntity lockOrUnlockStorageVersion (String uid, boolean lock) {
        Optional<StorageVersion> foundStorageObject = storageVersionRepository.findById(uid);
        if (!foundStorageObject.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        StorageVersion storageObject = foundStorageObject.get();
        storageObject.setLocked(lock);
        storageVersionRepository.save(storageObject);
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity deleteStorage (String uid) {
        Optional<StorageParent> storageParent = storageParentRepository.findById(uid);

        if (storageParent.isPresent()) {
            StorageParent parent = storageParent.get();
            parent.getVersions().stream().forEach(version -> {
                Version versionObj = new Version(version);
                StorageVersion byIdAndVersion = storageVersionRepository.findByParentIdAndVersionMajorAndVersionMinorAndVersionBuild(parent.getId(),
                        versionObj.getMajor(),
                        versionObj.getMinor(),
                        versionObj.getBuild());

                storageVersionRepository.delete(byIdAndVersion);
            });
            storageParentRepository.delete(parent);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity createNewVersion (String uid) {
        Optional<StorageParent> storageParent = storageParentRepository.findById(uid);

        if (storageParent.isPresent()) {
            StorageParent parent = storageParent.get();
            int versionsSize = parent.getVersions().size();
            String version = parent.getVersions().get(versionsSize - 1);
            Version versionObj = new Version(version);


            Optional<StorageVersion> storageVersion = Optional.ofNullable(
                    storageVersionRepository.findByParentIdAndVersionMajorAndVersionMinorAndVersionBuild(
                            parent.getId(),
                            versionObj.getMajor(),
                            versionObj.getMinor(),
                            versionObj.getBuild())
            );

            if (storageVersion.isEmpty()) {
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            storageVersion.get().setId(null);
            versionObj.incrementBuild();
            storageVersion.get().setVersion(versionObj);
            StorageVersion savedStorageVersion = storageVersionRepository.save(storageVersion.get());

            parent.getVersions().add(savedStorageVersion.getVersion().toString());
            storageParentRepository.save(parent);

            Collections.reverse(parent.getVersions());

            return new ResponseEntity(parent, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity updateParent(StorageParent storageParent) {
        Optional<StorageParent> parent = storageParentRepository.findById(storageParent.getId());
        if (parent.isPresent()) {
            storageParent.setVersions(parent.get().getVersions());
            StorageParent save = storageParentRepository.save(storageParent);

            Collections.reverse(save.getVersions());
            return new ResponseEntity(save, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity getLabel(String name) {
        Optional<Label> label = Optional.ofNullable(labelRepository.findByName(name));
        if (label.isPresent()) {
            Version version = new Version(label.get().getVersion());
            StorageVersion byIdAndVersion = storageVersionRepository.findByParentIdAndVersionMajorAndVersionMinorAndVersionBuild(label.get().getParentId(),
                    version.getMajor(),
                    version.getMinor(),
                    version.getBuild());

            StorageVersionObject storageVersionObject = mapVersionObject(byIdAndVersion);
            return new ResponseEntity(storageVersionObject, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity addLabel(LabelObject label) {
        Optional<Label> foundLabel = Optional.ofNullable(
                labelRepository.findByParentIdAndVersion(label.getParentId(), label.getVersion()));
        if (foundLabel.isPresent()) {
            Label updateLabel = foundLabel.get();
            updateLabel.setName(label.getName());
            labelRepository.save(updateLabel);
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } else {
            labelRepository.insert(LabelMapper.mapEntity(label));
            return new ResponseEntity(HttpStatus.CREATED);
        }
    }
}
