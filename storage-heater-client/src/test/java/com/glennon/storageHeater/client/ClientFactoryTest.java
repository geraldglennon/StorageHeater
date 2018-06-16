package com.glennon.storageHeater.client;

import com.glennon.storageHeater.presentation.api.StorageVersionObject;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientFactoryTest {

    @Test
    @Ignore
    public void getByLabelTest() {
        String label = "test";

        StorageVersionObject storageVersionObject = ClientFactory.getByLabel(label, "localhost");

        assertEquals(storageVersionObject.getLabel(), label);
    }

}