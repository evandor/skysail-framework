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
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
public class ListsCrudIntegrationTests extends IntegrationTests<TodosBrowser, Todo> {
    
    @Before
    public void setUp() {
        browser = new TodosBrowser(MediaType.APPLICATION_JSON);
    }

    @Test
    public void creating_todolist_persists_new_todolist() throws Exception {
        browser.asUser("admin").createTodoList(new TodoList("crudlist1"));
        String html = browser.asUser("admin").getTodoLists().getText();
        assertThat(html, containsString("crudlist1"));
    }
    
    @Test
    public void new_todolist_can_be_deleted_by_owner() throws Exception {
        Reference location = browser.asUser("admin").createTodoList(new TodoList("crudlist2"));
        String id = location.getLastSegment(true);
        browser.asUser("admin").deleteTodoList(id);
        Representation html = browser.asUser("admin").getTodoLists();
        assertThat(html.getText(), not(containsString("crudlist2")));
    }

    @Test
    @Ignore
    public void altering_todolist_updates_existing_todolist() throws Exception {
        TodoList theTodoList = new TodoList("crudlist3");
        Reference location = browser.asUser("admin").createTodoList(theTodoList);
        String id = location.getLastSegment(true);
        String html = browser.asUser("admin").getTodoList(id).getText();
        assertThat(html, containsString("crudlist3"));
        
        theTodoList.setId(id);
        theTodoList.setName("crudlist3!");
        browser.asUser("admin").updateTodoList(theTodoList);
    }

}
