package com.mstream.rulesengine.ratpack.demo.streamingticket;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.demo.IdFixtures;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import ratpack.exec.Promise;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class StreamingTicketsDao {

    private Map<String, Set<StreamingTicket>> streamingTickets = new ConcurrentHashMap<>();

    public Promise<Set<StreamingTicket>> getAllTickets(String householdId) {
        return Promise.value(
                IdFixtures.HOUSEHOLD_WITH_TWO_ACTIVE_STREAMS.equals(householdId) ?
                        ImmutableSet.of(
                                new StreamingTicket("1", IdFixtures.STREAMING_DEVICE_1),
                                new StreamingTicket("2", IdFixtures.STREAMING_DEVICE_2)
                        ) : fetchStreamingTIckets(householdId));
    }

    private Set<StreamingTicket> fetchStreamingTIckets(String householdId) {

        streamingTickets.computeIfAbsent(householdId, k -> new HashSet<>());

        return streamingTickets.get(householdId);
    }

    public Promise<Void> addStreamingTicket(
            UserProfile userProfile,
            String id) {
        return Promise.ofNull().map(ignore -> {
            if (streamingTickets.get(userProfile.getHouseholdId()) != null) {
                streamingTickets.get(userProfile.getHouseholdId()).add(new StreamingTicket(id, userProfile.getDeviceId()));
            } else {
                Set<StreamingTicket> streamingTicketSet = new HashSet<>();
                streamingTicketSet.add(new StreamingTicket(id, userProfile.getDeviceId()));
                streamingTickets.put(userProfile.getHouseholdId(), streamingTicketSet);
            }
            return null;
        });
    }
}
