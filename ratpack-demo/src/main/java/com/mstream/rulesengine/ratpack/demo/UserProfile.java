package com.mstream.rulesengine.ratpack.demo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserProfile {

    private final String domainId;
    private final String householdId;
    private final String territory;
    private final Integer contentId;
    private final String deviceId;

    @JsonCreator
    public UserProfile(
            @JsonProperty("domainId") String domainId,
            @JsonProperty("householdId") String householdId,
            @JsonProperty("territory") String territory,
            @JsonProperty("contentId") Integer contentId,
            @JsonProperty("deviceId") String deviceId) {
        this.domainId = domainId;
        this.householdId = householdId;
        this.territory = territory;
        this.contentId = contentId;
        this.deviceId = deviceId;
    }

    public String getDomainId() {
        return domainId;
    }

    public String getHouseholdId() {
        return householdId;
    }

    public String getTerritory() {
        return territory;
    }

    public Integer getContentId() {
        return contentId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "domainId='" + domainId + '\'' +
                ", householdId='" + householdId + '\'' +
                ", territory='" + territory + '\'' +
                ", contentId=" + contentId +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }

}
