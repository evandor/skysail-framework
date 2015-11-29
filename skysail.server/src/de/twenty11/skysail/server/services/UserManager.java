package de.twenty11.skysail.server.services;

import java.util.List;

import org.osgi.annotation.versioning.ProviderType;

import de.twenty11.skysail.server.um.domain.*;

@ProviderType
public interface UserManager {

    SkysailUser findByUsername(String username);

    SkysailUser findById(String id);

    List<SkysailGroup> getGroupsForUser(String username);

	void update(SkysailUser user);

}
