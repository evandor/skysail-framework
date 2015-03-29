package io.skysail.server.documentation.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

public class Browser {

    private Client client;

    public Browser(String url) {
        client = new Client(url, MediaType.TEXT_HTML);
    }

    protected Reference createTodoListAs(String username, TodoList todoList) {
        navigateToPostTodoListAs(client, "admin");
        client.post(createForm(todoList));
        return client.getLocation();
    }

    private Form createForm(TodoList todoList) {
        Form form = new Form();
        form.add("name", todoList.getName());
        return form;
    }

    private void navigateToPostTodoListAs(Client client, String username) {
        client.loginAs(username, "skysail").followLinkTitle(TodoApplication.APP_NAME)
                .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    protected void getTodoListsFor(Client client, String username) {
        client.loginAs(username, "skysail").followLinkTitle(TodoApplication.APP_NAME);
    }

    public Status getStatus() {
        return client.getResponse().getStatus();
    }

    public Representation getTodosFor(String username) {
        getTodoListsFor(client, username);
        return client.getCurrentRepresentation();
    }

}
