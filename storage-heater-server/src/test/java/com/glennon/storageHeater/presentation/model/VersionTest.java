package com.glennon.storageHeater.presentation.model;



import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Created by Gerald on 05/06/2017.
 */
public class VersionTest {

    @Test
    public void testVersion() throws IllegalArgumentException {
        Version version = new Version("1.2.3");

        assertEquals(version.getMajor(), 1);
        assertEquals(version.getMinor(), 2);
        assertEquals(version.getBuild(), 3);
    }

    @Test()
    public void testBadVersion() {
        assertThrows(IllegalStateException.class, ()-> new Version("1.2.3.2"));
    }
}