package com.mstream.rulesengine.ratpack.demo.actionprehandler;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.ActionPreHandlerCreator;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketId;
import ratpack.exec.Promise;
import ratpack.registry.Registry;

import java.util.Set;
import java.util.UUID;

public class StreamingTicketIdGenerationActionPreHandlerCreator implements ActionPreHandlerCreator {
    @Override
    public String getPreHandlerName() {
        return "streamingTicketIdGeneration";
    }

    @Override
    public Set<Class<?>> getProducedObject() {
        return ImmutableSet.of(StreamingTicketId.class);
    }

    @Override
    public Promise<Data> apply(Data data) {
        return Promise.value(data.withObjects(
                Registry.single(new StreamingTicketId(UUID.randomUUID().toString()))));
    }
}
