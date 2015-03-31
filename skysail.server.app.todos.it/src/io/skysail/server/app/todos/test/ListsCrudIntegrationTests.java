package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.todos.TodoList;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
@Slf4j
public class ListsCrudIntegrationTests extends IntegrationTests {

    private Browser browser;

    @Rule
    public TestRule watcher = new TestWatcher() {
       protected void starting(Description description) {
    	   log.info("");
    	   log.info("--------------------------------------------");
    	   log.info("{}running test '{}'", Client.TESTTAG, description.getMethodName());
    	   log.info("--------------------------------------------");
    	   log.info("");
       }
    };
    
    @Before
    public void setUp() {
        browser = new Browser(getBaseUrl());
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
        String id = location.getLastSegment();
        browser.asUser("admin").deleteTodoList(id);
        Representation html = browser.asUser("admin").getTodoLists();
        assertThat(html.getText(), not(containsString("crudlist2")));
    }

    @Test
    public void altering_todolist_updates_existing_todolist() throws Exception {
        Reference location = browser.asUser("admin").createTodoList(new TodoList("crudlist2"));
        String id = location.getLastSegment();
        String html = browser.asUser("admin").getTodoList(id).getText();
        //assertThat(html, containsString("crudlist2!"));
    }

}
