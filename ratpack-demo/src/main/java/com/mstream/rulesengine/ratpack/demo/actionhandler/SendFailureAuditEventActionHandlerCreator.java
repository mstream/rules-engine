package com.mstream.rulesengine.ratpack.demo.actionhandler;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.ActionHandlerCreator;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import ratpack.exec.Promise;

import java.util.Set;

public class SendFailureAuditEventActionHandlerCreator implements ActionHandlerCreator {
    @Override
    public String getActionName() {
        return "SEND_START_STREAM_FAILURE_AUDIT_EVENT";
    }

    @Override
    public Set<Class<?>> getRequiredObjects() {
        return ImmutableSet.of();
    }

    @Override
    public Promise<Void> apply(Data data) {
        return Promise.ofNull().map(ignore -> {
            System.out.println("Sending failure audit event for start stream:" + data.getContext().get(UserProfile.class));
            return null;
        });
    }
}
