package com.glennon.storageHeater.presentation.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gerald on 13/07/2017.
 */
public class Version {

    private int major;
    private int minor;
    private int build;

    public static final Version INITIAL_VERSION = new Version (0, 0, 1);

    public Version (String version) {
        Pattern pattern = Pattern.compile("^(\\d+).(\\d+).(\\d+)$");
        Matcher matcher = pattern.matcher(version);

        matcher.find();
        if (matcher.groupCount() < 3) {
            throw new IllegalArgumentException();
        }
        this.major = Integer.parseInt(matcher.group(1));
        this.minor = Integer.parseInt(matcher.group(2));
        this.build = Integer.parseInt(matcher.group(3));

    }

    public Version (int major, int minor, int build) {
        this.major = major;
        this.minor = minor;
        this.build = build;
    }

    public Version () {
        // needed for deserialization
    }

    public void incrementBuild () {
        this.build += 1;
    }

    public int getMajor() {
        return major;
    }

    public int getBuild() {
        return build;
    }

    public int getMinor() {
        return minor;
    }

    @Override
    public String toString () {
        return this.major + "." + this.minor + "." + this.build;
    }
}
