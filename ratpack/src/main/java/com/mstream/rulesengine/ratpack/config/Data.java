package com.mstream.rulesengine.ratpack.config;

import ratpack.handling.Context;
import ratpack.registry.Registry;

public class Data {

    private final Context context;
    private final Registry objects;

    private Data(Context context, Registry objects) {
        this.context = context;
        this.objects = objects;
    }

    public static Data create(Context context) {
        return new Data(context, Registry.empty());
    }

    public Data withObjects(Registry registry) {
        return new Data(context, objects.join(registry));
    }

    public Context getContext() {
        return context;
    }

    public Registry getObjects() {
        return objects;
    }
}
