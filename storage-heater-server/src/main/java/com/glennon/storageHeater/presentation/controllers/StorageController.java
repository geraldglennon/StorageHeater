package com.glennon.storageHeater.presentation.controllers;

import com.glennon.storageHeater.presentation.api.LabelObject;
import com.glennon.storageHeater.presentation.api.StorageObject;
import com.glennon.storageHeater.presentation.model.*;
import com.glennon.storageHeater.presentation.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Gerald on 03/06/2017.
 */
@RestController
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping(value = "/api/storage")
    public ResponseEntity getAllStorage(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "20") int perPage,
                                        @RequestParam(defaultValue = "") String q) {

        return storageService.getPaginated(page, perPage, q);
    }

    @GetMapping(value = "/api/storage/{uid}")
    public ResponseEntity getStorage(@PathVariable String uid) {
        return storageService.getStorage(uid);
    }

    @GetMapping(value = "/api/storage/{uid}/version/{version}")
    public ResponseEntity getStorageVersion(@PathVariable String uid, @PathVariable String version) {
        return storageService.getStorageVersion(uid, version);
    }

    @PostMapping(value = "/api/storage", consumes = "application/json; charset=UTF-8")
    public ResponseEntity create(@RequestBody @Valid StorageObject storageObject) {
        return storageService.createStorage(storageObject);
    }

    @PostMapping(value = "/api/storage/{uid}/version", consumes = "application/json; charset=UTF-8")
    public ResponseEntity createNewVersion(@PathVariable String uid) {
        return storageService.createNewVersion(uid);
    }

    @PutMapping(value = "/api/storage/{uid}/version", consumes = "application/json; charset=UTF-8")
    public ResponseEntity updateVersion(@PathVariable String uid, @RequestBody StorageVersion storageVersion) {
        return storageService.updateStorage(uid, storageVersion);
    }

    @PutMapping(value = "/api/storage/", consumes = "application/json; charset=UTF-8")
    public ResponseEntity updateParent(@RequestBody StorageParent storageParent) {
        return storageService.updateParent(storageParent);
    }

    @PutMapping(value = "/api/storage/lock/{uid}/{lock}")
    public ResponseEntity lockOrUnlockStorageVersion(@PathVariable String uid, @PathVariable boolean lock) {
        return storageService.lockOrUnlockStorageVersion(uid, lock);
    }

    @PutMapping(value = "/api/storage/labels")
    public ResponseEntity addLabel(@RequestBody LabelObject label) {
        return storageService.addLabel(label);
    }

    @GetMapping(value = "/api/storage/labels/{name}")
    public ResponseEntity getLabel(@PathVariable String name) {
        return storageService.getLabel(name);
    }

    @DeleteMapping(value = "/api/storage/{uid}")
    public ResponseEntity delete(@PathVariable String uid) {
        return storageService.deleteStorage(uid);
    }

}
