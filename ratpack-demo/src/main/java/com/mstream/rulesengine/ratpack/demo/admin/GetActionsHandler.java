package com.mstream.rulesengine.ratpack.demo.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mstream.rulesengine.ratpack.config.ActionHandlerCreator;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GetActionsHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        List<String> actionHandlerNames = StreamSupport
                .stream(ctx.getAll(ActionHandlerCreator.class).spliterator(), false)
                .map(ActionHandlerCreator::getActionName)
                .collect(Collectors.toList());

        ctx.getResponse().send(
                ctx.get(ObjectMapper.class)
                        .writeValueAsBytes(actionHandlerNames));
    }
}
