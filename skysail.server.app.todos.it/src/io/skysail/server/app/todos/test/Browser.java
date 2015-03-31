package io.skysail.server.app.todos.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.representation.Representation;

@Slf4j
public class Browser {

    private Client client;

    public Browser(String url) {
        log.info("{}creating new browser client with url '{}' and mediaType '{}'", Client.TESTTAG, url,
                MediaType.TEXT_HTML);
        client = new Client(url, MediaType.TEXT_HTML);
    }

    public Browser asUser(String username) {
        log.info("{}logging in as user '{}'", Client.TESTTAG, username);
        client.loginAs(username, "skysail");
        return this;
    }

    public Status getStatus() {
        return client.getResponse().getStatus();
    }

    public Representation getTodoLists() {
        log.info("{}retrieving TodoLists", Client.TESTTAG);
        getTodoLists(client);
        return client.getCurrentRepresentation();
    }

    public Representation getTodoList(String id) {
        log.info("{}retrieving TodoList #{}", Client.TESTTAG, id);
        getTodoList(client, id);
        return client.getCurrentRepresentation();
    }

    public void deleteTodoList(String id) {
        log.info("{}deleting TodoList #{}", Client.TESTTAG, id);
        deleteTodoList(client, id);
        // return client.getCurrentRepresentation();
    }

    protected Reference createTodoList(TodoList todoList) {
        navigateToPostTodoListAs(client);
        client.post(createForm(todoList));
        return client.getLocation();
    }

    private Form createForm(TodoList todoList) {
        Form form = new Form();
        form.add("name", todoList.getName());
        return form;
    }

    private void getTodoLists(Client client) {
        client.gotoRoot();
        client.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void getTodoList(Client client, String id) {
        client.gotoRoot();
        client.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void navigateToPostTodoListAs(Client client) {
        client.gotoRoot();
        client.followLinkTitle(TodoApplication.APP_NAME).followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void deleteTodoList(Client client, String id) {
        client.gotoRoot() //
            .followLinkTitle(TodoApplication.APP_NAME)//
            .followLinkTitle("update");

    }

}
