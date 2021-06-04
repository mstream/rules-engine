package com.mstream.rulesengine.ratpack.demo.factprovider;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.household.HouseholdDao;
import ratpack.exec.Promise;
import ratpack.func.Pair;

import java.util.Set;

public class IsHouseholdBlockedFactProviderCreator implements FactProviderCreator {

    @Override
    public String getFactName() {
        return "IS_HOUSEHOLD_BLOCKED";
    }

    @Override
    public Set<Class<?>> getProducedObject() {
        return ImmutableSet.of();
    }

    @Override
    public Promise<Pair<Boolean, Data>> apply(Data data) {
        return data.getContext().get(HouseholdDao.class)
                .getStatus(data.getContext().get(UserProfile.class).getHouseholdId())
                .map(HouseholdDao.Status.BLOCKED::equals)
                .map(result -> Pair.of(result, data));
    }
}
