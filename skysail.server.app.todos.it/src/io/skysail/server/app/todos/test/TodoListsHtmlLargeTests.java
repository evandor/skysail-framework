package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.test.browser.TodoListBrowser;

import java.io.IOException;
import java.math.BigInteger;

import org.junit.*;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 */
public class TodoListsHtmlLargeTests extends IntegrationTests<TodoListBrowser, TodoList> {

    //private static BrowserUtils browserUtils;

    private TodoList todoList;

    @BeforeClass
    public static void before() {
      //  browserUtils = new BrowserUtils();
    }

    @Before
    public void setUp() {
        browser = new TodoListBrowser(MediaType.TEXT_HTML, determinePort());
        browser.setUser("admin");
        todoList = createRandomTodoList();
    }

    @After
    public void tearDown() throws Exception {
         //browserUtils.screenShot(browser.getUrl());
    }

    @Test
    public void creating_new_todolist_will_persists_it() throws Exception {
        createListAndCheckAssertions();
    }

    private void createListAndCheckAssertions() throws IOException {
        browser.createTodoList(todoList);
        String html = browser.getTodoLists().getText();
        assertThat(html, containsString(todoList.getName()));
        assertThat(html, containsString(todoList.getName().substring(0, 17) + "..."));
    }

    private TodoList createRandomTodoList() {
        return new TodoList(new BigInteger(130, random).toString(32));
    }
}

