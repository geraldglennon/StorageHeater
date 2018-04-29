package com.glennon.storageHeater.presentation.controllers;

import com.glennon.storageHeater.presentation.api.UserCredentials;
import com.glennon.storageHeater.presentation.services.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by Gerald on 03/03/2018.
 */
@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private SecurityService securityService;

    @PostMapping
    public ResponseEntity login(@Valid @RequestBody UserCredentials userCredentials) {
        boolean authenticated = securityService.login(userCredentials);
        if (authenticated) {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping
    public ResponseEntity isAuthenticated() {
        if(!securityService.isAuthenticated()) {
            return new ResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
