package com.mstream.rulesengine.ratpack.demo.actionhandler;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.ActionHandlerCreator;
import com.mstream.rulesengine.ratpack.config.Data;
import ratpack.exec.Promise;
import ratpack.handling.Context;

import java.util.Set;

public class ReturnStreamingForbiddenResponseActionHandlerCreator implements ActionHandlerCreator {
    @Override
    public String getActionName() {
        return "RETURN_STREAMING_FORBIDDEN_RESPONSE";
    }

    @Override
    public Set<Class<?>> getRequiredObjects() {
        return ImmutableSet.of();
    }

    @Override
    public Promise<Void> apply(Data data) {
        return Promise.ofNull().map(ignore -> {
            data.getContext().getResponse().status(403).send();
            return null;
        });
    }
}
