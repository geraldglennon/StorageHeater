package com.glennon.storageHeater.presentation;

import org.junit.Test;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.Assert.assertTrue;

/**
 * Created by Gerald on 05/06/2017.
 */
public class QueryConverterTest {

    @Test
    public void testConvert() throws Exception {
        Query query = QueryConverter.convert("name=test&id!=59354&test~=123tyr");
        boolean name = query.getQueryObject().containsField("name");
        boolean id = query.getQueryObject().containsField("id");
        boolean test = query.getQueryObject().containsField("test");

        assertTrue(name);
        assertTrue(id);
        assertTrue(test);
    }
}