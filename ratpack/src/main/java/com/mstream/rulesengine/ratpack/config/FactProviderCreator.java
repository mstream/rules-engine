package com.mstream.rulesengine.ratpack.config;

import com.mstream.rulesengine.ratpack.ObjectProducer;
import ratpack.exec.Promise;
import ratpack.func.Pair;

import java.util.function.Function;


public interface FactProviderCreator extends Function<Data, Promise<Pair<Boolean, Data>>>, ObjectProducer {

    String getFactName();
}
