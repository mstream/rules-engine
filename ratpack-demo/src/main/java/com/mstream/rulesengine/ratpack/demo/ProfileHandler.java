package com.mstream.rulesengine.ratpack.demo;

import ratpack.handling.Context;
import ratpack.handling.Handler;
import ratpack.registry.Registry;


public class ProfileHandler implements Handler {

    @Override
    public void handle(Context context) {
        context.parse(UserProfile.class).then(userProfile -> context.next(Registry.single(userProfile)));
    }
}
