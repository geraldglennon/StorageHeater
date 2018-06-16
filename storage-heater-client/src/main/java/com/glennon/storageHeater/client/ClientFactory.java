package com.glennon.storageHeater.client;

import com.glennon.storageHeater.presentation.api.StorageVersionObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;

public class ClientFactory {

    public static StorageVersionObject getByLabel(String label, String host) {
        try {
            RestClient restClient = new RestClient();
            Client client = restClient.getClient();

            return client
                    .target(host)
                    .path("/api/storage/labels/" + label)
                    .request(MediaType.APPLICATION_JSON)
                    .get(StorageVersionObject.class);


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
