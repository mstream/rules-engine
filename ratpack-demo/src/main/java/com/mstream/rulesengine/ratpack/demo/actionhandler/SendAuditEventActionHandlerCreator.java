package com.mstream.rulesengine.ratpack.demo.actionhandler;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.ActionHandlerCreator;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketId;
import ratpack.exec.Promise;

import java.util.Set;

public class SendAuditEventActionHandlerCreator implements ActionHandlerCreator {
    @Override
    public String getActionName() {
        return "SEND_START_STREAM_AUDIT_EVENT";
    }

    @Override
    public Set<Class<?>> getRequiredObjects() {
        return ImmutableSet.of(StreamingTicketId.class);
    }

    @Override
    public Promise<Void> apply(Data data) {
        return Promise.ofNull().map(ignore -> {
            ImmutableMap<String, Object> info = ImmutableMap.of(
                    "profile", data.getContext().get(UserProfile.class),
                    "streamingTicketId", data.getObjects().get(StreamingTicketId.class).getId());

            System.out.println("Sending start stream audit event:" + info);
            return null;
        });
    }
}
