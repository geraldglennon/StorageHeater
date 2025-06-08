package com.glennon.storageHeater.presentation.controllers;

import com.glennon.storageHeater.presentation.api.LabelObject;
import com.glennon.storageHeater.presentation.api.StorageObject;
import com.glennon.storageHeater.presentation.model.StorageParent;
import com.glennon.storageHeater.presentation.model.StorageVersion;
import com.glennon.storageHeater.presentation.services.StorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Gerald on 03/06/2017.
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @GetMapping
    public ResponseEntity getAllStorage(@RequestParam(defaultValue = "0", name = "page") int page,
                                        @RequestParam(defaultValue = "20", name = "perPage") int perPage,
                                        @RequestParam(defaultValue = "", name = "q") String q) {

        return storageService.getPaginated(page, perPage, q);
    }

    @GetMapping(value = "/{uid}")
    public ResponseEntity getStorage(@PathVariable(name = "uid") String uid) {
        return storageService.getStorage(uid);
    }

    @GetMapping(value = "/{uid}/version/{version}")
    public ResponseEntity getStorageVersion(@PathVariable(name = "uid") String uid, @PathVariable(name = "version") String version) {
        return storageService.getStorageVersion(uid, version);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid StorageObject storageObject) {
        return storageService.createStorage(storageObject);
    }

    @PostMapping("/{uid}/version")
    public ResponseEntity createNewVersion(@PathVariable(name = "uid") String uid) {
        return storageService.createNewVersion(uid);
    }

    @PutMapping("/{uid}/version")
    public ResponseEntity updateVersion(@PathVariable(name = "uid") String uid, @RequestBody StorageVersion storageVersion) {
        return storageService.updateStorage(uid, storageVersion);
    }

    @PutMapping
    public ResponseEntity updateParent(@RequestBody StorageParent storageParent) {
        return storageService.updateParent(storageParent);
    }

    @PutMapping("/lock/{uid}/{lock}")
    public ResponseEntity lockOrUnlockStorageVersion(@PathVariable(name = "uid") String uid, @PathVariable boolean lock) {
        return storageService.lockOrUnlockStorageVersion(uid, lock);
    }

    @PutMapping("/labels")
    public ResponseEntity addLabel(@RequestBody LabelObject label) {
        return storageService.addLabel(label);
    }

    @GetMapping("/labels/{name}")
    public ResponseEntity getLabel(@PathVariable(name = "name") String name) {
        return storageService.getLabel(name);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity delete(@PathVariable(name = "uid") String uid) {
        return storageService.deleteStorage(uid);
    }

}
