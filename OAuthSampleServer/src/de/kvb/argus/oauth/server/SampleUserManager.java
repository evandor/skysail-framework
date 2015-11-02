package de.kvb.argus.oauth.server;

import java.util.HashSet;

public class SampleUserManager {

    private HashSet<SampleUser> userSet;

    public SampleUserManager() {
        userSet = new HashSet<SampleUser>();
    }

    public SampleUser addUser(String id) {
        SampleUser user = new SampleUser(id);
        if (!userSet.contains(user)) {
            userSet.add(user);
            return user;
        }
        return null;
    }

    public SampleUser findUserById(String id) {
        for (SampleUser user : userSet) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }
}
