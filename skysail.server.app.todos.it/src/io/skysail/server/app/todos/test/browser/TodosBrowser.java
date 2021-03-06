package io.skysail.server.app.todos.test.browser;

import org.restlet.data.*;
import org.restlet.representation.Representation;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.*;
import io.skysail.server.app.todos.*;
import io.skysail.server.app.todos.todos.Todo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TodosBrowser extends ApplicationBrowser<TodosBrowser, Todo> {

    public TodosBrowser(MediaType mediaType, int port) {
        super(TodoApplication.APP_NAME, mediaType, port);
    }

    protected Form createForm(Todo todo) {
        Form form = new Form();
        form.add("title", todo.getTitle());
        return form;
    }

    public void createTodo(String listId, Todo todo) {
        log.info("{}creating new Todo {} in List {}", ApplicationClient.TESTTAG, todo, listId);
        login();
        createTodo(client, listId, todo);
    }

    public Representation getTodosForList(String id) {
        log.info("{}getting todos for List #{}", ApplicationClient.TESTTAG, id);
        login();
        getTodosForList(client, id);
        return client.getCurrentRepresentation();
    }

    public String createTodoList(TodoList todoList) {
        log.info("{}creating todoList {}", ApplicationClient.TESTTAG, todoList);
        login();
        navigateToPostTodoListAs(client);
        client.post(createForm(todoList));
        return client.getLocation().getLastSegment(true);
    }

    private String createTodo(ApplicationClient<Todo> client, String listId, Todo todo) {
        navigateToPostTodo(client, listId);
        client.post(createForm(todo));
        return client.getLocation().getLastSegment(true);
    }

    private Form createForm(TodoList todoList) {
        Form form = new Form();
        form.add("name", todoList.getName());
        return form;
    }

    private void navigateToPostTodoListAs(ApplicationClient<Todo> client) {
        client.gotoAppRoot().followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void navigateToPostTodo(ApplicationClient<Todo> client, String listId) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists")
            .followLinkTitleAndRefId("List of Todos", listId)
                .followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void getTodosForList(ApplicationClient<Todo> client, String id) {
        client.gotoAppRoot()
            .followLinkTitle("Show Todo-Lists")
            .followLinkTitleAndRefId("List of Todos", id);
    }

}
