package de.twenty11.skysail.server.services;

import java.util.List;

import aQute.bnd.annotation.ProviderType;
import de.twenty11.skysail.server.um.domain.SkysailGroup;
import de.twenty11.skysail.server.um.domain.SkysailUser;

@ProviderType
public interface UserManager {

    SkysailUser findByUsername(String username);

    SkysailUser findById(String id);

    List<SkysailGroup> getGroupsForUser(String username);

	void update(SkysailUser user);

}
