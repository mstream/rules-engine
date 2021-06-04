package com.mstream.rulesengine.ratpack.demo.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.mstream.rulesengine.core.RulesEngineFactory;
import com.mstream.rulesengine.core.ValidationOptions;
import com.mstream.rulesengine.core.ValidationResult;
import com.mstream.rulesengine.ratpack.demo.RulesStringKeywordExpander;
import com.mstream.rulesengine.ratpack.demo.RulesStringKeywordShortener;
import com.mstream.rulesengine.ratpack.demo.RulesSupplier;
import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.http.TypedData;

public class ValidateRulesHandler implements Handler {

    @Override
    public void handle(Context ctx) {
        ctx.getRequest()
                .getBody()
                .map(TypedData::getText)
                .map(ctx.get(RulesStringKeywordExpander.class)::apply)
                .map(rulesString -> {
                    ValidationResult validationResult = RulesEngineFactory.fromEdn(rulesString).validate(ctx.get(ValidationOptions.class));
                    if (validationResult.getErrors().isEmpty() && validationResult.getWarnings().isEmpty()) {
                        ctx.get(RulesSupplier.class).update(rulesString);
                        ctx.getResponse().status(204).send();
                    } else {
                        String response = ctx.get(ObjectMapper.class).writeValueAsString(ImmutableMap.of(
                                "warnings", validationResult.getWarnings(),
                                "errors", validationResult.getErrors()
                        ));
                        ctx.getResponse().status(400).send(ctx.get(RulesStringKeywordShortener.class).apply(response));
                    }
                    return null;
                }).then(ignore -> {
        });
    }
}
