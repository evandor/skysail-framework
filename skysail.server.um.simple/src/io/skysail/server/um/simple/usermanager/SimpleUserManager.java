package io.skysail.server.um.simple.usermanager;

import java.util.List;

import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.services.UserManager;
import de.twenty11.skysail.server.um.domain.SkysailGroup;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@Component
public class SimpleUserManager implements UserManager {

    @Override
    public SkysailUser findByUsername(String username) {
        return null;
    }

    @Override
    public SkysailUser findById(String id) {
        return null;
    }

    @Override
    public List<SkysailGroup> getGroupsForUser(String username) {
        return null;
    }

    @Override
    public void update(SkysailUser user) {

    }

}
