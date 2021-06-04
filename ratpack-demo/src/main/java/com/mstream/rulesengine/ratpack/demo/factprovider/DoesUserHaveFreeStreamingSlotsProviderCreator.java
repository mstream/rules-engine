package com.mstream.rulesengine.ratpack.demo.factprovider;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.config.FactProviderCreator;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.domain.DomainDao;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketsDao;
import ratpack.exec.Promise;
import ratpack.func.Pair;

import java.util.Set;

public class DoesUserHaveFreeStreamingSlotsProviderCreator implements FactProviderCreator {

    @Override
    public String getFactName() {
        return "DOES_USER_HAVE_FREE_STREAMING_SLOTS";
    }

    @Override
    public Set<Class<?>> getProducedObject() {
        return ImmutableSet.of();
    }

    @Override
    public Promise<Pair<Boolean, Data>> apply(Data data) {
        UserProfile userProfile = data.getContext().get(UserProfile.class);

        return data.getContext()
                .get(DomainDao.class)
                .getStreamingSlots(userProfile.getDomainId())
                .right(data.getContext().get(StreamingTicketsDao.class).getAllTickets(userProfile.getHouseholdId()))
                .map(maxStreamingSlotsAndStreamingTickets ->
                        maxStreamingSlotsAndStreamingTickets.right().size() < maxStreamingSlotsAndStreamingTickets.left())
                .map(result -> Pair.of(result, data));
    }
}
