package com.mstream.rulesengine.ratpack.config;

import com.mstream.rulesengine.ratpack.ObjectProducer;
import ratpack.exec.Promise;

import java.util.function.Function;


public interface ActionPreHandlerCreator extends Function<Data, Promise<Data>>, ObjectProducer {

    String getPreHandlerName();

}
