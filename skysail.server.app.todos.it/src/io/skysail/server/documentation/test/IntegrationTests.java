package io.skysail.server.documentation.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.todos.TodoApplication;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.restlet.data.Form;
import org.restlet.representation.Representation;

public class IntegrationTests {

    private static final String HOST = "http://localhost";
    private static final String PORT = "2015";

    protected Bundle thisBundle = FrameworkUtil.getBundle(this.getClass());

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    protected String getBaseUrl() {
        return HOST + (PORT != null ? ":" + PORT : "");
    }

    protected Representation createTodoListAs(Client client, String username, Form form) {
        navigateToPostTodoListAs(client, "admin");
        return client.post(form);
    }

    private void navigateToPostTodoListAs(Client client, String username) {
        client.loginAs(username, "skysail")
            .followLinkTitle(TodoApplication.APP_NAME)
            .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    protected void getTodoListsFor(Client client, String username) {
        client.loginAs(username, "skysail")
            .followLinkTitle(TodoApplication.APP_NAME);
    }

}
