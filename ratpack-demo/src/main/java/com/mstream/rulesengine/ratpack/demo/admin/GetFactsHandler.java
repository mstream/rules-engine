package com.mstream.rulesengine.ratpack.demo.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import ratpack.handling.Context;
import ratpack.handling.Handler;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class GetFactsHandler implements Handler {
    @Override
    public void handle(Context ctx) throws Exception {
        List<String> factProviderNames = StreamSupport
                .stream(ctx.getAll(FactProviderCreator.class).spliterator(), false)
                .map(FactProviderCreator::getFactName)
                .collect(Collectors.toList());

        ctx.getResponse().send(
                ctx.get(ObjectMapper.class)
                        .writeValueAsBytes(factProviderNames));
    }
}
