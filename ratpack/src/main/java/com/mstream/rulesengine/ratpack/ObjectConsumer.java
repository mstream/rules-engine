package com.mstream.rulesengine.ratpack;

import java.util.Set;

public interface ObjectConsumer {

    Set<Class<?>> getRequiredObjects();
}
