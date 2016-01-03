package de.twenty11.skysail.server.services;

import org.osgi.annotation.versioning.ProviderType;

import de.twenty11.skysail.server.um.domain.SkysailUser;

@ProviderType
public interface UserManager {

    SkysailUser findByUsername(String username);

    SkysailUser findById(String id);

	void update(SkysailUser user);

}
