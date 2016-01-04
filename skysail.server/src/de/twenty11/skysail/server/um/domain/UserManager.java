package de.twenty11.skysail.server.um.domain;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface UserManager {

    SkysailUser findByUsername(String username);

    SkysailUser findById(String id);

	void update(SkysailUser user);

}
