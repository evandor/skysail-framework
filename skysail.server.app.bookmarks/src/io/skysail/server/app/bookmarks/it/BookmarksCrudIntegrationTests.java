package io.skysail.server.app.bookmarks.it;

import io.skysail.server.app.bookmarks.Bookmark;

import org.junit.Before;
import org.junit.Test;
import org.restlet.data.MediaType;

/**
 * Integration tests for creating, reading, updating, and deleting TodoLists.
 *
 */
public class BookmarksCrudIntegrationTests extends IntegrationTests {
    
    @Before
    public void setUp() {
        browser = new Browser(getBaseUrl(), MediaType.APPLICATION_JSON);
    }

    @Test
    public void creating_todolist_persists_new_todolist() throws Exception {
        browser.asUser("admin").createBookmark(new Bookmark("http://www.heise.de"));
      //  String html = browser.asUser("admin").getBookmarks().getText();
        //assertThat(html, containsString("crudlist1"));
    }
    
//    @Test
//    public void new_todolist_can_be_deleted_by_owner() throws Exception {
//        Reference location = browser.asUser("admin").createTodoList(new TodoList("crudlist2"));
//        String id = location.getLastSegment(true);
//        browser.asUser("admin").deleteTodoList(id);
//        Representation html = browser.asUser("admin").getTodoLists();
//        assertThat(html.getText(), not(containsString("crudlist2")));
//    }
//
//    @Test
//    @Ignore
//    public void altering_todolist_updates_existing_todolist() throws Exception {
//        TodoList theTodoList = new TodoList("crudlist3");
//        Reference location = browser.asUser("admin").createTodoList(theTodoList);
//        String id = location.getLastSegment(true);
//        String html = browser.asUser("admin").getTodoList(id).getText();
//        assertThat(html, containsString("crudlist3"));
//        
//        theTodoList.setId(id);
//        theTodoList.setName("crudlist3!");
//        browser.asUser("admin").updateTodoList(theTodoList);
//    }

}
