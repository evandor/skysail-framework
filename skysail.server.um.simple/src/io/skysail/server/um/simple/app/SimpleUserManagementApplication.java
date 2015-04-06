package io.skysail.server.um.simple.app;

import io.skysail.api.um.RestletRolesProvider;
import io.skysail.server.app.SkysailApplication;
import aQute.bnd.annotation.component.Component;

@Component(immediate = true)
public class SimpleUserManagementApplication extends SkysailApplication implements RestletRolesProvider {
    // providing restlet roles.
}
