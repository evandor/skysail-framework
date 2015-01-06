package de.twenty11.skysail.server.core.restlet.test;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.Restlet;
import org.restlet.data.ClientInfo;
import org.restlet.data.Status;
import org.restlet.security.Role;

import de.twenty11.skysail.server.app.SkysailApplication;
import de.twenty11.skysail.server.core.restlet.RolesPredicateAuthorizer;

public class RolesPredicateAuthorizerTest {

    private Request request;
    private ClientInfo clientInfo;
    
    private Restlet next = new Restlet() {
        @Override
        public void handle(Request request, Response response) {
            response.setStatus(Status.SUCCESS_CREATED);
        }
    };
    private Response response;
    private Role requiredRole;

    @Before
    public void setUp() throws Exception {
        response = Mockito.mock(Response.class);
        request = Mockito.mock(Request.class);
        clientInfo = new ClientInfo();
        clientInfo.setRoles(Collections.emptyList());
        Mockito.when(request.getClientInfo()).thenReturn(clientInfo);
        requiredRole = new Role("IamRequired");
    }

    @Test
    public void testName() throws Exception {
        RolesPredicateAuthorizer authorizer = new RolesPredicateAuthorizer(null);
        authorizer.setNext(next);

        authorizer.handle(request, response);

        Mockito.verify(response).setStatus(Status.SUCCESS_CREATED);
    }
    
    @Test
    public void rejects_execution_if_required_role_is_missing() throws Exception {
        RolesPredicateAuthorizer authorizer = new RolesPredicateAuthorizer(SkysailApplication.anyOf("requiredRole"));
        authorizer.setNext(next);

        authorizer.handle(request, response);

        Mockito.verify(response).setStatus(Status.CLIENT_ERROR_FORBIDDEN);
    }

    @Test
    public void accepts_execution_if_required_role_is_provided() throws Exception {
        RolesPredicateAuthorizer authorizer = new RolesPredicateAuthorizer(SkysailApplication.anyOf("IamRequired"));
        authorizer.setNext(next);
        clientInfo.setRoles(Arrays.asList(new Role[] { requiredRole }));

        authorizer.handle(request, response);

        Mockito.verify(response).setStatus(Status.SUCCESS_CREATED);
    }

}
