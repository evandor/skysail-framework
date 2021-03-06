package io.skysail.server.app.todos.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.app.todos.TodoList;
import io.skysail.server.app.todos.test.browser.*;
import io.skysail.server.app.todos.todos.Todo;

import org.junit.*;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;

/**
 * Integration tests for creating, reading, updating, and deleting Todos.
 *
 */
public class TodosCrudIntegrationTests extends BrowserTests<TodosBrowser, Todo> {
    
    private TodoListBrowser listBrowser;

    @Before
    public void setUp() {
        browser = new TodosBrowser(MediaType.APPLICATION_JSON,determinePort());
        browser.setUser("admin");
        
        listBrowser = new TodoListBrowser(MediaType.APPLICATION_JSON,determinePort());
        listBrowser.setUser("admin");
    }

    @Test
    public void creating_todo_in_new_list_persists_them() throws Exception {
        String id = listBrowser.createTodoList(new TodoList("list1"));
        browser.createTodo(id, new Todo("todo1"));
        Representation todosForList = browser.getTodosForList(id);
        assertThat(todosForList.getText(), containsString("todo1"));
    }
    
    @Test
    @Ignore
    public void new_todolist_can_be_deleted_by_owner() throws Exception {
        String id = browser.createTodoList(new TodoList("crudlist2"));
        listBrowser.deleteTodoList(id);
        Representation html = listBrowser.getTodoLists();
        assertThat(html.getText(), not(containsString("crudlist2")));
    }

    

}
