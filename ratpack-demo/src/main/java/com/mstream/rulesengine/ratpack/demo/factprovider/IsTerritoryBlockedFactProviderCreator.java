package com.mstream.rulesengine.ratpack.demo.factprovider;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.domain.DomainDao;
import ratpack.exec.Promise;
import ratpack.func.Pair;

import java.util.Set;

public class IsTerritoryBlockedFactProviderCreator implements FactProviderCreator {

    @Override
    public String getFactName() {
        return "IS_COUNTRY_ALLOWED";
    }

    @Override
    public Set<Class<?>> getProducedObject() {
        return ImmutableSet.of();
    }

    @Override
    public Promise<Pair<Boolean, Data>> apply(Data data) {

        UserProfile userProfile = data.getContext().get(UserProfile.class);

        return data.getContext().get(DomainDao.class)
                .getTerritories(userProfile.getDomainId())
                .map(territories -> territories.contains(userProfile.getTerritory()))
                .map(result -> Pair.of(result, data));
    }
}
