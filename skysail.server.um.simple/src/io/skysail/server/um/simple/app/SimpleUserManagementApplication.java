package io.skysail.server.um.simple.app;

import io.skysail.api.um.RestletRolesProvider;
import aQute.bnd.annotation.component.Component;
import de.twenty11.skysail.server.app.SkysailApplication;

@Component(immediate = true)
public class SimpleUserManagementApplication extends SkysailApplication implements RestletRolesProvider {
    // providing restlet roles.
}
