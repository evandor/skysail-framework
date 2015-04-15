package io.skysail.server.app.todos.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

@Slf4j
public class TodoListBrowser extends ApplicationBrowser<TodoListBrowser, TodoList> {

    public TodoListBrowser(MediaType mediaType) {
        super(TodoApplication.APP_NAME, mediaType, "2014");
    }

    protected String createTodoList(TodoList todoList) {
        log.info("{}creating todoList {}", ApplicationClient.TESTTAG, todoList);
        login();
        navigateToPostTodoListAs(client);
        client.post(createForm(todoList));
        return client.getLocation().getLastSegment(true);
    }

    protected Representation getTodoLists() {
        log.info("{}retrieving TodoLists", ApplicationClient.TESTTAG);
        login();
        getTodoLists(client);
        return client.getCurrentRepresentation();
    }

    protected void deleteTodoList(String id) {
        log.info("{}deleting TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteTodoList(client, id);
        // return client.getCurrentRepresentation();
    }

    protected Representation getTodoList(String id) {
        log.info("{}retrieving TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        getTodoList(client, id);
        return client.getCurrentRepresentation();
    }

    protected void updateTodoList(TodoList theTodoList) {
        log.info("{}updating TodoList #{}", ApplicationClient.TESTTAG, theTodoList.getId());
        login();
        updateTodoList(client, theTodoList);
    }

    private void getTodoLists(ApplicationClient<TodoList> client) {
        client.gotoRoot();
        client.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void updateTodoList(ApplicationClient<TodoList> client, TodoList theTodoList) {
        client.gotoAppRoot()
            .followLinkTitleAndRefId("update", theTodoList.getId())
            .followLink(Method.PUT, theTodoList);
    }

    private void getTodoList(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(TodoApplication.APP_NAME);
    }

    private void deleteTodoList(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id)
                .followLink(Method.DELETE, null);
    }

    private void navigateToPostTodoListAs(ApplicationClient<TodoList> client) {
        client.gotoAppRoot().followLinkRelation(LinkRelation.CREATE_FORM);
    }

    @Override
    protected Form createForm(TodoList todoList) {
        Form form = new Form();
        form.add("name", todoList.getName());
        return form;
    }

}
