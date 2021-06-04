package com.mstream.rulesengine.ratpack.demo.actionhandler;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.ActionHandlerCreator;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketId;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketsDao;
import ratpack.exec.Promise;

import java.util.Set;

public class CreateStreamingTicketActionHandlerCreator implements ActionHandlerCreator {

    @Override
    public String getActionName() {
        return "CREATE_STREAMING_TICKET";
    }

    @Override
    public Set<Class<?>> getRequiredObjects() {
        return ImmutableSet.of(StreamingTicketId.class);
    }

    @Override
    public Promise<Void> apply(Data data) {
        UserProfile userProfile = data.getContext().get(UserProfile.class);
        StreamingTicketsDao streamingTicketsDao = data.getContext().get(StreamingTicketsDao.class);

        return Promise.ofNull().map(ignore -> {
            streamingTicketsDao.addStreamingTicket(
                    userProfile,
                    data.getObjects().get(StreamingTicketId.class).getId()).then(ignored ->
                    streamingTicketsDao.getAllTickets(userProfile.getHouseholdId())
                            .then(streamingTickets -> System.out.println("Created streaming ticket :" + streamingTickets)));
            return null;
        });
    }
}
