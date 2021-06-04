package com.mstream.rulesengine.ratpack.demo;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class RulesSupplier implements Supplier<String> {

    // TODO expand it and make a handler which updates it after a validation
    private static final String DEFAULT_RULES = "[" +
            "{" +
            ":name \"HOUSEHOLD_IS_BLOCKED\" " +
            ":conditions [{\"IS_HOUSEHOLD_BLOCKED\" true}] " +
            ":actions [\"RETURN_STREAMING_FORBIDDEN_RESPONSE\" \"SEND_START_STREAM_FAILURE_AUDIT_EVENT\"] " +
            "}" +
            " " +
            "{" +
            ":name \"COUNTRY_IS_NOT_ALLOWED\" " +
            ":conditions [{\"IS_COUNTRY_ALLOWED\" false}] " +
            ":actions [\"RETURN_STREAMING_FORBIDDEN_RESPONSE\" \"SEND_START_STREAM_FAILURE_AUDIT_EVENT\"] " +
            "}" +
            " " +
            "{" +
            ":name \"NO_DEVICE_SLOTS_REMAINING\" " +
            ":conditions [{\"DOES_USER_HAVE_FREE_STREAMING_SLOTS\" false}] " +
            ":actions [\"RETURN_STREAMING_FORBIDDEN_RESPONSE\" \"SEND_START_STREAM_FAILURE_AUDIT_EVENT\"] " +
            "}" +
            " " +
            "{" +
            ":name \"FREE_TO_AIR\" " +
            ":conditions [{\"IS_FREE_TO_AIR\" true}] " +
            ":actions [\"RETURN_STREAMING_ALLOWED_RESPONSE\" \"SEND_START_STREAM_AUDIT_EVENT\" \"CREATE_STREAMING_TICKET\"] " +
            "}" +
            " " +
            "{" +
            ":name \"STREAMING_ALLOWED\" " +
            ":conditions [{\"IS_COUNTRY_ALLOWED\" true \"IS_HOUSEHOLD_BLOCKED\" false}] " +
            ":actions [\"RETURN_STREAMING_ALLOWED_RESPONSE\" \"SEND_START_STREAM_AUDIT_EVENT\" \"CREATE_STREAMING_TICKET\"] " +
            "}" +
            "]";

    private final AtomicReference<String> stringRef = new AtomicReference<>(DEFAULT_RULES);

    @Override
    public String get() {
        return stringRef.get();
    }

    public void update(String rules) {
        stringRef.set(rules);
    }
}
