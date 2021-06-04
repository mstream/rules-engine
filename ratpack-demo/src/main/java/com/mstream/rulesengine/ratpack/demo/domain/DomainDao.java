package com.mstream.rulesengine.ratpack.demo.domain;

import com.mstream.rulesengine.ratpack.demo.IdFixtures;
import ratpack.exec.Promise;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DomainDao {

    public Promise<Integer> getStreamingSlots(String domainId) {
        return Promise.value(
                IdFixtures.DOMAIN_WITH_TWO_STREAMING_SLOTS.equals(domainId) ?
                        2 :
                        90000000);
    }

    public Promise<List<String>> getTerritories(String domainId) {
        return Promise.value(
                IdFixtures.DOMAIN_WITH_ALLOWED_GB_TERRITORY.equals(domainId) ?
                        Collections.singletonList("GB") : Collections.singletonList("IT")
        );
    }
}
