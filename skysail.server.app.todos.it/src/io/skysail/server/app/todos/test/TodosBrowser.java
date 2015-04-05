package io.skysail.server.app.todos.test;

import io.skysail.api.links.LinkRelation;
import io.skysail.client.testsupport.ApplicationBrowser;
import io.skysail.client.testsupport.ApplicationClient;
import io.skysail.server.app.todos.TodoApplication;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;
import lombok.extern.slf4j.Slf4j;

import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Representation;

@Slf4j
public class TodosBrowser extends ApplicationBrowser<TodosBrowser, Todo> {

    public TodosBrowser(MediaType mediaType) {
        super(TodoApplication.APP_NAME, mediaType);
    }

    protected Representation getTodoLists() {
        log.info("{}retrieving TodoLists", ApplicationClient.TESTTAG);
        login();
        getTodoLists(client);
        return client.getCurrentRepresentation();
    }

    protected Representation getTodoList(String id) {
        log.info("{}retrieving TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        getTodoList(client, id);
        return client.getCurrentRepresentation();
    }

    protected void deleteTodoList(String id) {
        log.info("{}deleting TodoList #{}", ApplicationClient.TESTTAG, id);
        login();
        deleteTodoList(client, id);
        // return client.getCurrentRepresentation();
    }

    protected void updateTodoList(TodoList theTodoList) {
        log.info("{}updating TodoList #{}", ApplicationClient.TESTTAG, theTodoList.getId());
        login();
        updateTodoList(client, theTodoList);
    }

    protected void createTodo(String listId, Todo todo) {
        log.info("{}creating new Todo {} in List {}", ApplicationClient.TESTTAG, todo, listId);
        login();
        createTodo(client, listId, todo);
    }

    protected Representation getTodosForList(String id) {
        log.info("{}getting todos for List #{}", ApplicationClient.TESTTAG, id);
        login();
        getTodosForList(client, id);
        return client.getCurrentRepresentation();
    }

    protected String createTodoList(TodoList todoList) {
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
    
    private Form createForm(Todo todo) {
        Form form = new Form();
        form.add("title", todo.getTitle());
        return form;
    }

    private void getTodoLists(ApplicationClient<Todo> client) {
        client.gotoRoot();
        client.followLinkTitle(TodoApplication.APP_NAME);
    }

    private void getTodoList(ApplicationClient<?> client, String id) {
        client.gotoRoot().followLinkTitle(TodoApplication.APP_NAME);
    }

    private void navigateToPostTodoListAs(ApplicationClient<Todo> client) {
        client.gotoAppRoot().followLinkRelation(LinkRelation.CREATE_FORM);
    }

    private void navigateToPostTodo(ApplicationClient<Todo> client, String listId) {
        client.gotoAppRoot()
            .followLinkTitleAndRefId("List of Todos", listId)
            .followLinkRelation(LinkRelation.CREATE_FORM);
    }
    private void deleteTodoList(ApplicationClient<?> client, String id) {
        client.gotoAppRoot() //
                .followLinkTitleAndRefId("update", id).followLink(Method.DELETE, null);
    }

    private void updateTodoList(ApplicationClient client, TodoList theTodoList) {
        client.gotoRoot() //
                .followLinkTitle(TodoApplication.APP_NAME)//
                .followLinkTitleAndRefId("update", theTodoList.getId()).followLink(Method.PUT, theTodoList);
    }

    private void getTodosForList(ApplicationClient<Todo> client, String id) {
        client.gotoAppRoot().followLinkTitleAndRefId("List of Todos", id);
    }

}
