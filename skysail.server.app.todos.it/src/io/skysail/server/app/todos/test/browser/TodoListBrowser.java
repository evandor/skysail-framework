package io.skysail.server.app.todos.test.browser;

import io.skysail.client.testsupport.*;
import io.skysail.server.app.todos.*;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.*;
import org.restlet.representation.Representation;

@Slf4j
public class TodoListBrowser extends ApplicationBrowser<TodoListBrowser, TodoList> {

    public TodoListBrowser(MediaType mediaType, String port) {
        super(TodoApplication.APP_NAME, mediaType, port);
    }

    public String createTodoList(TodoList todoList) {
        log.info("{}creating todoList {}", ApplicationClient.TESTTAG, todoList);
        login();
        navigateToPostTodoListAs(client);
        client.post(createForm(todoList));
        return client.getLocation().getLastSegment(true);
    }

    public Representation getTodoLists() {
        log.info("{}retrieving TodoLists", ApplicationClient.TESTTAG);
        login();
        getTodoLists(client);
        return client.getCurrentRepresentation();
    }

    public void deleteTodoList(String id) {
        log.info("{}deleting TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteTodoList(client, id);
    }

    public Representation getTodoList(String id) {
        log.info("{}retrieving TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        getTodoList(client, id);
        return client.getCurrentRepresentation();
    }

    public void updateTodoList(TodoList theTodoList) {
        log.info("{}updating TodoList #{}", ApplicationClient.TESTTAG, theTodoList.getId());
        login();
        updateTodoList(client, theTodoList);
    }

    @Override
    protected Form createForm(TodoList todoList) {
        Form form = new Form();
        form.add("name", todoList.getName());
        return form;
    }
    
    private void getTodoLists(ApplicationClient<TodoList> client) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists");
            //.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void updateTodoList(ApplicationClient<TodoList> client, TodoList theTodoList) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists")
            .followLinkTitleAndRefId("update", theTodoList.getId())
            .followLink(Method.PUT, theTodoList);
    }

    private void getTodoList(ApplicationClient<?> client, String id) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists");
            //.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void deleteTodoList(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
            .followLinkTitle("Show Todo-Lists")
                .followLinkTitleAndRefId("update", id)
                .followLink(Method.DELETE, null);
    }

    private void navigateToPostTodoListAs(ApplicationClient<TodoList> client) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists")
            .followLinkTitle("create new List");
    }

}
