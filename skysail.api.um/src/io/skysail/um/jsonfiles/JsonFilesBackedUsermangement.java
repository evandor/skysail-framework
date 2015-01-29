package io.skysail.um.jsonfiles;

import io.skysail.um.api.AuthenticationService;
import io.skysail.um.api.AuthorizationService;
import io.skysail.um.api.UserManagementProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

@Component
public class JsonFilesBackedUsermangement implements UserManagementProvider {

    private volatile AuthenticationService authenticationService;

    private volatile AuthorizationService authorizationService;

    // temporary, will be created from json file later
    private static List<User> users = new ArrayList<User>();

    @Activate
    public void activate() {
        users.add(new User("admin", "skysail"));

        this.authenticationService = new JsonFileBackedAuthenticationService(users);

        this.authorizationService = new AuthorizationService() {

            @Override
            public Set<String> getRolesFor(String username) {
                return null;
            }
        };
    }

    @Deactivate
    public void deactivate() {
        this.authenticationService = null;
        users = Collections.emptyList();
    }

    @Override
    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    @Override
    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

}
