package de.twenty11.skysail.server.core.restlet;

import org.restlet.security.Authorizer;

public interface RoleAuthorizerFactory {

    Authorizer create(String roleName);

}
