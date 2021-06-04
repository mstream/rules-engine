package com.mstream.rulesengine.ratpack;

import java.util.Set;

public interface ObjectProducer {

    Set<Class<?>> getProducedObject();
}
