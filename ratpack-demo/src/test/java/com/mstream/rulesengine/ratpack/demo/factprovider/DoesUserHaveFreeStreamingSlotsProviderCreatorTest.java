package com.mstream.rulesengine.ratpack.demo.factprovider;

import com.google.common.collect.ImmutableSet;
import com.mstream.rulesengine.ratpack.config.Data;
import com.mstream.rulesengine.ratpack.demo.UserProfile;
import com.mstream.rulesengine.ratpack.demo.domain.DomainDao;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicket;
import com.mstream.rulesengine.ratpack.demo.streamingticket.StreamingTicketsDao;
import org.junit.Test;
import ratpack.exec.ExecResult;
import ratpack.exec.Promise;
import ratpack.func.Pair;
import ratpack.handling.Context;
import ratpack.registry.Registry;
import ratpack.test.exec.ExecHarness;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DoesUserHaveFreeStreamingSlotsProviderCreatorTest {

    @Test
    public void freeStreamingSlots() throws Exception {
        DoesUserHaveFreeStreamingSlotsProviderCreator underTest = new DoesUserHaveFreeStreamingSlotsProviderCreator();

        UserProfile userProfile = new UserProfile(
                "domain",
                "household",
                "territory",
                1,
                "device1");

        DomainDao domainDao = mock(DomainDao.class);
        when(domainDao.getStreamingSlots(any())).thenReturn(Promise.value(2));

        StreamingTicketsDao streamingTicketsDao = mock(StreamingTicketsDao.class);
        when(streamingTicketsDao.getAllTickets(any()))
                .thenReturn(Promise.value(ImmutableSet.of(new StreamingTicket(id, "device2"))));

        Context context = mock(Context.class);
        when(context.get(UserProfile.class)).thenReturn(userProfile);
        when(context.get(DomainDao.class)).thenReturn(domainDao);
        when(context.get(StreamingTicketsDao.class)).thenReturn(streamingTicketsDao);

        Data data = Data.create(context).withObjects(Registry.of(registry ->
                registry
                        .add(userProfile)
                        .add(domainDao)
                        .add(streamingTicketsDao)
        ));

        Promise<Pair<Boolean, Data>> testPromise = underTest.apply(data);

        ExecResult<Pair<Boolean, Data>> execResult = ExecHarness.harness().yield(exec -> testPromise);

        assertTrue(execResult.isSuccess());

    }
}
