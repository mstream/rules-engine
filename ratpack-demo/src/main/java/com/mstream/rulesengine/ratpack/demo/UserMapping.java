package com.mstream.rulesengine.ratpack.demo;

import java.util.HashMap;
import java.util.Map;

public class UserMapping {

    private Map<String, UserProfile> userMapping = new HashMap<>();

    public UserMapping() {
        userMapping.put("userWithBlockedHousehold", new UserProfile(IdFixtures.DOMAIN_WITH_TWO_STREAMING_SLOTS, IdFixtures.BLOCKED_HOUSEHOLD, "GB", 111, "1"));
    }

    public UserProfile getUserProfile(String username) {
        return userMapping.get(username);
    }
}
