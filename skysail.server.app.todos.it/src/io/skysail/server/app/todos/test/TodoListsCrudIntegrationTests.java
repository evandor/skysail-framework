package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.test.browser.TodoListBrowser;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Ignore;
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
        browser = new TodoListBrowser(MediaType.APPLICATION_JSON, determinePort());
        browser.setUser("admin");
        todoList = createRandomTodoList();
    }

    @Test
    public void creating_new_todolist_will_persists_it() throws Exception {
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
    @Ignore // cannot follow link as it is not displayed
    public void new_todolist_cannot_be_deleted_by_someone_else() throws Exception {
        String id = browser.createTodoList(todoList);
        browser.setUser("demo");
        browser.deleteTodoList(id);
        assertThat(browser.getTodoLists().getText(), not(containsString(todoList.getName())));
    }

    @Test
    public void altering_todolist_updates_existing_todolist() throws Exception {
        String id = browser.createTodoList(todoList);
        assertThat(browser.getTodoList(id).getText(), containsString(todoList.getName()));
        
        todoList.setId(id);
        todoList.setDesc("description changed");
        browser.updateTodoList(todoList);
        
        String updatedText = browser.getTodoList(id).getText();
        assertThat(updatedText, containsString("description changed"));
    }

    private TodoList createRandomTodoList() {
        return new TodoList(new BigInteger(130, random).toString(32));
    }
}

