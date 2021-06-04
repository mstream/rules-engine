package com.mstream.rulesengine.ratpack.demo.admin;

import com.mstream.rulesengine.ratpack.demo.RulesStringKeywordShortener;
import com.mstream.rulesengine.ratpack.demo.RulesSupplier;
import ratpack.handling.Context;
import ratpack.handling.Handler;

public class GetRulesHandler implements Handler {
    @Override
    public void handle(Context ctx) {
        ctx.getResponse()
                .send(ctx.get(RulesStringKeywordShortener.class)
                        .apply(ctx.get(RulesSupplier.class).get()));
    }
}
