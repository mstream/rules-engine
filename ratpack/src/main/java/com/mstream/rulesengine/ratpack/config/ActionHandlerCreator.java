package com.mstream.rulesengine.ratpack.config;

import com.mstream.rulesengine.ratpack.ObjectConsumer;
import ratpack.exec.Promise;

import java.util.Set;
import java.util.function.Function;


public interface ActionHandlerCreator extends Function<Data, Promise<Void>>, ObjectConsumer {

    String getActionName();
}
