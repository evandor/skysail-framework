package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
public class ListsCrudIntegrationTests extends IntegrationTests<TodosBrowser, Todo> {
    
    @Before
    public void setUp() {
        browser = new TodosBrowser(MediaType.APPLICATION_JSON);
        browser.setUser("admin");
    }

    @Test
    public void creating_todolist_persists_new_todolist() throws Exception {
        TodoList todoList = createTodoList();
        browser.createTodoList(todoList);
        String html = browser.getTodoLists().getText();
        assertThat(html, containsString(todoList.getName()));
    }
    
    @Test
    public void new_todolist_can_be_deleted_by_owner() throws Exception {
        TodoList todoList = createTodoList();
        String id = browser.createTodoList(todoList);
        browser.deleteTodoList(id);
        assertThat(browser.getTodoLists().getText(), not(containsString(todoList.getName())));
    }

    @Test
    @Ignore
    public void altering_todolist_updates_existing_todolist() throws Exception {
        TodoList theTodoList = createTodoList();
        String id = browser.createTodoList(theTodoList);
        assertThat(browser.getTodoList(id).getText(), containsString(theTodoList.getName()));
        
        theTodoList.setId(id);
        theTodoList.setName("crudlist3!");
        browser.updateTodoList(theTodoList);
    }

    private TodoList createTodoList() {
        return new TodoList(new BigInteger(130, random).toString(32));
    }
}

