package com.mstream.rulesengine.ratpack.demo.household;

import com.mstream.rulesengine.ratpack.demo.IdFixtures;
import ratpack.exec.Promise;

public class HouseholdDao {

    public enum Status {
        ACTIVE,
        BLOCKED
    }

    public Promise<Status> getStatus(String householdId) {
        return Promise.value(
                IdFixtures.BLOCKED_HOUSEHOLD.equals(householdId) ?
                        Status.BLOCKED :
                        Status.ACTIVE);

    }
}
