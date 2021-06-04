package com.mstream.rulesengine.ratpack.demo.streamingticket;

public class StreamingTicket {

    private final String id;
    private final String deviceId;

    public StreamingTicket(String id, String deviceId) {
        this.id = id;
        this.deviceId = deviceId;
    }

    public String getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public String toString() {
        return "StreamingTicket{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
