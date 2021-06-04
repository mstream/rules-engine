package com.mstream.rulesengine.ratpack.demo.factprovider;

import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.content.ContentService;
import ratpack.exec.Promise;
import ratpack.func.Pair;

import java.util.Set;

public class IsContentFreeToAirFactProviderCreator implements FactProviderCreator {

    @Override
    public String getFactName() {
        return "IS_FREE_TO_AIR";
    }

    @Override
    public Set<Class<?>> getProducedObject() {
        return null;
    }

    @Override
    public Promise<Pair<Boolean, Data>> apply(Data data) {

        UserProfile userProfile = data.getContext().get(UserProfile.class);

        return data.getContext().get(ContentService.class)
                .isContentFreeToAir(userProfile.getContentId())
                .map(result -> Pair.of(result, data));
    }
}
