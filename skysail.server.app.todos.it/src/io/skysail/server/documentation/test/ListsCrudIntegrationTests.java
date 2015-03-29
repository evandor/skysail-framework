package io.skysail.server.documentation.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.server.app.todos.TodoList;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.Reference;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
public class ListsCrudIntegrationTests extends IntegrationTests {

    private Browser browser;

    @Before
    public void setUp() {
        browser = new Browser(getBaseUrl());
    }

    @Test
    public void creating_todolist_persists_new_todolist() throws Exception {
        browser.createTodoListAs("admin", new TodoList("crudlist1"));
        String html = browser.getTodosFor("admin").getText();
        assertThat(html, containsString("crudlist1"));
    }

    @Test
    public void altering_todolist_updates_existing_todolist() throws Exception {
        Reference location = browser.createTodoListAs("admin", new TodoList("crudlist2"));
        System.out.println(browser.getStatus());        
        System.out.println(location);
        String html = browser.getTodosFor("admin").getText();
        assertThat(html, containsString("crudlist2!"));
    }

}
