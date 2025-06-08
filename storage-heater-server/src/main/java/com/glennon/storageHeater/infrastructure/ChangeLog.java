package com.glennon.storageHeater.infrastructure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.glennon.storageHeater.presentation.model.StorageParent;
import com.glennon.storageHeater.presentation.model.StorageVersion;
import com.glennon.storageHeater.presentation.repositories.StorageParentRepository;
import com.glennon.storageHeater.presentation.repositories.StorageVersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static com.glennon.storageHeater.infrastructure.Profiles.DEVELOPMENT;

/**
 * Created by Gerald on 03/06/2017.
 */

@Profile(DEVELOPMENT)
@Configuration
public class ChangeLog {

    @Autowired
    private StorageParentRepository storageParentRepository;

    @Autowired
    private StorageVersionRepository storageVersionRepository;

    @Bean
    public boolean addData() throws IOException {
        InputStream testData = this.getClass().getClassLoader().getResourceAsStream("data/testVersion.json");
        InputStream testDataVersions = this.getClass().getClassLoader().getResourceAsStream("data/versionData.json");

        ObjectMapper objectMapper = new ObjectMapper();

        List<StorageParent> storageParents = objectMapper.readValue(testData,
                new TypeReference<List<StorageParent>>(){});

        storageParentRepository.saveAll(storageParents);

        List<StorageVersion> storageVersions = objectMapper.readValue(testDataVersions,
                new TypeReference<List<StorageVersion>>(){});

        storageVersionRepository.saveAll(storageVersions);

        return true;
    }
}
