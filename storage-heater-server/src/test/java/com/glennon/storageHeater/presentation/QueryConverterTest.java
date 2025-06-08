package com.glennon.storageHeater.presentation;


import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.query.Query;

import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Created by Gerald on 05/06/2017.
 */
public class QueryConverterTest {

    @Test
    public void testConvert() throws Exception {
        Query query = QueryConverter.convert("name=test&id!=59354&test~=123tyr");
        boolean name = query.getQueryObject().containsKey("name");
        boolean id = query.getQueryObject().containsKey("id");
        boolean test = query.getQueryObject().containsKey("test");

        assertTrue(name);
        assertTrue(id);
        assertTrue(test);
    }
}