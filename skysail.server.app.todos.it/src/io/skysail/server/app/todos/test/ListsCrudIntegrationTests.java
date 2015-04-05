package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.todos.Todo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

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
        browser.createTodoList(new TodoList("crudlist1"));
        String html = browser.getTodoLists().getText();
        assertThat(html, containsString("crudlist1"));
    }
    
    @Test
    public void new_todolist_can_be_deleted_by_owner() throws Exception {
        String id = browser.createTodoList(new TodoList("crudlist2"));
        browser.deleteTodoList(id);
        Representation html = browser.getTodoLists();
        assertThat(html.getText(), not(containsString("crudlist2")));
    }

    @Test
    @Ignore
    public void altering_todolist_updates_existing_todolist() throws Exception {
        TodoList theTodoList = new TodoList("crudlist3");
        String id = browser.createTodoList(theTodoList);
        String html = browser.getTodoList(id).getText();
        assertThat(html, containsString("crudlist3"));
        
        theTodoList.setId(id);
        theTodoList.setName("crudlist3!");
        browser.updateTodoList(theTodoList);
    }

}
