package com.mstream.rulesengine.ratpack.demo.actionhandler;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.ActionHandlerCreator;
import com.mstream.rulesengine.ratpack.config.Data;
import ratpack.exec.Promise;

import java.util.Set;

public class ReturnStreamingAllowedResponseActionHandlerCreator implements ActionHandlerCreator {
    @Override
    public String getActionName() {
        return "RETURN_STREAMING_ALLOWED_RESPONSE";
    }

    @Override
    public Set<Class<?>> getRequiredObjects() {
        return ImmutableSet.of();
    }

    @Override
    public Promise<Void> apply(Data data) {
        return Promise.ofNull().map(ignore -> {
            data.getContext().getResponse().status(200).send();
            return null;
        });
    }
}
