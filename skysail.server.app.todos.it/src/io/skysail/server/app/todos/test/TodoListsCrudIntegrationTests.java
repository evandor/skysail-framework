package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoList;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
public class TodoListsCrudIntegrationTests extends IntegrationTests<TodoListBrowser, TodoList> {
    
    private TodoList todoList;

    @Before
    public void setUp() {
        browser = new TodoListBrowser(MediaType.APPLICATION_JSON);
        browser.setUser("admin");
        todoList = createTodoList();
    }

    @Test
    public void creating_todolist_persists_new_todolist() throws Exception {
        browser.createTodoList(todoList);
        String html = browser.getTodoLists().getText();
        assertThat(html, containsString(todoList.getName()));
    }
    
    @Test
    public void new_todolist_can_be_deleted_by_owner() throws Exception {
        String id = browser.createTodoList(todoList);
        browser.deleteTodoList(id);
        assertThat(browser.getTodoLists().getText(), not(containsString(todoList.getName())));
    }

    @Test
    public void altering_todolist_updates_existing_todolist() throws Exception {
        String id = browser.createTodoList(todoList);
        assertThat(browser.getTodoList(id).getText(), containsString(todoList.getName()));
        
        todoList.setId(id);
        //todoList.setName("crudlist3!");
        todoList.setDesc("description changed");
        browser.updateTodoList(todoList);
    }

    private TodoList createTodoList() {
        return new TodoList(new BigInteger(130, random).toString(32));
    }
}

