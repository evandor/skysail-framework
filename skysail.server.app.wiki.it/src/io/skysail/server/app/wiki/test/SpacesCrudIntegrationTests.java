package io.skysail.server.app.wiki.test;

import io.skysail.client.testsupport.IntegrationTests;
import io.skysail.server.app.wiki.spaces.Space;

import org.junit.Before;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
public class SpacesCrudIntegrationTests extends IntegrationTests<SpacesBrowser, Space> {
    
    private Space space;

    @Before
    public void setUp() {
        browser = new SpacesBrowser(MediaType.APPLICATION_JSON);
        browser.setUser("admin");
        space = createSpace();
    }

//    @Test
//    public void creating_todolist_persists_new_todolist() throws Exception {
//        browser.createTodoList(todoList);
//        String html = browser.getTodoLists().getText();
//        assertThat(html, containsString(todoList.getName()));
//    }
//    
//    @Test
//    public void new_todolist_can_be_deleted_by_owner() throws Exception {
//        String id = browser.createTodoList(todoList);
//        browser.deleteTodoList(id);
//        assertThat(browser.getTodoLists().getText(), not(containsString(todoList.getName())));
//    }
//
//    @Test
//    public void altering_todolist_updates_existing_todolist() throws Exception {
//        String id = browser.createTodoList(todoList);
//        assertThat(browser.getTodoList(id).getText(), containsString(todoList.getName()));
//        
//        todoList.setId(id);
//        //todoList.setName("crudlist3!");
//        todoList.setDesc("description changed");
//        browser.updateTodoList(todoList);
//    }

    private Space createSpace() {
        return new Space();
    }
}

