package com.glennon.storageHeater;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.hazelcast.config.annotation.web.http.EnableHazelcastHttpSession;

@EnableHazelcastHttpSession(maxInactiveIntervalInSeconds = 3600)
@Configuration
public class HazelcastConfig {

}