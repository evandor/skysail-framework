package io.skysail.server.app.bookmarks.it;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import io.skysail.client.testsupport.BrowserTests;
import io.skysail.server.app.bookmarks.Bookmark;
import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.representation.Representation;

/**
 * Integration tests for creating, reading, updating, and deleting Bookmarks.
 *
 */
@Slf4j
@Ignore
public class BookmarksCrudIntegrationTests extends BrowserTests<BookmarksBrowser, Bookmark> {

    @Before
    public void setUp() {
        browser = new BookmarksBrowser(MediaType.APPLICATION_JSON,determinePort());
    }

    @Test
    public void creating_bookmarks_persists_it() throws Exception {
        browser.asUser("admin").createBookmark(new Bookmark("http://www.heise.de", "heise"));
        String html = browser.asUser("admin").getBookmarks().getText();
        assertThat(html, containsString("http://www.heise.de"));
    }

    @Test
    @Ignore
    public void new_bookmark_can_be_deleted_by_owner() throws Exception {
        Reference location = browser.asUser("admin").createBookmark(new Bookmark("http://localhost:2014","home"));
        String id = location.getLastSegment(true);
        browser.asUser("admin").deleteBookmark(id);
        Representation html = browser.asUser("admin").getBookmarks();
        assertThat(html.getText(), not(containsString("localhost")));
    }
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
