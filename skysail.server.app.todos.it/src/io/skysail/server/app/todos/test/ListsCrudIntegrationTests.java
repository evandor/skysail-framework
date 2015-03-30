package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import lombok.extern.slf4j.Slf4j;
import io.skysail.client.testsupport.Client;
import io.skysail.server.app.todos.TodoList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.restlet.data.Reference;

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
    public void altering_todolist_updates_existing_todolist() throws Exception {
        Reference location = browser.asUser("admin").createTodoList(new TodoList("crudlist2"));
        System.out.println(location);
        String html = browser.asUser("admin").getTodoLists().getText();
        assertThat(html, containsString("crudlist2!"));
    }

}
